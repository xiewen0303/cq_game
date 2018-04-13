package com.junyou.bus.task.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.role.IncrRoleResp;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.task.TaskOutputWrapper;
import com.junyou.bus.task.configure.ZhuXianRenWuBiaoConfig;
import com.junyou.bus.task.configure.ZhuXianRenWuBiaoConfigExportService;
import com.junyou.bus.task.dao.TaskDao;
import com.junyou.bus.task.entity.AbsTask;
import com.junyou.bus.task.entity.Task;
import com.junyou.bus.task.manage.CaijiTaskManager;
import com.junyou.bus.task.manage.KillTaskManager;
import com.junyou.bus.task.manage.OtherTaskManager;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.FinishTaskLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.tunnel.PublicMsgSender;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;


@Service
public class TaskService implements ITaskService{
	
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private ZhuXianRenWuBiaoConfigExportService zhuXianRenWuBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	
	public List<Task> initTask(Long userRoleId) {
		return taskDao.initTask(userRoleId);
	}
	
	public Object[] getTask(Long userRoleId){
		Task task = getTaskInfo(userRoleId);
		return TaskOutputWrapper.getTaskRunState(task);
	}
	
	/**
	 * 获取玩家主线任务
	 * @param userRoleId
	 * @return
	 */
	private Task getTaskInfo(Long userRoleId){
		List<Task> taskMains = taskDao.cacheLoadAll(userRoleId);
		if(taskMains != null && taskMains.size() > 0){
			return taskMains.get(0);
		}
		return null;
	}
	
	public void onlineHandle(Long userRoleId){
		List<Task> list = taskDao.cacheLoadAll(userRoleId);
		if(list == null || list.size() < 1){
			//获得第一个主线任务 TODO wind  需要测试一下
			ZhuXianRenWuBiaoConfig	zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.loadById(null);
			if(zhuXianRenWuBiaoConfig == null){
				ChuanQiLog.error("第一个任务不存在,taskId="+zhuXianRenWuBiaoConfigExportService.getMin());
				return;
			}
			
			createTask(userRoleId, zhuXianRenWuBiaoConfig);
			
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			int type = GameConstants.GAME_JINDU_TYPE_FIRST;
			if((role.getUserId().hashCode() & 1) == 1){
				type = GameConstants.GAME_JINDU_TYPE_FIRST_OTHER;
			}
			//游戏进度--接取第一个任务
			PublicMsgSender.send2PublicInner(InnerCmdType.GAME_JINDU, GameConstants.DEFAULT_ROLE_ID, type);
//			return;
		}
		for (Task task : list) {
			task.setTaskService(this);
			if(task.getState().equals(GameConstants.TASK_RECEIVE_STATE)){//已接任务分配管理器
				ZhuXianRenWuBiaoConfig config = zhuXianRenWuBiaoConfigExportService.loadById(task.getTaskId());
				if(config == null){
					ChuanQiLog.error("ZhuXianRenWuBiaoConfig is null.id:"+task.getTaskId());
					continue;
				}
				task.setTargetId(config.getData1());
				task.setNeedCount(config.getData2());
				if(config.getType().equals(GameConstants.TASK_TYPE_KILL)){
					KillTaskManager.getManager().addTask(task.getUserRoleId(), task);
				}//主线任务中，无其他任务类型，采集任务无需监听
				
				//处理采集
				if(config.getType() == GameConstants.TASK_TYPE_CAIJI){
					if(task.getProgress() >= config.getData2()){
						task.setState(GameConstants.TASK_COMPLETE);
						completeTask(userRoleId);
					}
				}
			}
			

		}
	}
	public void offlineHandle(Long userRoleId){
		KillTaskManager.getManager().offline(userRoleId);
		CaijiTaskManager.getManager().offline(userRoleId);
		OtherTaskManager.getManager().offline(userRoleId);
	}

	@Override
	public boolean handle(AbsTask absTask,BusMsgQueue busMsgQueue) {
		Task task = (Task)absTask;
		boolean result = false;
		int progress = task.getProgress() + 1;
		if(progress >= task.getNeedCount()){
			task.setState(GameConstants.TASK_COMPLETE);
			result = true;
		}
		task.setProgress(progress);
		update(task);
		if(result){
			busMsgQueue.addMsg(task.getUserRoleId(), ClientCmdType.REFRESH_TASK_STATE, TaskOutputWrapper.getTaskRunState(task));
		}else{
			//处理任务变化推送给前端
			busMsgQueue.addMsg(task.getUserRoleId(), ClientCmdType.REFRESH_TASK_PROGRESS, progress);
		}
		return result;
	}
	
