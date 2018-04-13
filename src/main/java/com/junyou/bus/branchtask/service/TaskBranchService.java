package com.junyou.bus.branchtask.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.configure.ConditionType;
import com.junyou.bus.branchtask.configure.ZhiXianRenWuConfig;
import com.junyou.bus.branchtask.configure.ZhiXianRenWuConfigExportService;
import com.junyou.bus.branchtask.dao.TaskBranchDao;
import com.junyou.bus.branchtask.entity.TaskBranch;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;


@Service
public class TaskBranchService {
	
	@Autowired
	private TaskBranchDao taskDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ZhiXianRenWuConfigExportService zhiXianConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	
	public List<TaskBranch> initAll(long userRoleId){
		List<TaskBranch> taskDays = taskDao.initAll(userRoleId);
		return taskDays;
	}
	
	/**
	 * 登陆后处理逻辑
	 * @param userRoleId
	 */
	public void onlineHandle(long userRoleId){
		
		List<TaskBranch> tasks = taskDao.cacheLoadAll(userRoleId);
		
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		if(userRole != null && userRole.getLevel() == 1){
			branchActivity(userRoleId, ConditionType.HERO_LEVEL, 1);
		}
		
		//添加任务监听
		if(tasks != null && tasks.size() > 0){
			List<Object[]> taskvos = new ArrayList<>();
			for (TaskBranch task : tasks) {
				if(task.getStatus() == 2){
					continue;
				}
				Object[] result = coverTaskVo(task);
				taskvos.add(result);
				
				guildTaskCheck(userRoleId, task);
			}
			
			BusMsgSender.send2One(userRoleId, ClientCmdType.SEND_BRANCHTASK_STATE, taskvos.toArray());
		}
	}
	
	//
	private void guildTaskCheck(long userRoleId, TaskBranch task) {
		//处理离线的公会加入支线任务
		ZhiXianRenWuConfig  config = zhiXianConfigExportService.loadById(task.getTaskId());
		if(config == null){
			ChuanQiLog.error("支线任务配置表不存在,id="+task.getTaskId());
			return;
		}
		if(config.getType() == BranchEnum.B13.getBranchTaskType() && task.getStatus() == 0){
			//check是否已经加入公会
			if(guildExportService.getGuildInfo(userRoleId) != null){
				completeBranch(userRoleId, BranchEnum.B13, 1);
			}
		}
	}
	
	@Autowired
	private GuildExportService guildExportService;
	
	/**
	 * 转换成 RaskVo
	 * @param taskBranch
	 * @return
	 */
	private Object[] coverTaskVo(TaskBranch taskBranch){
		return new Object[]{taskBranch.getTaskId(),taskBranch.getStatus(),taskBranch.getProgress()};
	}
	
	
	/**
	 * 获取玩家任务信息
	 * @param userRoleId
	 * @return
	 */
	public List<TaskBranch> getTaskBranch(long userRoleId){
		//判定是否是公会日常,并确认是否有公会
		List<TaskBranch> taskBranchs = taskDao.cacheLoadAll(userRoleId);
		return taskBranchs;
	}
	
	/**
	 * 获取玩家任务信息
	 * @param userRoleId
	 * @return  taskId = taskBranch
	 */
	public Map<Integer,TaskBranch> getTaskBranchMap(long userRoleId){
		//判定是否是公会日常,并确认是否有公会
		List<TaskBranch> taskBranchs = taskDao.cacheLoadAll(userRoleId);
		if(taskBranchs == null || taskBranchs.size() == 0){
			return null;
		}
		Map<Integer,TaskBranch> result = new HashMap<Integer, TaskBranch>();
		for (TaskBranch taskBranch : taskBranchs) {
			result.put(taskBranch.getTaskId(), taskBranch);
		}
		return result;
	}
	
