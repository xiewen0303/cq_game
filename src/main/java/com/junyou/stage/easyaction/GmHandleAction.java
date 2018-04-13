package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.module.GameModType;
import com.junyou.stage.service.GmService;
import com.junyou.stage.tunnel.StageMsgQueue;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.STAGE,groupName = EasyGroup.STAGE)
public class GmHandleAction {
	
	@Autowired
	private GmService gmService;
	
	/**
	 * GM隐身操作
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_YINSHEN_OR_NOT)
	public void gmYinshen(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Boolean xianshi = inMsg.getData();
		
		boolean result = gmService.gmYinshen(userRoleId, xianshi, stageId);
//		StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_YINSHEN_OR_NOT, result);
	}
	
	/**
	 * GM传送到指定地图
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_MOVE_TO_MAP)
	public void gmTeleportMap(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer mapId = inMsg.getData();
		
		Object result = gmService.gmTeleportMap(userRoleId, stageId,mapId);
		if(null != result){
//			StageMsgSender.send2StageControl(userRoleId, StageTeleportCommands.INNER_APPLY_CHANGE_MAP, result);
		}
	}
	

	/**
	 * GM传送到本地图指定坐标
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_MOVE_TO_THIS_MAP_OTHER)
	public void gmMoveToThisMap(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] point = inMsg.getData();
		
		Object result = gmService.gmTeleportMap(userRoleId, stageId,point);
		if(null != result){
//			StageMsgSender.send2StageControl(userRoleId, StageTeleportCommands.INNER_APPLY_CHANGE_MAP, result);
		}
	}
	
	/**
	 * GM模糊查询某个在线角色
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_SELECT_OTHER_ROLE)
	public void gmSelectOtherRole(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String roleName = inMsg.getData();
		
		Object result = gmService.gmSelectOtherRole(userRoleId.toString(), roleName);
		if(result != null){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_SELECT_OTHER_ROLE, result);
		}
	}
	
	/**
	 * GM传送到某个玩家身边
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_MOVE_TO_OTHER_PLAYER)
	public void gmMoveToOtherPlayer(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targerRoleId = inMsg.getData();
		
		StageMsgQueue stageMsgQueue = new StageMsgQueue();
		gmService.gmTeleportMapToOtherRole(userRoleId, stageId,targerRoleId,stageMsgQueue);
		
		stageMsgQueue.flush();
	}

	/**
	 * GM传送到某个玩家身边
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_CHANGE_TYPE)
	public void gmChangeType(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer isGm  = inMsg.getData();
		
		gmService.changeGm(userRoleId, stageId, isGm);
	}

	/**
	 * GM封号
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_FENG_OTHER_PLAYER)
	public void gmFenghao(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] msg = inMsg.getData();
		Long targerRoleId  = (Long)msg[0];
		String reasons  = (String)msg[1];
		
		Object result = gmService.gmFenghao(userRoleId, targerRoleId, reasons);
		if(result != null){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_FENG_OTHER_PLAYER, result);
		}
	}

	/**
	 * GM禁言
	 */
//	@EasyMapping(mapping = GmHandleCommands.GM_JINYAN_OTHER_ROLE)
	public void gmJinyan(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		Object[] msg = inMsg.getData();
		Long targerRoleId  = (Long)msg[0];
		String reasons  = (String)msg[1];
		
		Object result =gmService.gmJinyan(userRoleId, targerRoleId, reasons);
		if(result != null){
//			StageMsgSender.send2One(userRoleId, GmHandleCommands.GM_JINYAN_OTHER_ROLE, result);
		}
	}
}
