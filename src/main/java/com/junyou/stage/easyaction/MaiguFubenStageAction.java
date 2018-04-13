package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.MaiguFubenStageService;
import com.kernel.pool.executor.Message;

/**
 * 多人副本场景
 * @author chenjianye
 * @date 2015-04-29
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE)
public class MaiguFubenStageAction {
	@Autowired
	private MaiguFubenStageService maiguFubenStageService;
	
	@EasyMapping(mapping = InnerCmdType.MAIGU_SELF_LEAVE_FUBEN)
	public void selfLeaveFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		maiguFubenStageService.selfLeaveFuben(stageId,userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.MAIGU_FUBEN_FINISH)
	public void challengeOver(Message inMsg){
		String stageId = inMsg.getStageId();
		Integer result = inMsg.getData();
		maiguFubenStageService.challengeOver(stageId,result);
	}
	
	
//	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_SHANGHAI_CONSOLE)
//	public void moreFubenShanghaiConsole(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId();
//		Object[] obj = moreFubenStageService.getMoreFubenShanghaiConsole(stageId);
//		StageMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_SHANGHAI_CONSOLE, obj);
//	}
}