	public void taskChanageCondition(Object[] roleIds,String conditionId,int type,BusMsgQueue busMsgQueue){
		if(type == GameConstants.TASK_TYPE_KILL){
			for (Object roleId : roleIds) {
				Long userRoleId = CovertObjectUtil.obj2long(roleId);
				KillTaskManager.getManager().handle(userRoleId, conditionId,busMsgQueue);
			}
		}
		//TODO wind 待测
//		else if(type == GameConstants.TASK_TYPE_CAIJI){
//			for (Object roleId : roleIds) {
//				Long userRoleId = CovertObjectUtil.obj2long(roleId);
//				CaijiTaskManager.getManager().handle(userRoleId, conditionId,busMsgQueue);
//			}
//		}
	}
	/**
	 * 获取任务状态
	 * @param userRoleId
	 * @return
	 */
	public Object[] getTaskRunState(Long userRoleId){
		Task task = getTaskInfo(userRoleId);
		ZhuXianRenWuBiaoConfig zhuXianRenWuBiaoConfig = null;
		
		if(task != null){
			if(task.getState().equals(GameConstants.TASK_PAY) || task.getState().equals(GameConstants.TASK_WJ_STATE)){
				receiveTask(task);
				return TaskOutputWrapper.getTaskRunState(task);
			}
			zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.loadById(task.getTaskId());
			if(zhuXianRenWuBiaoConfig != null){
				if(task.getState().intValue() == GameConstants.TASK_RECEIVE_STATE){//任务已接
					if(GameConstants.TASK_TYPE_CAIJI == zhuXianRenWuBiaoConfig.getType() || GameConstants.TASK_TYPE_CLIENT_KILL == zhuXianRenWuBiaoConfig.getType()){
						task.setProgress(task.getProgress() + 1);
					}
					
					if(GameConstants.TASK_TYPE_TALK == zhuXianRenWuBiaoConfig.getType() || task.getProgress() >= zhuXianRenWuBiaoConfig.getData2()){//会话任务，或任务达标
						task.setState(GameConstants.TASK_COMPLETE);
					}
					update(task);
					
					
					//采集完成后直接接下一个任务
					if(GameConstants.TASK_TYPE_CAIJI == zhuXianRenWuBiaoConfig.getType()){
						Object[] result = completeTask(userRoleId);
						if(result != null && (int)result[0] != 0){
							BusMsgSender.send2One(userRoleId,ClientCmdType.FINISH_AND_RECIVE_TASK,result);
							return null;
						}
					}
				}
			}
			
		}else{
			ChuanQiLog.error("进入创建任务协议，请检查前段登录协议顺序  1550  1552");
//			/**
//			 * TODO wind  这里下面的代码应该是不需要的。
//			 */
			zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.loadById(null);
			if(zhuXianRenWuBiaoConfig == null){
				return null;
			}
			task = createTask(userRoleId, zhuXianRenWuBiaoConfig);
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			int type = GameConstants.GAME_JINDU_TYPE_FIRST;
			if((role.getUserId().hashCode() & 1) == 1){
				type = GameConstants.GAME_JINDU_TYPE_FIRST_OTHER;
			}
			//游戏进度--接取第一个任务
			PublicMsgSender.send2PublicInner(InnerCmdType.GAME_JINDU, GameConstants.DEFAULT_ROLE_ID, type);
		}
		//处理数据，转换成客户端需要的格式
		return TaskOutputWrapper.getTaskRunState(task);
	}
	
	private Task createTask(Long userRoleId,ZhuXianRenWuBiaoConfig config){
		Task task = new Task();
		task.setUserRoleId(userRoleId);
		task.setTaskId(config.getId());
		task.setUpdateTime(GameSystemTime.getSystemMillTime());
		task.setTaskService(this);
		if(!config.getType().equals(GameConstants.TASK_TYPE_TALK)){//非会话业务需要处理进度统计
			task.setState(GameConstants.TASK_RECEIVE_STATE);
			task.setProgress(0);
			task.setTargetId(config.getData1());
			task.setNeedCount(config.getData2());
			if(config.getType().equals(GameConstants.TASK_TYPE_KILL)){
				KillTaskManager.getManager().addTask(task.getUserRoleId(), task);
			}
		}else{
			task.setState(GameConstants.TASK_COMPLETE);
		}
		taskDao.cacheInsert(task, userRoleId);
		return task;
	}
	
