package com.junyou.bus.daytask.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.daytask.service.TaskDayService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 日常任务
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-3-17 上午10:31:14
 */
@Controller
@EasyWorker(moduleName = GameModType.TASK_DAY_MODULE)
public class TaskDayAction {
	
	@Autowired
	private TaskDayService taskDayService;
	
	/**
	 * 获取日常任务信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_DAY_INFO)
	public void taskDayInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayInfo(userRoleId,GameConstants.TASK_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_DAY_INFO, result);
		 
	} 
	
	/**
	 * 请求完成任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_DAY_FINISH)
	public void taskDayFinish(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayFinish(userRoleId,GameConstants.TASK_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_DAY_FINISH, result);
	}
	
	/**
	 * 请求完成所有剩余任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_DAY_FINISH_ALL)
	public void taskDayFinishAll(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayFinishAll(userRoleId,GameConstants.TASK_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_DAY_FINISH_ALL, result);
	}
	
	/**
	 * 请求公会日常杀怪任务任务状态信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_GUILD_INFO)
	public void taskGuildInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayInfo(userRoleId,GameConstants.GUILD_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_GUILD_INFO, result);
		 
	} 
	
	/**
	 * 请求完成任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_GUILD_FINISH)
	public void taskGuildFinish(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayFinish(userRoleId,GameConstants.GUILD_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_GUILD_FINISH, result);
		
	}
	
	/**
	 * 请求完成所有剩余任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_GUILD_FINISH_ALL)
	public void taskGuildFinishAll(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayFinishAll(userRoleId,GameConstants.GUILD_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_GUILD_FINISH_ALL, result);
	}
	
	
	
	/**
	 * 获取BOSS日常任务信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_BOSS_INFO)
	public void taskBOSSInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayInfo(userRoleId,GameConstants.BOSS_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_BOSS_INFO, result);
		 
	} 
	
	/**
	 * 请求完成BOSS任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_BOSS_FINISH)
	public void taskBOSSFinish(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayFinish(userRoleId,GameConstants.BOSS_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_BOSS_FINISH, result);
	}
	
	/**
	 * 请求完成所有剩余BOSS任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TASK_BOSS_FINISH_ALL)
	public void taskBOSSFinishAll(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		 
		Object[] result = taskDayService.taskDayFinishAll(userRoleId,GameConstants.BOSS_DAY);
		
		BusMsgSender.send2One(userRoleId, ClientCmdType.TASK_BOSS_FINISH_ALL, result);
	}
}