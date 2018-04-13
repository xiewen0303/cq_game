package com.junyou.stage.campwar.easyaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.campwar.service.CampWarStageService;
import com.junyou.stage.tunnel.StageMsgQueue;
import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.pool.executor.Message;
import com.kernel.token.annotation.TokenCheck;

/**
 * 阵营战场景Action
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-8 下午2:58:04 
 */
@Controller
@EasyWorker(moduleName = GameModType.STAGE,groupName = EasyGroup.STAGE)
public class CampWarStageAction {
	@Autowired
	private CampWarStageService campWarStageService;

	@EasyMapping(mapping = InnerCmdType.S_CAMP_WAR_START)
	public void campWarStart(Message inMsg){
		Object[] data = inMsg.getData();
		Integer hdId = (Integer) data[0];
		String stageId = (String) data[1];
		campWarStageService.campWarStart(stageId, hdId);
	}
	/**
	 * 强制将玩家剔出阵营战副本，并且清除所有此次阵营战的数据
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_CAMP_CHECK)
	@EasyMapping(mapping = InnerCmdType.S_LEVEL_CAMP)
	public void sLevelCampWar(Message inMsg){
		String stageId = inMsg.getStageId();
		StageMsgQueue stageMsgQueue = new StageMsgQueue();
		
		campWarStageService.campWarEnd(stageMsgQueue, stageId);
		stageMsgQueue.flush();
	}
	
	@TokenCheck(component = GameConstants.COMPONENT_CAMP_ADD_EXP)
	@EasyMapping(mapping = InnerCmdType.S_CAMP_ADD_EXP)
	public void sCampAddExp(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		campWarStageService.campAddExp(userRoleId, stageId);
	}
	
	@EasyMapping(mapping = InnerCmdType.S_CAMP_DEAD)
	public void sCampDead(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		Object[] data = inMsg.getData();
		Integer constants = (Integer) data[0];
		Long deadRoleId = null;
		if(data.length > 1){
			deadRoleId = (Long) data[1];
		}
		campWarStageService.campDead(stageId, userRoleId, constants,deadRoleId);
	}
	/**
	 * 请求排名信息
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.GET_RANK)
	public void getCampRank(Message inMsg){
		String stageId = inMsg.getStageId();
		Long userRoleId = inMsg.getRoleId();
		
		Object[] result = campWarStageService.getCampRank(stageId, userRoleId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.GET_RANK, result);
	}
	/**
	 * 阵营战结束
	 * @param inMsg
	 */
	@TokenCheck(component = GameConstants.COMPONENT_CAMP_RANK)
	@EasyMapping(mapping = InnerCmdType.S_CAMP_RANK)
	public void sCampRank(Message inMsg){
		String stageId = inMsg.getStageId();
		
		campWarStageService.campRank(stageId);
	}
}