package com.junyou.bus.task.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.task.service.TaskService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 主线任务Action
 * @author LiuYu
 * @date 2014-12-24 上午10:31:31
 */
@Controller
@EasyWorker(moduleName = GameModType.TASK_MODULE)
public class TaskAction {

	@Autowired
	private TaskService taskService;
	
	
	/**
	 * 任务更新
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.KILL_MONSTOR)
	public void conditionChange(Message inMsg){
		Object[] data = inMsg.getData();
		String conditionId = (String) data[0];
		int type = (Integer) data[1];
		Object[] roleIds = (Object[])data[2];
		
		BusMsgQueue busMsgQueue = new BusMsgQueue();
		taskService.taskChanageCondition(roleIds, conditionId, type,busMsgQueue);
		busMsgQueue.flush();
	}
	
	/**
	 * 更新任务状态
	 * @param context
	 */
	@EasyMapping(mapping = ClientCmdType.REFRESH_TASK_STATE)
	public void getRunTask(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = taskService.getTaskRunState(userRoleId);
		if(result != null){
			BusMsgSender.send2One(userRoleId,ClientCmdType.REFRESH_TASK_STATE,result);
		}
	}
	
	/**
	 * 完成任务
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.FINISH_AND_RECIVE_TASK)
	public void completeTask(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = taskService.completeTask(userRoleId);
		
		BusMsgSender.send2One(userRoleId,ClientCmdType.FINISH_AND_RECIVE_TASK,result);
	}
	
}
