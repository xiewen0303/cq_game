package com.junyou.bus.branchtask.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 
/**
 * 支线任务
 * @author  作者：wind
 * @version 创建时间：2017-9-4 上午11:25:53
 */
@Controller
@EasyWorker(moduleName = GameModType.TASK_BRANCH_MODULE)
public class TaskBranchAction {
	
	@Autowired
	private TaskBranchService taskBranchService;
	
	/**
	 * 获取日常任务信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.RECIVE_TASK)
	public void reciveTask(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] taskIds = (Object[])inMsg.getData();
		
		Object[] result = taskBranchService.reciveTask(userRoleId,taskIds);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RECIVE_TASK, result);
	}
	
	/**
	 * 统计支线完成度
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_BRANCH_COUNT)
	public void branchCount(Message inMsg){
		 Object[] data  = (Object[])inMsg.getData();
		 long roleId = inMsg.getRoleId();
		 BranchEnum  acEnum = (BranchEnum)data[0];
		 Object params = data[1];
		 
		 taskBranchService.completeBranch(roleId, acEnum,params);
	}
	
	/**
	 * 激活支线
	 */
	@EasyMapping(mapping = InnerCmdType.INNER_BRANCH_ACTIVITY)
	public void branchActivity(Message inMsg){
		 Object[] data  = (Object[])inMsg.getData();
		 long userRoleId = inMsg.getRoleId();
		 int  openType = (int)data[0];
		 Object params = data[1];
		 
		 taskBranchService.branchActivity(userRoleId,openType,params);
	}
}