	/**
	 * 获取玩家任务信息
	 * @param userRoleId
	 * @return
	 */
	public TaskBranch getTaskBranch(long userRoleId,final int taskId){
		
		List<TaskBranch> taskBranchs = taskDao.cacheLoadAll(userRoleId,new IQueryFilter<TaskBranch>() {
			boolean isStop;
			@Override
			public boolean check(TaskBranch entity){
				if(entity.getTaskId() == taskId){
					isStop = true;
					return true;
				}
				return false;
			}

			@Override
			public boolean stopped() {
				return isStop;
			}
		});
		
		if(taskBranchs!=null && taskBranchs.size() > 0){
			return taskBranchs.get(0);
		}
		
		return null;
	}
	
	/**
	 * 完成度统计
	 * @param roleId
	 * @param acEnum
	 * @param params
	 */
	public void completeBranch(Long userRoleId, BranchEnum branchEnum, Object params) {
		try {
			List<Object[]> result = new ArrayList<>();
			List<TaskBranch> taskBranchs = getTaskBranch(userRoleId);
			long nowTime = System.currentTimeMillis();
			for (TaskBranch taskBranch : taskBranchs) {
				if(taskBranch.getStatus() != 0){
					continue;
				}
				ZhiXianRenWuConfig config = zhiXianConfigExportService.loadById(taskBranch.getTaskId());
				if(config == null){
					ChuanQiLog.error("branch config is not exits,id="+taskBranch.getTaskId());
					break;
				}
				
				if(config.getType() != branchEnum.getBranchTaskType()){
					continue;
				}
				
				//数量累计
				if(BranchEnum.isNumberAdd(branchEnum)){
					int addCount = (int)params;
					taskBranch.setProgress(taskBranch.getProgress() + addCount);
					taskBranch.setUpdateTime(nowTime);
					int target = (int)config.getTarget();
					if(target <= taskBranch.getProgress()){
						taskBranch.setStatus(1);
					}
					taskDao.cacheUpdate(taskBranch, userRoleId);
					
					result.add(coverTaskVo(taskBranch));
				}
				
				//大小比较
				if(BranchEnum.isNumberCompare(branchEnum)){
					int souceData = (int)params;
					int target = (int)config.getTarget();
					if(souceData >= target){
						taskBranch.setProgress(target);
					}else{
						if(souceData > taskBranch.getProgress()){
							taskBranch.setProgress(souceData);
						}
					}
					taskBranch.setUpdateTime(nowTime);
					if(target <= taskBranch.getProgress()){
						taskBranch.setStatus(1);
					}
					taskDao.cacheUpdate(taskBranch, userRoleId);
					
					result.add(coverTaskVo(taskBranch));
				}
			}
			
			if(result.size() > 0){
				BusMsgSender.send2One(userRoleId, ClientCmdType.SEND_BRANCHTASK_STATE, result.toArray());
			}
		} catch (Exception e) {
			ChuanQiLog.error("completeBranch error:userRoleId:"+userRoleId + "\tbranchEnum:"+branchEnum+"\tparams:"+params,e);
		}
	}
	
	/**
	 * 激活对应的支线
	 * @param roleId
	 * @param openType {@link ConditionType}
	 * @param params
	 */
	public void branchActivity(Long userRoleId, int openType, Object params) {
		Map<Integer,TaskBranch> taskBranchs = getTaskBranchMap(userRoleId);
		List<Object[]> clientDatas = new ArrayList<>();
		for (ZhiXianRenWuConfig config : zhiXianConfigExportService.loadAll().values()) {
			
			if(taskBranchs != null && taskBranchs.size() > 0){
				if(taskBranchs.containsKey(config.getId())){
					continue;
				}
			}
			
			if (!config.getOpenCondition().isType(openType)){
				continue;
			}
			
			//是否达到激活条件
			if (!config.getOpenCondition().isOpen(params)){
				continue;
			}
			
			//激活
			TaskBranch taskBranch = createTaskBranch(userRoleId, config.getId());
			clientDatas.add(coverTaskVo(taskBranch));
		}
		
		if(clientDatas.size() > 0){
			BusMsgSender.send2One(userRoleId, ClientCmdType.SEND_BRANCHTASK_STATE, clientDatas.toArray());
		}
	}
	
