package com.junyou.bus.daytask.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.daytask.configure.RiChangJiangLiConfig;
import com.junyou.bus.daytask.configure.RiChangJiangLiConfigExportService;
import com.junyou.bus.daytask.configure.RiChangRenWuConfig;
import com.junyou.bus.daytask.configure.RiChangRenWuConfigExportService;
import com.junyou.bus.daytask.dao.TaskDayDao;
import com.junyou.bus.daytask.entity.TaskDay;
import com.junyou.bus.daytask.filter.TaskDayTypeFilter;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.resource.export.RoleResourceBackExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.task.entity.AbsTask;
import com.junyou.bus.task.manage.KillTaskManager;
import com.junyou.bus.task.service.ITaskService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.export.GuildExportService;
import com.junyou.public_.guild.manager.GuildManager;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;


@Service
public class TaskDayService implements ITaskService {
	
	@Autowired
	private TaskDayDao taskDayDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RiChangRenWuConfigExportService riChangRenWuConfigExportService;
	@Autowired
	private RiChangJiangLiConfigExportService riChangJiangLiConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private GuildExportService guildExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private RoleResourceBackExportService roleResourceBackExportService;
	
	
	public List<TaskDay> initAll(long userRoleId){
		List<TaskDay> taskDays = taskDayDao.initAll(userRoleId);
		
		return taskDays;
	}
	
	/**
	 * 登陆后处理逻辑
	 * @param userRoleId
	 */
	public void onlineHandle(long userRoleId){
		List<TaskDay> taskDays = taskDayDao.cacheLoadAll(userRoleId);
		//添加任务监听
		if(taskDays != null){
			for (TaskDay taskDay : taskDays) {
				initConfigData(taskDay);
				//处理跨天信息
				checkCrossDay(taskDay);
				
				addTaskDayListener(userRoleId, taskDay);
			}
		}
	}
	
	/**
	 * 判定数据是否已经初始化时使用
	 * @param taskDays
	 * @param type
	 * @return
	 */
	public boolean isContain(List<TaskDay> taskDays,int type){
		if(taskDays == null) return false;
		
		for (TaskDay taskDay : taskDays) {
			if(taskDay.getTaskDayType() == type){
				return true;
			}
		}
		 return false;
	}
	
	
	/**
	 * 获取玩家任务信息
	 * @param userRoleId
	 * @return
	 */
	public TaskDay getTaskDay(long userRoleId,int type){
		//判定是否是公会日常,并确认是否有公会
		if(type == GameConstants.GUILD_DAY && !isGuild(userRoleId)){
			return null;
		}
		
		List<TaskDay> taskDays = taskDayDao.cacheLoadAll(userRoleId,new TaskDayTypeFilter(type));
		if(taskDays == null){
			taskDays =new ArrayList<>();
		}
		TaskDay taskDay = null;
		 
		if(taskDays.size() ==0){
			
			taskDay = createTaskDay(userRoleId,type);
			if(taskDay == null) return null;
			taskDayDao.cacheInsert(taskDay, userRoleId);
			
			//添加到监听器中去 新开启时添加到杀怪的监听器中
//			addTaskDayListener(userRoleId, taskDay);
		}else{
			
			taskDay = taskDays.get(0);
//			initConfigData(taskDay);
//			
//			//跨天添加到监听器中去 新开启时添加到杀怪的监听器中
//			if(taskDay.getLastOptTime() <= DatetimeUtil.getDate00Time()){	
//				//处理跨天信息
//				doCrossDay(taskDay, userRoleId);
//				
//				addTaskDayListener(userRoleId, taskDay);
//			}
//			taskDayDao.cacheUpdate(taskDay, userRoleId);
		}
		
		return taskDay;
	}
	
	/**
	 * 跨天检测
	 */
	private void checkCrossDay(TaskDay taskDay){
		
		if(taskDay.getLastOptTime() != 0l &&  taskDay.getLastOptTime() <= DatetimeUtil.getDate00Time()){
//			doCrossDay(taskDay);
			taskKuaTian(taskDay);
		}
	}
	private void doCrossDay(TaskDay taskDay){
		taskDay.setKillCount(0);
		
		Integer level = getRoleLevel(taskDay.getUserRoleId());
		if(level == null) return;
		
		RiChangRenWuConfig riChangRenWuConfig  = getTaskDayConfig(level,taskDay.getTaskDayType());
		if(riChangRenWuConfig == null) return;
		taskDay.setTaskId(riChangRenWuConfig.getId());
		
		Integer loopId = riChangJiangLiConfigExportService.getConfigIdByLevel(level,taskDay.getTaskDayType());
		if(loopId == null){
			return;
		}
		taskDay.setLoopId(loopId);
		
		taskDay.setTimes(1);
		taskDay.setStatus(GameConstants.TASK_STATUS_NO);
		taskDay.setLastOptTime(GameSystemTime.getSystemMillTime()); 
		taskDayDao.cacheUpdate(taskDay, taskDay.getUserRoleId());
	}
	
