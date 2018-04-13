package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.MoreFubenStageService;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 多人副本场景
 * @author chenjianye
 * @date 2015-04-29
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE)
public class MoreFubenStageAction {
	@Autowired
	private MoreFubenStageService moreFubenStageService;
	
	@EasyMapping(mapping = InnerCmdType.S_EXIT_MORE_FUBEN)
	public void exitFuben(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		moreFubenStageService.exitFuben(stageId,userRoleId);
	}
	
	@EasyMapping(mapping = InnerCmdType.S_MORE_OVER)
	public void challengeOver(Message inMsg){
		String stageId = inMsg.getStageId();
		moreFubenStageService.challengeOver(stageId);
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_MORE_FUBEN_SYNC)
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_START_SYNC)
	public void startSynSourceServer(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getData();
		moreFubenStageService.startSynSourceServer(userRoleId,stageId);
	}
	
	/**
	 * 同步后发现用户已经不再跨服，需要将用户从跨服场景中删除
	 * @param inMsg
	 */
	@EasyMapping(mapping = InnerCmdType.MORE_FUBEN_REMOVE_FROM_KUAFU,kuafuType=EasyKuafuType.KFING_S2KF_TYPE)
	public void removeRoleFromKuafu(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		moreFubenStageService.removeRoleFromKuafu(userRoleId,stageId);
	}
	
//	@EasyMapping(mapping = ClientCmdType.MORE_FUBEN_SHANGHAI_CONSOLE)
//	public void moreFubenShanghaiConsole(Message inMsg){
//		Long userRoleId = inMsg.getRoleId();
//		String stageId = inMsg.getStageId();
//		Object[] obj = moreFubenStageService.getMoreFubenShanghaiConsole(stageId);
//		StageMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_SHANGHAI_CONSOLE, obj);
//	}
}
