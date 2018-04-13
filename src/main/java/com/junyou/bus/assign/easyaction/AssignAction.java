package com.junyou.bus.assign.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.bus.assign.service.AssignService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.module.GameModType;
import com.kernel.pool.executor.Message;
 

/**
 * 签到action 
 * @author jy
 *
 */
@Component
@EasyWorker(moduleName = GameModType.ASSIGN)
public class AssignAction {
	
	@Autowired
	private AssignService assignService;
	
	/**
	 * 签到初始化
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ASSIGN_INIT)
	public void assignInit(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = assignService.getAssignInfo(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ASSIGN_INIT, obj);
	}
	
	
	/**
	 * 签到
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ASSIGN)
	public void assign(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = assignService.assign(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ASSIGN, obj);
	}
	
	/**
	 * 具体类型领奖（累计签到天数）
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ASSIGN_TOTAL)
	public void assignTotal(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Integer rewardType = inMsg.getData();
		Object[] obj = assignService.assignTotal(userRoleId, rewardType);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ASSIGN_TOTAL, obj);
	}
	
	/**
	 * 一键补签
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.ASSIGN_RETROACTIVE)
	public void assignRetroactive(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] obj = assignService.assignRetroactive(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ASSIGN_RETROACTIVE, obj);
	}
	
	/**
	 * 福利大厅有无可领奖励
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.FLDT_HAVE_JL)
	public void getFuLiDaTinStateValue(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		
		Object result = assignService.getFuLiDaTinStateValue(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.FLDT_HAVE_JL, result);
	}
	
}