	/**
	 * 初始化对应的文件信息
	 * @param taskDay
	 */
	private void initConfigData(TaskDay taskDay){
		if(taskDay == null) return;
		RiChangRenWuConfig riChangRenWuConfig = riChangRenWuConfigExportService.loadById(taskDay.getTaskId());
		if(riChangRenWuConfig == null){
			ChuanQiLog.error("riChangRenWuConfig configId is not exits,configId="+taskDay.getTaskId());
			RoleWrapper role = roleExportService.getLoginRole(taskDay.getUserRoleId());
			if(role == null){
				return;
			}
			//重新选取一次任务
			riChangRenWuConfig = riChangRenWuConfigExportService.randomConfig(role.getLevel(), taskDay.getTaskDayType());
			if(riChangRenWuConfig == null){
				return;
			}
			taskDay.setTaskId(riChangRenWuConfig.getId());
			taskDayDao.cacheUpdate(taskDay, taskDay.getUserRoleId());
		}
		
		taskDay.setNeedKillCount(riChangRenWuConfig.getNum());
		taskDay.setMonsterId(riChangRenWuConfig.getMonster());
		
		RiChangJiangLiConfig rcjlConfig = riChangJiangLiConfigExportService.loadById(taskDay.getLoopId());
		taskDay.setMaxTimes(rcjlConfig.getMaxcount());
	}
	
	
	
	
	/**
	 * 给玩家随机出来一个日常任务Id
	 * @param userRoleId
	 * @return
	 */
	public RiChangRenWuConfig getTaskDayConfig(Integer level,int type){ 
		if(level == null) return null;
		
		RiChangRenWuConfig riChangRenWuConfig = riChangRenWuConfigExportService.randomConfig(level, type);
		if(riChangRenWuConfig == null){
			return null;
		}
		 
		
		return riChangRenWuConfig;
	}
 
	
	
	/**
	 * 创建每日任务
	 * @param userRoleId
	 */
	public TaskDay createTaskDay(long userRoleId,int type){
		TaskDay taskDay =new  TaskDay();
		
		Integer level = getRoleLevel(userRoleId);
		if(level == null) return null;
		
		RiChangRenWuConfig riChangRenWuConfig  = getTaskDayConfig(level,type);
		if(riChangRenWuConfig == null) return null;
		taskDay.setTaskId(riChangRenWuConfig.getId());
		 
		Integer loopId = riChangJiangLiConfigExportService.getConfigIdByLevel(level,type);
		if(loopId == null){
			return null;
		}
		taskDay.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		taskDay.setTaskDayType(type);
		taskDay.setLoopId(loopId);
		taskDay.setTimes(1);
		taskDay.setUserRoleId(userRoleId);
		taskDay.setKillCount(0);
		taskDay.setLastOptTime(GameSystemTime.getSystemMillTime()); 
		taskDay.setMonsterId(riChangRenWuConfig.getMonster()); 
		taskDay.setStatus(GameConstants.TASK_STATUS_NO);
		taskDay.setRenwuCount(0);
		initConfigData(taskDay);
		
		return taskDay;
	}
	
//	private RoleWrapper getRoleWrapper(long userRoleId){
//		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
//		if(roleWrapper != null){
//			return roleWrapper;
//		}
//		
//		return null;
//	}
	
	private int getRoleLevel(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper != null){
			return roleWrapper.getLevel();
		}
		
