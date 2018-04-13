package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.BaguaFubenStageService;
import com.kernel.pool.executor.Message;

/**
 * 多人副本场景
 * @author chenjianye
 * @date 2015-04-29
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE)
public class BaguaFubenStageAction {
	@Autowired
	private BaguaFubenStageService baguaFubenStageService;
	
	@EasyMapping(mapping = InnerCmdType.BAGUA_SELF_LEAVE_FUBEN)
	public void selfLeaveFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		baguaFubenStageService.selfLeaveFuben(stageId,userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.BAGUA_FUBEN_FINISH)
	public void challengeOver(Message inMsg){
		String stageId = inMsg.getStageId();
		baguaFubenStageService.challengeOver(stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.BAGUA_ENTER_DOOR_KF)
	public void baguaFubenEnterDoor(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer position = inMsg.getData();
		baguaFubenStageService.enterDoor(stageId, userRoleId,position);
	}
	
//	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_SHANGHAI_CONSOLE)
//	public void moreFubenShanghaiConsole(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId();
//		Object[] obj = moreFubenStageService.getMoreFubenShanghaiConsole(stageId);
//		StageMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_SHANGHAI_CONSOLE, obj);
//	}
}
