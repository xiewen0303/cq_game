package com.junyou.stage.easyaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hehj.easyexecutor.annotation.EasyMapping;
import com.hehj.easyexecutor.annotation.EasyWorker;
import com.hehj.easyexecutor.enumeration.EasyGroup;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.module.GameModType;
import com.junyou.stage.service.TeamService;
import com.junyou.stage.tunnel.StageMsgQueue;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.number.LongUtils;
import com.kernel.pool.executor.Message;

@Controller
@EasyWorker(moduleName = GameModType.TEAM_MODULE,groupName = EasyGroup.STAGE)
public class TeamAction {
	@Autowired
	private TeamService teamService;
	/**
	 * 创建队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.CREATE_TEAM)
	public void createTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = teamService.createTeam(userRoleId, stageId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.CREATE_TEAM, result);
	}
	/**
	 * 附近玩家
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_ARROUND_PLAYER)
	public void arroundPlayer(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = teamService.getArroundPlayer(userRoleId, stageId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_ARROUND_PLAYER, result);
	}
	/**
	 * 邀请玩家
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_YAOQING_PLAYER)
	public void yaoqingPlayer(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targetRoleId = LongUtils.obj2long(inMsg.getData());
		StageMsgQueue stageMsgQueue = new StageMsgQueue();
		Object[] result = teamService.yaoqing(userRoleId, stageId, targetRoleId, stageMsgQueue);
		StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_YAOQING_PLAYER, result);
		stageMsgQueue.flush();
	}
	/**
	 * 设置自动接收邀请
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_AUTO_AGREE_YAOQING)
	public void setAutoYaoqing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Boolean auto = inMsg.getData();
		teamService.setAutoAgree(userRoleId, stageId, GameConstants.TEAM_AUTO_YAOQING, auto);
	}
	/**
	 * 设置自动接收申请
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_AUTO_AGREE_APPLY)
	public void setAutoApply(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Boolean auto = inMsg.getData();
		teamService.setAutoAgree(userRoleId, stageId, GameConstants.TEAM_AUTO_APPLY, auto);
	}
	/**
	 * 同意邀请
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_AGREE_YAPQING)
	public void agreeYaoqing(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targetRoleId = LongUtils.obj2long(inMsg.getData());
		Object[] result = teamService.agreeYaoqing(userRoleId, stageId, targetRoleId);
		if(result != null){
			StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_AGREE_YAPQING, result);
		}
	}
	/**
	 * 我的队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_MY_TEAM_INFO)
	public void myTeamInfo(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = teamService.myTeamInfo(userRoleId, stageId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_MY_TEAM_INFO, result);
	}
	/**
	 * 离开队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_LEAVE_TEAM)
	public void leaveTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object targetRoleId = inMsg.getData();
		Object[] result = teamService.leaveTeam(userRoleId, stageId, targetRoleId);
		if(result != null){
			StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_LEAVE_TEAM, result);
		}
	}
	/**
	 * 附近队伍
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_ARROUND_TEAM)
	public void arroundTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Object[] result = teamService.getArroundTeam(userRoleId, stageId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_ARROUND_TEAM, result);
	}
	/**
	 * 转让队长
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_CHANGE_LEADER)
	public void changeLeader(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targetRoleId = LongUtils.obj2long(inMsg.getData());
		Object[] result = teamService.changeLeader(userRoleId, stageId, targetRoleId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_CHANGE_LEADER, result);
	}
	/**
	 *申请入队
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_APPLY_TEAM)
	public void applyTeam(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targetRoleId = LongUtils.obj2long(inMsg.getData());
		Object[] result = teamService.applyTeam(userRoleId, stageId, targetRoleId);
		if(result != null){
			StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_APPLY_TEAM, result);
		}
	}
	/**
	 * 同意组队
	 * @param inMsg
	 */
	@EasyMapping(mapping = ClientCmdType.TEAM_LEADER_AGREE)
	public void agreeApply(Message inMsg){
		Long userRoleId = inMsg.getRoleId();
		String stageId = inMsg.getStageId();
		Long targetRoleId = LongUtils.obj2long(inMsg.getData());
		Object[] result = teamService.agreeApply(userRoleId, stageId, targetRoleId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.TEAM_LEADER_AGREE, result);
	}
}