		return 0;
	}
	
	
	
	@Override
	public boolean handle(AbsTask absTask, BusMsgQueue busMsgQueue) {
		boolean result =false;
		
		TaskDay taskDay  = (TaskDay)absTask;
		 
		if(taskDay.getKillCount()+1 >= taskDay.getNeedKillCount()){
			int oldTaskId = taskDay.getTaskId();
			long userRoleId = taskDay.getUserRoleId();
			  //完成单个任务
			 finishTaskDay(taskDay);
			 
			 //如果次数满了就移除监听
			 result = taskDay.getTimes() > taskDay.getMaxTimes();
			 taskDay.setRenwuCount(taskDay.getRenwuCount() + 1);
			 //发放奖励,并推送新的任务信息
			 Object[] finishInfo = getFinishInfo(userRoleId, oldTaskId, taskDay);
			 //发送击杀怪物数量
			 busMsgQueue.addMsg(taskDay.getUserRoleId(),getCmdKillByType(taskDay.getTaskDayType()), taskDay.getKillCount());
			 busMsgQueue.addMsg(userRoleId,  getCmdFinishByType(taskDay.getTaskDayType()), finishInfo);
		
			//完成1次任务推送一次成就
			try {
				if(taskDay.getTaskDayType() == GameConstants.TASK_DAY){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_DAYTASKCOUNT, 1});
					//修炼任务
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.TASK_RICHANG, 1});
				}else if(taskDay.getTaskDayType() == GameConstants.GUILD_DAY){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_GUILDTASKCOUNT, 1});
					//修炼任务
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.TASK_GUILD, 1});
				}
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			 
		}else{
			taskDay.setKillCount(taskDay.getKillCount()+1);
			//发送击杀怪物数量
			busMsgQueue.addMsg(taskDay.getUserRoleId(),getCmdKillByType(taskDay.getTaskDayType()), taskDay.getKillCount());
		}
		
		taskDayDao.cacheUpdate(taskDay, taskDay.getUserRoleId());
		 
		if(result){
			taskDay.setAddListener(false);
		}
		return result;
	}
	
	public Short getCmdFinishByType(int type){
		if(type == GameConstants.TASK_DAY){
			return ClientCmdType.TASK_DAY_FINISH;
		}
		if(type == GameConstants.GUILD_DAY){
			return ClientCmdType.TASK_GUILD_FINISH;
		}
		if(type == GameConstants.BOSS_DAY){
			return ClientCmdType.TASK_BOSS_FINISH;
		}
		return null;
	}
	
	public Short getCmdKillByType(int type){
		if(type == GameConstants.TASK_DAY){
			return ClientCmdType.TASK_DAY_KILL;
		}
		if(type == GameConstants.GUILD_DAY){
			return ClientCmdType.TASK_GUILD_KILL;
		}
		if(type == GameConstants.BOSS_DAY){
			return ClientCmdType.TASK_BOSS_KILL;
		}
		return null;
	}
	 
	//获得完成的信息
	public Object[] getFinishInfo(Long userRoleId,int oldTaskId,TaskDay taskDay) {
		 
		if(taskDay == null){
			return AppErrorCode.TASK_DAY_ERROR;
		}
		
		RiChangRenWuConfig  rcrwConfig = riChangRenWuConfigExportService.loadById(oldTaskId);
		if(rcrwConfig == null){
			ChuanQiLog.error("日常任务配置错误,taskId:"+oldTaskId);
			return AppErrorCode.TD_CONFIG_ERROR;
		}
		
		int exp = rcrwConfig.getJiangexp();
		int money= rcrwConfig.getJiangmoney();
		int zhen= rcrwConfig.getJiangzhen();
		int gx = rcrwConfig.getJianggongxian(); 
		
		Map<String,Integer> totalItems = null;
		
		if(rcrwConfig.getAwards() == null){
			totalItems = new HashMap<String, Integer>();
		}else{
			totalItems = new HashMap<String, Integer>(rcrwConfig.getAwards());
		}
		
		 
		 
		//添加总奖励
		if(taskDay.getTimes() > taskDay.getMaxTimes()){
			Integer loopId = taskDay.getLoopId();
			RiChangJiangLiConfig  config = riChangJiangLiConfigExportService.loadById(loopId);
			if(config!=null){
				Map<String, Integer> totalAwards =	config.getAwards();
				if(totalAwards != null) {
					ObjectUtil.mapAdd(totalItems, totalAwards);
				}
			}
			
			exp += config.getJiangexp();
			money += config.getJiangmoney();
			zhen += config.getJiangzhen();
			gx += config.getJianggongxian();
			
			//修改任务状态为全部完成
			taskDay.setStatus(GameConstants.TASK_STATUS_YES);
			taskDayDao.cacheUpdate(taskDay, userRoleId);
		}
		
		if(totalItems !=null && totalItems.size() >0){
			Object[] checkFlag = roleBagExportService.checkPutInBag(totalItems, userRoleId);
			if(checkFlag != null){
				String title = EmailUtil.getCodeEmail(GameConstants.DAY_TASK_EMAIL_TITLE);
				String content = EmailUtil.getCodeEmail(getCode(taskDay.getTaskDayType()));
				String[] attachments = EmailUtil.getAttachments(totalItems);
				for (String attachment : attachments) {
					emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
				}
			}else{
				roleBagExportService.putInBag(totalItems, userRoleId, GoodsSource.TASK_DAY, true);
			}
		}
		
		if(exp >0 ){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, exp, userRoleId, LogPrintHandle.GET_TASKDAY_FINISH, LogPrintHandle.GBZ_TASKDAY_FINISH);
		}
		
		if(money >0 ){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_TASKDAY_FINISH, LogPrintHandle.GBZ_TASKDAY_FINISH);
		}
		
		if(zhen >0 ){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.ZHENQI, zhen, userRoleId, LogPrintHandle.GET_TASKDAY_FINISH, LogPrintHandle.GBZ_TASKDAY_FINISH);
		}
		//贡献
		
		if(gx >0 ){
			addGuildGX(userRoleId, gx);  
		}
		
		Object[] awardsResult =	coverList(totalItems);
		
		Object[] result = null;
		
		//if(taskDay.getTimes() <= taskDay.getMaxTimes() && taskDay.getKillCount() < taskDay.getNeedKillCount()){
		if(taskDay.getTimes() > taskDay.getMaxTimes() && taskDay.getStatus() == GameConstants.TASK_STATUS_YES){
			result = new Object[]{1,exp,money,zhen,taskDay.getTimes(),awardsResult};
		}else{
			Object[] taskVo = getTaskVo(userRoleId, taskDay);
			result = new Object[]{1,exp,money,zhen,taskDay.getTimes(),awardsResult,taskVo};
		}
		
		return result;
	}
	
	private String getCode(int type){
		if(type == GameConstants.TASK_DAY){
			return GameConstants.DAY_TASK_AWARD;
		}
		if(type == GameConstants.GUILD_DAY){
			return GameConstants.DAY_GUILD_AWARD;
		}
		if(type == GameConstants.BOSS_DAY){
			return GameConstants.DAY_BOSS_AWARD;
		}
		ChuanQiLog.error("目前只支持普通日常任务与公会日常任务！");
		return null;
	}
	
	private void addGuildGX(long userRoleId, int gx) {
		guildExportService.addGongxian(userRoleId, gx);
	}
	
	
	/**
	 * 正在做到快天时，算成
	 * @param taskDay
	 */
	public void finishTaskDay(TaskDay taskDay){
			long userRoleId = taskDay.getUserRoleId();
			//如果跨天
			if(taskDay.getLastOptTime() < DatetimeUtil.getDate00Time()) {
				taskDay.setTimes(1);
				taskDay.setStatus(GameConstants.TASK_STATUS_NO);
				int level = getRoleLevel(userRoleId);
				Integer loopId = riChangJiangLiConfigExportService.getConfigIdByLevel(level,taskDay.getTaskDayType());
				if(loopId == null){
					return;
				}
				taskDay.setLoopId(loopId);
				RiChangJiangLiConfig  richangejlConfig = riChangJiangLiConfigExportService.loadById(loopId);
				taskDay.setMaxTimes(richangejlConfig.getMaxcount());
			}else{
				taskDay.setTimes(taskDay.getTimes()+1);
			}
			
			//次数是否已满
			taskDay.setKillCount(0); 
			if(taskDay.getTimes() <= taskDay.getMaxTimes()){
				
				Integer level = getRoleLevel(userRoleId);
				if(level == null) {
					return;	
				}
				
				RiChangRenWuConfig riChangRenWuConfig = riChangRenWuConfigExportService.randomConfig(level, taskDay.getTaskDayType());
				if(riChangRenWuConfig == null) {
					return;
				}
				
				taskDay.setTaskId(riChangRenWuConfig.getId());
				taskDay.setMonsterId(riChangRenWuConfig.getMonster());
				taskDay.setNeedKillCount(riChangRenWuConfig.getNum());
			} 
			taskDay.setLastOptTime(GameSystemTime.getSystemMillTime());
			//活跃度  lxn
			setHuoyuedu(userRoleId,taskDay.getTaskDayType(),1);
	}
	/**
	 *活跃度  lxn
	 */
	private void  setHuoyuedu(Long userRoleId,int type,int num){
		if(type == GameConstants.TASK_DAY){
			huoYueDuExportService.completeActivity(userRoleId,ActivityEnum.A14,num);
		}else if(type == GameConstants.GUILD_DAY){
			huoYueDuExportService.completeActivity(userRoleId,ActivityEnum.A15,num);
		}
		
	}
	/**
	 * 添加每日任务监听器,上线或者前端请求时加入时,添加监听器
	 */
	public void addTaskDayListener(long userRoleId,TaskDay task){ 
		
		if(task == null){ return; }
		
		if(task.isAddListener()){
			return;
		}
		
		if(task.getTimes() > task.getMaxTimes()){ return;}
		
		if(task.getTaskDayType() == GameConstants.GUILD_DAY && !isGuild(userRoleId))  return ;
		
		//这里都是杀怪任务
		task.setTaskService(this);
		task.setAddListener(true);
		KillTaskManager.getManager().addTask(userRoleId, task);
	}
	
	public boolean isGuild(long userRoleId){
		GuildMember guild = GuildManager.getManager().getGuildMember(userRoleId);
		if(guild == null){
			return false;
		}
		return true;
	}
 
 
	/**
	 * 日常任务信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] taskDayInfo(Long userRoleId,int type) {
		TaskDay taskDay  = getTaskDay(userRoleId,type);
		if(taskDay == null){
			return new Object[]{2,AppErrorCode.TASK_DAY_NO};
		}
		
		checkCrossDay(taskDay);
		
		addTaskDayListener(userRoleId, taskDay);
		
		//if(taskDay.getTimes() >= taskDay.getMaxTimes() && taskDay.getKillCount() >= taskDay.getNeedKillCount()){
		if(taskDay.getTimes() > taskDay.getMaxTimes() && taskDay.getStatus() == GameConstants.TASK_STATUS_YES){
			return new Object[]{0};
		}
		RiChangRenWuConfig rcrwConfig = riChangRenWuConfigExportService.loadById(taskDay.getTaskId());
		if(rcrwConfig == null){
			ChuanQiLog.error("taskDayInfo RiChangRenWuConfig is not exits,configId="+taskDay.getTaskId());
			return new Object[]{2,AppErrorCode.TASK_DAY_NO};
		}
		
		return new Object[]{1,getTaskVo(userRoleId, taskDay),taskDay.getTimes(),taskDay.getMaxTimes(),rcrwConfig.getFinish(),taskDay.getLoopId()};
	}
	
	private Object[] getTaskVo(long userRoleId,TaskDay taskDay){
		Integer taskId = taskDay.getTaskId();
		RiChangRenWuConfig rcrwConfig = riChangRenWuConfigExportService.loadById(taskId);
		if(rcrwConfig == null){
			ChuanQiLog.error("RiChangRenWuConfig configId is not exits,id="+taskId);
			return null;
		}
		Map<String, Integer> awards = rcrwConfig.getAwards();
		Object[] awardsResult = null;
		if(awards != null){
			awardsResult = coverList(awards);
		} 
		
		List<Object> result = new ArrayList<>();
		
		result.add(taskDay.getTaskId());
		result.add(awardsResult);
		result.add(taskDay.getKillCount());
		result.add(taskDay.getNeedKillCount());
		result.add(rcrwConfig.getMap());
		result.add(taskDay.getMonsterId());
		result.add(rcrwConfig.getJiangexp());
		result.add(rcrwConfig.getJiangmoney());
		result.add(rcrwConfig.getJiangzhen()); 
		result.add(rcrwConfig.isFly()==true?1:0);
		result.add(rcrwConfig.getZuoBiao());
		
		if(rcrwConfig.getJianggongxian().intValue() != 0){ 
			result.add(rcrwConfig.getJianggongxian()); 
		} 
		return result.toArray();
	}
	
	private Object[] coverList(Map<String, Integer> awards){
		Object[] awardsResult = new Object[awards.size()];
		int i =0;
		for (Entry<String, Integer> element : awards.entrySet()) {
			awardsResult[i++] =	new Object[]{element.getKey(),element.getValue()}; 
		}
		return awardsResult;
	}

	public Object[] taskDayFinish(Long userRoleId,int type) {
		
		TaskDay taskDay =	getTaskDay(userRoleId,type);
		if(taskDay == null){
			return AppErrorCode.TASK_DAY_ERROR;
		}
		
		//判断次数是否已满
		//if(taskDay.getTimes() >= taskDay.getMaxTimes() && taskDay.getKillCount() >= taskDay.getNeedKillCount()){
		if(taskDay.getTimes() > taskDay.getMaxTimes() && taskDay.getStatus() == GameConstants.TASK_STATUS_YES){
			return AppErrorCode.TASK_DAY_FULL;
		}
		
		Integer oldTaskId = taskDay.getTaskId();
		RiChangRenWuConfig  rcrwConfig = riChangRenWuConfigExportService.loadById(oldTaskId);
		if(rcrwConfig == null){
			ChuanQiLog.error("日常任务配置错误,taskId:"+oldTaskId);
			return AppErrorCode.TD_CONFIG_ERROR;
		}
		
		int needGold = rcrwConfig.getFinish();
		Object[] isEnought = roleBagExportService.isEnought(GoodsCategory.GOLD, needGold, userRoleId);
		if(isEnought != null){
			return isEnought;
		}
		
		//扣除元宝
		roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_TASKDAY_FINISH, true,LogPrintHandle.CBZ_TASKDAY_FINISH);
		//腾讯OSS消费上报
		if(PlatformConstants.isQQ()){
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,needGold,LogPrintHandle.CONSUME_TASKDAY_FINISH,QQXiaoFeiType.CONSUME_TASKDAY_FINISH,1});
		}
		refreshTashDay(taskDay,type);
		 
		Object[] finishInfo = getFinishInfo(userRoleId, oldTaskId, taskDay);

		return finishInfo;
	}
	
	/**
	 * 使用元宝完成当前的任务,刷新任务信息
	 * @param taskDay
	 */
	public void refreshTashDay(TaskDay taskDay,int type){
		long userRoleId =taskDay.getUserRoleId();
		
		taskDay.setTimes(taskDay.getTimes()+1);
		taskDay.setLastOptTime(GameSystemTime.getSystemMillTime());
		
		Integer level = getRoleLevel(userRoleId);
		if(level == null) return ;
		
		RiChangRenWuConfig riChangRenWuConfig  = getTaskDayConfig(level,type);
		if(riChangRenWuConfig == null) return ;
		
		taskDay.setMonsterId(riChangRenWuConfig.getMonster());
		taskDay.setNeedKillCount(riChangRenWuConfig.getNum()); 
		
		taskDayDao.cacheUpdate(taskDay, userRoleId);
	}

	/**
	 * 完成所有剩下的任务
	 * @param userRoleId
	 * @return
	 */
	public Object[] taskDayFinishAll(Long userRoleId,int type) {
		TaskDay taskDay = getTaskDay(userRoleId,type);
		if(taskDay == null){
			return AppErrorCode.TASK_NOT_EXIST;
		}
		
		int noFinishCount = taskDay.getMaxTimes() - taskDay.getTimes()+1;
		
		//if(noFinishCount <= 1 && taskDay.getKillCount() >= taskDay.getNeedKillCount()) {
		if(noFinishCount <= 1 && taskDay.getStatus() == GameConstants.TASK_STATUS_YES) {
		   return AppErrorCode.TASK_DAY_FULL;
		}
		
		//计算价格
		RiChangRenWuConfig  config = riChangRenWuConfigExportService.loadById(taskDay.getTaskId());
		if(config == null){
			return AppErrorCode.TD_CONFIG_ERROR;
		}
		int needGold = config.getFinish();
		
		int needMoney = noFinishCount * needGold;
		
		Object[] errorCode = roleBagExportService.isEnought(GoodsCategory.GOLD, needMoney, userRoleId);
		if(errorCode != null){
			return errorCode;
		}
		
		//扣除元宝
		roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, needMoney, userRoleId, LogPrintHandle.CONSUME_TASKDAY_FINISH, true,LogPrintHandle.CBZ_TASKDAY_FINISH);
		//腾讯OSS消费上报
		if(PlatformConstants.isQQ()){
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,needMoney,LogPrintHandle.CONSUME_TASKDAY_FINISH,QQXiaoFeiType.CONSUME_TASKDAY_FINISH,1});
		}
		int exp = config.getJiangexp();
		int money= config.getJiangmoney();
		int zhen= config.getJiangzhen();
		int gongxian =config.getJianggongxian();
		
	 
		Map<String,Integer> totalItems = null;
		
		if(config.getAwards() == null){
			totalItems = new HashMap<String, Integer>();
		}else{
			totalItems = new HashMap<String, Integer>(config.getAwards());
		}
		
		 
		int level = getRoleLevel(userRoleId);
		
		//奖励
		for (int i = 1; i < noFinishCount; i++) {
			RiChangRenWuConfig nconfig = riChangRenWuConfigExportService.randomConfig(level, type);
			if(nconfig == null){
				ChuanQiLog.error("获取日常任务配置错误,level:"+level);
				continue;
			}
			exp += nconfig.getJiangexp();
			money += nconfig.getJiangmoney();
			zhen += nconfig.getJiangzhen();
			gongxian += nconfig.getJianggongxian();
			Map<String,Integer> nawards = nconfig.getAwards();
			if(nawards != null && nawards.size() >0 ){
				ObjectUtil.mapAdd(totalItems, nawards);
			} 
		}
		
		 
		//添加总奖励