	private void update(Task task){
		task.setUpdateTime(GameSystemTime.getSystemMillTime());
		taskDao.cacheUpdate(task, task.getUserRoleId());
	}
	/**
	 * 接任务
	 * @param task
	 */
	private void receiveTask(Task task){
		ZhuXianRenWuBiaoConfig zhuXianRenWuBiaoConfig = null;
		if(task.getState().intValue() == GameConstants.TASK_PAY){
			zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.nextById(task.getTaskId());
			if(zhuXianRenWuBiaoConfig == null){
				return;
			}
			task.setTaskId(zhuXianRenWuBiaoConfig.getId());
			task.setState(GameConstants.TASK_WJ_STATE);
			update(task);
		}else if(task.getState().intValue() != GameConstants.TASK_WJ_STATE){
			return;
		}
		//接任务
		if(zhuXianRenWuBiaoConfig == null){
			zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.loadById(task.getTaskId());
			if(zhuXianRenWuBiaoConfig == null){
				return;
			}
		}
		//玩家等级是否达到任务可接等级
		Long userRoleId = task.getUserRoleId();
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(zhuXianRenWuBiaoConfig.getMinlevel().intValue() > role.getLevel().intValue()){
			return;
		}
		task.setProgress(0);
		//无条件任务 状态直接设置成 已完成
		if(zhuXianRenWuBiaoConfig.getType().equals(GameConstants.TASK_TYPE_TALK)){
			task.setState(GameConstants.TASK_COMPLETE);
		}else{
			task.setState(GameConstants.TASK_RECEIVE_STATE);
			if(zhuXianRenWuBiaoConfig.getType().equals(GameConstants.TASK_TYPE_KILL)){
				task.setTargetId(zhuXianRenWuBiaoConfig.getData1());
				task.setNeedCount(zhuXianRenWuBiaoConfig.getData2());
				KillTaskManager.getManager().addTask(userRoleId, task);
			}
		}
		update(task);
		return;
	}
	
	/**
	 * 交付任务
	 * @param userRoleId
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] completeTask(Long userRoleId){
		Task task = getTaskInfo(userRoleId);
		//任务已不存在
		if(task == null){
			return AppErrorCode.TASK_NOT_EXIST;
		}
		
		//任务已交付
		if(task.getState().intValue() == GameConstants.TASK_PAY){
			return AppErrorCode.TASK_IS_RECIVE;
		}
		
		//任务还没完成
		if(task.getState().intValue() != GameConstants.TASK_COMPLETE){
			return AppErrorCode.TASK_NOT_FINISH;
		}
		
		//玩家等级是否达到任务可接等级
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		ZhuXianRenWuBiaoConfig zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.loadById(task.getTaskId());
		if(zhuXianRenWuBiaoConfig == null){
			//任务已不存在
			ChuanQiLog.error("Task is not exist. TaskId is " + task.getTaskId());
		}
		
		//任务状态修改为已交付
		task.setState(GameConstants.TASK_PAY);
		update(task);
		
		//奖励物品
		Map<String,Integer> rewardItems = zhuXianRenWuBiaoConfig.getAllAwardItemByJob(role.getConfigId());
		String content = EmailUtil.getCodeEmail(GameConstants.TASK_EMAIL_CONTENT_CODE, zhuXianRenWuBiaoConfig.getName());
		int answer = roleBagExportService.putInBagOrEmail(rewardItems, userRoleId, GoodsSource.ZHUXIAN_TASK, true,content);
		//数值奖励
		long money = accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, zhuXianRenWuBiaoConfig.getJiangmoney(), userRoleId, LogPrintHandle.GET_TASK, LogPrintHandle.GBZ_TASK);
		long zhenqi = zhuXianRenWuBiaoConfig.getZhenqi();
		roleExportService.addZhenqi(userRoleId, zhenqi);
		IncrRoleResp incrRoleResp = roleExportService.incrExp(userRoleId, zhuXianRenWuBiaoConfig.getJiangexp());
		long exp = 0;
		if(incrRoleResp != null){
			exp = incrRoleResp.getIncrExp();
		}
		
		//任务完成日志
		if(zhuXianRenWuBiaoConfig.isRizhi() && role != null && role.isFirstLogin()){
			long onlineTime = GameSystemTime.getSystemMillTime() - role.getOnlineTime();
			GamePublishEvent.publishEvent(new FinishTaskLogEvent(userRoleId, role.getName(),task.getTaskId(),onlineTime));
		}
		//下一个任务Id
		receiveTask(task);
		return new Object[]{answer,money,exp,zhenqi,TaskOutputWrapper.getTaskRunState(task)};
	}
	/**
	 * GM跳任务
	 * @param userRoleId
	 * @param taskId
	 */
	public void gmChangeTask(Long userRoleId,int taskId){
		ZhuXianRenWuBiaoConfig zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.loadById(taskId);
		if(zhuXianRenWuBiaoConfig == null){
			return;
		}
		Task task = getTaskInfo(userRoleId);
		zhuXianRenWuBiaoConfig = zhuXianRenWuBiaoConfigExportService.loadById(task.getTaskId());
		if(zhuXianRenWuBiaoConfig.getType().equals(GameConstants.TASK_TYPE_KILL)){
			KillTaskManager.getManager().cancelTask(userRoleId, task);
		}
		task.setTaskId(taskId);
		task.setState(GameConstants.TASK_WJ_STATE);
		receiveTask(task);
		BusMsgSender.send2One(userRoleId, ClientCmdType.REFRESH_TASK_STATE, TaskOutputWrapper.getTaskRunState(task));
	}
	
}