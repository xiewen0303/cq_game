package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.module.GameModType;
import com.junyou.stage.service.TeleportService;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.STAGE, groupName = EasyGroup.STAGE)
public class TeleportAction{
	@Autowired
	private TeleportService teleportService;
	/**
	 * 申请传送点传送
	 * @param waypointid
	 */
	@EasyMapping(mapping = ClientCmdType.APPLY_TELEPORT_POINT)
	public void applyWaypointTeleport(Message inMsg){
		
		Object inputData = inMsg.getData();
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		String waypointid = (String)inputData;
		
		Object result = teleportService.applyWaypointTeleport(stageId,roleId,waypointid);
		if(null != result){
			StageMsgSender.send2StageControl(roleId, InnerCmdType.S_APPLY_CHANGE_STAGE, result);
		}
	}
	
	/**
	 * 剧情传送
	 * @param waypointid
	 */
	@EasyMapping(mapping = ClientCmdType.APPLY_TELEPORT_PLOT_STAGE)
	public void applyPlotWaypointTeleport(Message inMsg){
		
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();

		Integer id = (Integer)inMsg.getData();
		
		Object applyEnterData = teleportService.applyPlotWaypointTeleport(stageId,roleId,id);
		if(applyEnterData != null){
			BusMsgSender.send2BusInner(roleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);	
		}
	}
	
	/**
	 * 获取分线信息
	 */
	@EasyMapping(mapping = ClientCmdType.APPLY_LINE_INFO)
	public void applyLineInfo(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = teleportService.applyLineInfo(stageId);
		if(null != result){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_LINE_INFO, result);
		}
	}
	/**
	 * 申请换线
	 */
	@EasyMapping(mapping = ClientCmdType.APPLY_CHANGE_LINE)
	public void applyChangeLine(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Integer line = inMsg.getData();
		Object[] result = teleportService.applyChangeLine(roleId, stageId, line);
		if(null != result){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_CHANGE_LINE, result);
		}
	}
	
	/**
	 * 申请飞鞋传送
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.APPLY_FLY_SHOES)
	public void applyFlyShoesTeleport(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		String flyShoesId = inMsg.getData();
		Object result = teleportService.applyFlyShoesTeleport(stageId, roleId, flyShoesId);
		if(null != result){
			StageMsgSender.send2StageControl(roleId, InnerCmdType.S_APPLY_CHANGE_STAGE, result);
		}
	}
	/**
	 * 申请使用回城卷轴
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.APPLY_TOWN_PORTAL)
	public void applyTownPortalTeleport(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Long guid = LongUtils.obj2long(inMsg.getData());
		Object result = teleportService.applyTownPortalTeleport(stageId, roleId, guid);
		if(null != result){
			StageMsgSender.send2StageControl(roleId, InnerCmdType.S_APPLY_CHANGE_STAGE, result);
		}
	}
	
	/**
	 * 申请每日活动地图传送
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.APPLY_GOTO_ACTIVE_MAP)
	public void applyGotoActiveMap(Message inMsg){
		Long roleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		Integer activeId = inMsg.getData();
		Object result = teleportService.applyGotoActiveMap(roleId, stageId, activeId);
		if(null != result){
			StageMsgSender.send2One(roleId, ClientCmdType.APPLY_GOTO_ACTIVE_MAP, result);
		}
		
	}
	
	
	/**
	 * 飞到镖车
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.FLY_BIAOCHE)
	public void flyToBiaoChe(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		
		teleportService.flyToBiaoChe(stageId, userRoleId);
	}
}