//		if(taskDay.getTimes() >= taskDay.getMaxTimes()){
			RiChangJiangLiConfig  totalConfig = riChangJiangLiConfigExportService.loadById(taskDay.getLoopId());
			if(totalConfig!=null){
				Map<String, Integer> totalAwards =	totalConfig.getAwards();
				if(totalAwards != null){
					ObjectUtil.mapAdd(totalItems, totalAwards);
				}
			}
			
			exp += totalConfig.getJiangexp();
			money += totalConfig.getJiangmoney();
			zhen += totalConfig.getJiangzhen();
			gongxian += totalConfig.getJianggongxian();
//		}
		
		
		
		if(exp >0 ){
		roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, exp, userRoleId, LogPrintHandle.GET_TASKDAY_FINISH, LogPrintHandle.GBZ_TASKDAY_FINISH);
		} 
		if(money >0 ){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.GET_TASKDAY_FINISH, LogPrintHandle.GBZ_TASKDAY_FINISH);
		} 
		if(zhen >0 ){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.ZHENQI, zhen, userRoleId, LogPrintHandle.GET_TASKDAY_FINISH, LogPrintHandle.GBZ_TASKDAY_FINISH);
		}
		//添加贡献
		if(gongxian>0){
			addGuildGX(userRoleId, gongxian);
		}
		
		if(totalItems != null && totalItems.size() != 0){
			Object[] checkFlag = roleBagExportService.checkPutInBag(totalItems, userRoleId);
			if(checkFlag != null){
				String title = EmailUtil.getCodeEmail(GameConstants.DAY_TASK_EMAIL_TITLE);
				String content = EmailUtil.getCodeEmail(getCode(type));
				String[] attachments = EmailUtil.getAttachments(totalItems);
				for (String attachment : attachments) {
					emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
				}
			}else{
				roleBagExportService.putInBag(totalItems, userRoleId, GoodsSource.TASK_DAY, true);
			}
		}
		
		//修改缓存值
		taskDay.setTimes(taskDay.getMaxTimes()+1);
		taskDay.setKillCount(0);
		taskDay.setLastOptTime(GameSystemTime.getSystemMillTime());
		taskDay.setStatus(GameConstants.TASK_STATUS_YES);//修改成今日任务已经全部完成
		taskDay.setRenwuCount(taskDay.getRenwuCount() + noFinishCount);
		taskDayDao.cacheUpdate(taskDay, userRoleId);

		//取消杀怪的监听
		KillTaskManager.getManager().cancelTask(userRoleId, taskDay);
		 
		//活跃度
		this.setHuoyuedu(userRoleId,taskDay.getTaskDayType(),noFinishCount);
		//成就
		try {
			if(taskDay.getTaskDayType() == GameConstants.TASK_DAY){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_DAYTASKCOUNT,  noFinishCount});
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.TASK_RICHANG, noFinishCount});
			}else if(taskDay.getTaskDayType() == GameConstants.GUILD_DAY){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_GUILDTASKCOUNT,  noFinishCount});
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.TASK_GUILD, noFinishCount});
			}
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		return new Object[]{1,exp,money,zhen,coverList(totalItems)};
	}

	/**
	 * 移除公会日常杀怪监听
	 * @param userRoleId
	 */
	public void removeGuildDayTask(long userRoleId) {
		List<TaskDay>  taskDays = taskDayDao.cacheLoadAll(userRoleId, new TaskDayTypeFilter(GameConstants.GUILD_DAY));
		if(taskDays != null && taskDays.size() >0){
			//清空击杀怪物
			TaskDay task = taskDays.get(0);
			task.setKillCount(0);
			task.setAddListener(false);
			taskDayDao.cacheUpdate(task, userRoleId);
			//取消杀怪的监听
			KillTaskManager.getManager().cancelTask(userRoleId, task);
		}
	}

	/**
	 * 添加公会日常杀怪监听
	 * @param userRoleId
	 */
	public void addGuildDayTask(long userRoleId) {
		
		List<TaskDay>  taskDays = taskDayDao.cacheLoadAll(userRoleId, new TaskDayTypeFilter(GameConstants.GUILD_DAY));
		if(taskDays != null && taskDays.size() >0){
			//添加监听
			TaskDay  taskDay = taskDays.get(0);
			taskDay.setKillCount(0);
			addTaskDayListener(userRoleId, taskDay);
		}
	}
	 