	/**
	 * 任务ID
	 * @param userRoleId
	 * @param taskId
	 * @return
	 */
	private TaskBranch createTaskBranch(long userRoleId,int taskId){
		TaskBranch taskBranch = new TaskBranch();
		taskBranch.setUserRoleId(userRoleId);
		taskBranch.setStatus(0);
		taskBranch.setProgress(0);
		taskBranch.setUpdateTime(System.currentTimeMillis());
		taskBranch.setTaskId(taskId);
		taskBranch.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		taskDao.cacheInsert(taskBranch, userRoleId);
		return taskBranch;
	}
	
	/**
	 * 领取奖励
	 * @param userRoleId
	 * @param taskIds
	 * @return
	 */
	public Object[] reciveTask(Long userRoleId, Object[] taskIds) {
		if(taskIds == null){
			ChuanQiLog.error("recive taskIds is null");
			return AppErrorCode.CLIENT_DATA_ERROR;
		}
		Map<Integer, TaskBranch> taskBranchs = getTaskBranchMap(userRoleId);
		if(taskBranchs == null || taskBranchs.size() ==0){
			ChuanQiLog.error("taskBranchs is null");
			return AppErrorCode.CLIENT_DATA_ERROR;
		}
		
		
		Map<String,Integer> items = new HashMap<>();//道具奖励
		long exp  = 0;
		int zhenqi = 0;
		int money = 0;
		
		List<Integer> tIds = new ArrayList<>();
		for (Object obj : taskIds) {
			int taskId = CovertObjectUtil.object2int(obj);
			TaskBranch taskBranch = taskBranchs.get(taskId);
			if(taskBranch == null){
				ChuanQiLog.error("revices taskId is not exits,id="+taskId);
				continue;
			}
			
			if(taskBranch.getStatus() != 1){
				ChuanQiLog.error(" taskId is revice  status is error,id="+taskId+" status:" +taskBranch.getStatus());
				continue;
			}
			
			ZhiXianRenWuConfig config = zhiXianConfigExportService.loadById(taskId);
			if(config == null){
				ChuanQiLog.error(" taskId config  is exits ,id="+taskId);
				continue;
			}
			
			ObjectUtil.mapAdd(items, config.getAwards());
			exp += config.getExp();
			zhenqi += config.getZhenQi();
			money += config.getMoney();
			
			tIds.add(taskId);
		}
		
		Object[] flagBag = roleBagExportService.checkPutInBag(items, userRoleId);
		if(flagBag != null){
			return flagBag;
		}
		
		long nowTime = System.currentTimeMillis();
		List<Object[]> clientDatas = new ArrayList<>();
		for (Integer id : tIds) {
			TaskBranch taskBranch = taskBranchs.get(id);
			taskBranch.setStatus(2);
			taskBranch.setUpdateTime(nowTime);
			taskDao.cacheUpdate(taskBranch, userRoleId);
			
			clientDatas.add(coverTaskVo(taskBranch));
		}
		
		roleBagExportService.putGoodsAndNumberAttr(items, userRoleId,  
				GoodsSource.ZHIXIAN_GET,
				LogPrintHandle.GET_ZHIXIAN_REWARD, 
				LogPrintHandle.GBZ_ZHIXIAN_REWARD_LB, true);
		
		if(zhenqi > 0){
			roleExportService.addZhenqi(userRoleId, zhenqi);	
		}
		if(money > 0){
			accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_ZHIXIAN_REWARD, LogPrintHandle.GBZ_ZHIXIAN_REWARD_LB);	
		}
		if(exp > 0){
			roleExportService.incrExp(userRoleId, exp);	
		}
		
		//推送支线任务激活
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_ACTIVITY, new Object[]{ConditionType.ZHIXIAN_RENWU,tIds.toArray()});
		//推送客服端更新
		BusMsgSender.send2One(userRoleId, ClientCmdType.SEND_BRANCHTASK_STATE, clientDatas.toArray());
		
		return new Object[]{1,tIds.toArray()};
	}
}