//	public void offlineHandle(Long userRoleId) {
//		主线任务添加下线清除了，这里不需要操作了
//		KillTaskManager.getManager().offline(userRoleId);
//		CaijiTaskManager.getManager().offline(userRoleId);
//		OtherTaskManager.getManager().offline(userRoleId);
//	}
	
	
	//TODO ----以下方法供资源找回模块使用---------
	/**
	 * 计算日常任务\公会日常资源情况
	 */
	public void calDayFubenResource(Map<String,Map<String,Integer>> dayTaskMap,Map<String,Map<String,Integer>> guildTaskMap,Long userRoleId){
		List<TaskDay> taskDays = taskDayDao.cacheLoadAll(userRoleId);
		if(taskDays != null && taskDays.size() > 0){
			for (TaskDay taskDay : taskDays) {
				if(DatetimeUtil.dayIsToday(taskDay.getLastOptTime())){
					continue;
				}
				Map<String,Map<String,Integer>> map = null;
				if(taskDay.getTaskDayType() == GameConstants.TASK_DAY){
					map = dayTaskMap;
				}else if(taskDay.getTaskDayType() == GameConstants.GUILD_DAY){
					map = guildTaskMap;
				}
				if(map == null){
					continue;
				}
				calDayFubenResource(map, taskDay,true);
			}
		}
	}
	
	private void calDayFubenResource(Map<String,Map<String,Integer>> map,TaskDay taskDay,boolean doCrossDay){
		int day = DatetimeUtil.twoDaysDiffence(taskDay.getLastOptTime());
		if(day < 2 && taskDay.getTimes() > taskDay.getMaxTimes()){
			return;
		}
		int level = getRoleLevel(taskDay.getUserRoleId());
		RiChangRenWuConfig nconfig = riChangRenWuConfigExportService.randomConfig(level, taskDay.getTaskDayType());
		if(nconfig == null){
			return;
		}
		RiChangJiangLiConfig  totalConfig = riChangJiangLiConfigExportService.loadById(taskDay.getLoopId());
		if(totalConfig == null){
			return;
		}
		if(day > GameConstants.RESOURCE_BACK_MAX_DAY){
			day = GameConstants.RESOURCE_BACK_MAX_DAY;
		}else if(taskDay.getTimes() > 0 && taskDay.getTimes() <= taskDay.getMaxTimes()){
			int count = taskDay.getMaxTimes() - taskDay.getTimes() + 1;
			int exp = nconfig.getJiangexp() * count + totalConfig.getJiangexp();
			int money = nconfig.getJiangmoney() * count + totalConfig.getJiangmoney();
			int zhenqi = nconfig.getJiangzhen() * count + totalConfig.getJiangzhen();
			Map<String,Integer> dayMap = new HashMap<>();
			dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, exp);
			dayMap.put(ModulePropIdConstant.MONEY_GOODS_ID, money);
			dayMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, zhenqi);
			map.put(day+"", new HashMap<>(dayMap));
			day--;
		}
		if(day > 0){
			int exp = nconfig.getJiangexp() * taskDay.getMaxTimes() + totalConfig.getJiangexp();
			int money = nconfig.getJiangmoney() * taskDay.getMaxTimes() + totalConfig.getJiangmoney();
			int zhenqi = nconfig.getJiangzhen() * taskDay.getMaxTimes() + totalConfig.getJiangzhen();
			Map<String,Integer> dayMap = new HashMap<>();
			dayMap.put(ModulePropIdConstant.EXP_GOODS_ID, exp);
			dayMap.put(ModulePropIdConstant.MONEY_GOODS_ID, money);
			dayMap.put(ModulePropIdConstant.MONEY_ZHENQI_ID, zhenqi);
			for (; day > 0; day--) {
				map.put(day+"", new HashMap<>(dayMap));
			}
		}
		if(doCrossDay){
			doCrossDay(taskDay);
		}
	}
	
	public void taskKuaTian(TaskDay taskDay){
		try{
			String type = null;
			if(taskDay.getTaskDayType() == GameConstants.TASK_DAY){
				type = GameConstants.RESOURCE_TYPE_DAY_TASK;
			}else if(taskDay.getTaskDayType() == GameConstants.GUILD_DAY){
				type = GameConstants.RESOURCE_TYPE_GUILD_TASK;
			}
			if(type != null){
				Map<String,Map<String,Integer>> map = new HashMap<>();
				calDayFubenResource(map, taskDay,false);
				roleResourceBackExportService.changeTypeMap(taskDay.getUserRoleId(), map, type);
			}
			doCrossDay(taskDay);
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
}
