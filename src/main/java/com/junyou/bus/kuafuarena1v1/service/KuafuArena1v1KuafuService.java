package com.junyou.bus.kuafuarena1v1.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuarena1v1.constants.KuafuArena1v1Constants;
import com.junyou.bus.kuafuarena1v1.util.KuafuMatch;
import com.junyou.bus.kuafuarena1v1.util.KuafuMatchManager;
import com.junyou.bus.kuafuarena1v1.util.KuafuMatchMember;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuArena1v1PublicConfig;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.kuafu.KuafuArenaFbStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuArena1v1KuafuService {

	@Autowired
	private KuafuMatchManager kuafuMatchManager;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;

	public KuafuArena1v1PublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_KUAFU_ARENA_1v1);
	}

	public void recieveRoleData(Object[] data) {
		Object[] roleData = (Object[]) data[0];
		Long userRoleId = LongUtils.obj2long(roleData[1]);
		String userRoleName = (String)(roleData[2]);
		Long matchId = LongUtils.obj2long(data[1]);
		if (roleData[0] == null) {// 如果跨服数据未同步过来，视为进入失败
			KuafuMsgSender.send2KuafuSource(userRoleId,
					InnerCmdType.KUAFU_ARENA_1V1_ENTER_STAGE_FAIL, null);
			ChuanQiLog.error("userRoleId={} recieve data fail in matchId={}",userRoleId,matchId);
			clearUserRoleData(userRoleId, false);
			return;
		}
		Integer jifen = (Integer) data[2];
		kuafuMatchManager.enterMatch(userRoleId,userRoleName, matchId, jifen, roleData);
		publicRoleStateExportService.change2PublicOnline(userRoleId);
	}

	public void enterStage(Long matchId) {
		// 判断人员是否到齐，
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		Map<Long, KuafuMatchMember> members = match.getMembers();
		int size = members.size();
		if (size < 2) {
			// 有人没进来
			kuafuMatchManager.removeMatchById(matchId);
			// 通知已进入的人解散
			for (KuafuMatchMember e : members.values()) {
				KuafuMsgSender.send2KuafuSource(e.getRoleId(),
						InnerCmdType.KUAFU_ARENA_1V1_ENTER_STAGE_FAIL, null);
				ChuanQiLog.error("userRoleId={} enter stage fail in matchId={}",e.getRoleId(),matchId);
				clearUserRoleData(e.getRoleId(), false);
			}
			return;
		}
		// 判断人员是否在线
		// 创建stage
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		boolean birthPointFlag = false;
		boolean fail = false;
		Long enterSuccessRoleId = null;
		for (KuafuMatchMember e : members.values()) {
			if (!e.isOnline()) {
				continue;
			}
			IRole role = null;
			try {
				role = RoleFactory.createKuaFu(e.getRoleId(), null,dataContainer.getData(GameConstants.COMPONENET_KUAFU_DATA, e.getRoleId().toString()));
			} catch (Exception ex) {
				fail = true;
				break;
			}
			RoleState roleState = dataContainer.getData(
					GameModType.STAGE_CONTRAL, e.getRoleId().toString());
			if (null == roleState) {
				roleState = new RoleState(e.getRoleId());
				dataContainer.putData(GameModType.STAGE_CONTRAL, e.getRoleId()
						.toString(), roleState);
			}
			int[] birthPoint = null;
			if (!birthPointFlag) {
				birthPoint = publicConfig.getZuobiao1();
				birthPointFlag = true;
			} else {
				birthPoint = publicConfig.getZuobiao2();
			}
			Object[] applyEnterData = new Object[] { publicConfig.getMapId(),
					birthPoint[0], birthPoint[1], MapType.KUAFU_AREBA_SINGLE,
					null, null, matchId };
			// 传送前加一个无敌状态
			role.getStateManager().add(new NoBeiAttack());
			role.setChanging(true);
			StageMsgSender.send2StageControl(role.getId(),
					InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
			KuafuMsgSender.send2KuafuSource(
					e.getRoleId(),
					ClientCmdType.KUAFU_ARENA_1V1_ENTER,
					GameSystemTime.getSystemMillTime()
							+ publicConfig.getDaojishi2() * 1000L
							+ publicConfig.getZongtime() * 1000L);
			enterSuccessRoleId = e.getRoleId();
		}
		if (fail) {
			// 非正常失败 不扣积分
			for (KuafuMatchMember e : members.values()) {
				if (enterSuccessRoleId != null) {
					clearUserRoleData(e.getRoleId(),
							enterSuccessRoleId.equals(e.getRoleId()));
				}
			}
			ChuanQiLog.error("enter stage fail matchId={}",matchId);
		} else {
			for (KuafuMatchMember e : members.values()) {
				if (e.isOnline()) {
					KuafuMsgSender.send2KuafuSource(e.getRoleId(),
							InnerCmdType.KUAFU_ARENA_1V1_ENTER_XIAOHEIWU, null);
				}else{
				}
			}
			// 启动开始的定时
			BusTokenRunable runable = new BusTokenRunable(
					GameConstants.DEFAULT_ROLE_ID,
					InnerCmdType.KUAFU_ARENA_1V1_START_TO_KUAFU, matchId);
			scheduleExportService.schedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_START + matchId,
					runable, publicConfig.getDaojishi2(), TimeUnit.SECONDS);
		}
	}

	public void start(Long matchId) {
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		ChuanQiLog.info("matchId={} start", matchId);
		match.setStart(true);
		Map<Long, KuafuMatchMember> members = match.getMembers();
		// TODO 判断两人是都在场景里
		for (KuafuMatchMember e : members.values()) {
			if (e.isOnline()) {
				KuafuMsgSender.send2KuafuSource(e.getRoleId(),
						InnerCmdType.KUAFU_ARENA_1V1_PK_START, null);
			}
		}
		boolean kickSchedule = false;
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		if (match.isAllOnline()) {
			kickSchedule = true;
			// 启动强制结束的定时
			BusTokenRunable runable = new BusTokenRunable(
					GameConstants.DEFAULT_ROLE_ID,
					InnerCmdType.KUAFU_ARENA_1V1_FORCE_END, matchId);
			scheduleExportService.schedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_FORCE_END + matchId,
					runable, publicConfig.getZongtime(), TimeUnit.SECONDS);
		} else {
			// 直接结束
			if (match.isAllOffline()) {
				ChuanQiLog.info("all offline matchId={}", matchId);
				for (KuafuMatchMember e : members.values()) {
					clearUserRoleData(e.getRoleId(), e.isOnline());
				}
				kuafuMatchManager.removeMatchById(matchId);
				String stageId = KuafuArena1v1Constants.STAGE_ID_PREFIX + matchId;
				// 回收stage
				IStage stage = StageManager.getStage(stageId);
				if (stage == null) {
					return;// 场景不存在
				}

				if (stage.getStageType() != StageType.KUAFU_ARENA_SINGLE) {
					return;
				}
				for (KuafuMatchMember e : members.values()) {
					IRole role = stage.getElement(e.getRoleId(), ElementType.ROLE);
					if(role!=null){
						stage.leave(role);
					}
				}
				if(stage.isCanRemove()){
					StageManager.removeCopy(stage);
				}
			} else {
				kickSchedule = true;
				ChuanQiLog.info("one offline before enter stage in matchId={}",
						matchId);
				String stageId = KuafuArena1v1Constants.STAGE_ID_PREFIX
						+ matchId;
				IStage stage = StageManager.getStage(stageId);
				if (stage == null) {
					return;// 场景不存在
				}

				if (stage.getStageType() != StageType.KUAFU_ARENA_SINGLE) {
					return;// 当前地图不是领地战地图
				}
				KuafuArenaFbStage kStage = (KuafuArenaFbStage) stage;
				for (KuafuMatchMember e : members.values()) {
					if (!e.isOnline()) {
						someOneLose(e.getRoleId(), kStage);
						clearUserRoleData(e.getRoleId(), false);
						break;
					}
				}
				ChuanQiLog.info(" matchId={} end when one offline", matchId);
			}
		}
		if (kickSchedule) {
			// 启动踢人的定时
			BusTokenRunable runable1 = new BusTokenRunable(
					GameConstants.DEFAULT_ROLE_ID,
					InnerCmdType.KUAFU_ARENA_1V1_KICK_MEMBER, matchId);
			scheduleExportService.schedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_KICK_MEMBER + matchId,
					runable1,
					publicConfig.getZongtime() + publicConfig.getEndtime(),
					TimeUnit.SECONDS);
		}
	}

	public void someOneLose(Long userRoleId, KuafuArenaFbStage kStage) {
		Long matchId = kuafuMatchManager.getMatchId(userRoleId);
		if (matchId == null) {
			return;
		}
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		kStage.changeEnd();
		int useTime = (int) ((GameSystemTime.getSystemMillTime() - match
				.getStartTime()) / 1000);

		String loserName = match.getMember(userRoleId).getName();
		Integer loserJifen = match.getMember(userRoleId).getJifen();
		Map<Long, KuafuMatchMember> members = match.getMembers();
		// 发送结算面板指令
		String winnerName = null;
		Long winnerRoleId = null;
		Integer winnerJifen = null;
		for (KuafuMatchMember e : members.values()) {
			if (!e.getRoleId().equals(userRoleId)) {
				winnerRoleId = e.getRoleId();
				winnerName = e.getName();
				winnerJifen = e.getJifen();
				break;
			}
		}
		if (winnerRoleId != null) {
			KuafuMsgSender.send2KuafuSource(
					KuafuSessionManager.getServerId(winnerRoleId),
					InnerCmdType.KUAFU_ARENA_1V1_CALC_RESULT, new Object[] {
							winnerRoleId, true, loserName, loserJifen, useTime,
							matchId });
			KuafuMsgSender.send2KuafuSource(
					KuafuSessionManager.getServerId(userRoleId),
					InnerCmdType.KUAFU_ARENA_1V1_CALC_RESULT, new Object[] {
							userRoleId, false, winnerName, winnerJifen,
							useTime, matchId });
			ChuanQiLog.info("loser is {},winner is {} in matchId={}",
					new Object[] { userRoleId, winnerRoleId, matchId });
		}
	}

	public void kuafuArenaDeadHandle(Long userRoleId, String stageId) {
		Long matchId = kuafuMatchManager.getMatchId(userRoleId);
		if (matchId == null) {
			return;
		}
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFU_ARENA_SINGLE) {
			return;// 当前地图不是领地战地图
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;// 角色不存在
		}
		KuafuArenaFbStage kStage = (KuafuArenaFbStage) stage;
		if (!kStage.isEnd()) {
			someOneLose(userRoleId, kStage);
			scheduleExportService.cancelSchedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_FORCE_END + matchId);
		}
		ChuanQiLog.info("userRoleId={} dead", userRoleId);
	}

	public void kuafuArenaEnd(Long matchId) {
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		Map<Long, KuafuMatchMember> members = match.getMembers();
		String stageId = KuafuArena1v1Constants.STAGE_ID_PREFIX + matchId;
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFU_ARENA_SINGLE) {
			return;// 当前地图不是领地战地图
		}
		KuafuArenaFbStage kStage = (KuafuArenaFbStage) stage;
		boolean end = kStage.isEnd();
		Map<Long, IRole> roles = new HashMap<Long, IRole>();
		for (KuafuMatchMember e : members.values()) {
			IRole role = stage.getElement(e.getRoleId(), ElementType.ROLE);
			if (role != null) {
				roles.put(e.getRoleId(), role);
			}
		}
		if (!end) {
			// 此时还没结束
			Long UserRoleId1 = null;
			Long UserRoleId2 = null;
			for (KuafuMatchMember e : members.values()) {
				if (UserRoleId1 == null) {
					UserRoleId1 = e.getRoleId();
				} else {
					UserRoleId2 = e.getRoleId();
				}
			}
			IRole role1 = roles.get(UserRoleId1);
			IRole role2 = roles.get(UserRoleId2);
			if (role1 != null && role2 != null) {
				if (role1.getFightAttribute().getCurHp() > role2
						.getFightAttribute().getCurHp()) {
					someOneLose(UserRoleId2, kStage);
				} else {
					someOneLose(UserRoleId1, kStage);
				}
			} else if (role1 == null && role2 != null) {
				someOneLose(UserRoleId2, kStage);
			} else if (role1 != null && role2 == null) {
				someOneLose(UserRoleId1, kStage);
			}
			kStage.changeEnd();
		}
	}

	public void kickAllMember(Long matchId) {
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		Map<Long, KuafuMatchMember> members = match.getMembers();
		// 回收match
		kuafuMatchManager.removeMatchById(matchId);
		for (KuafuMatchMember e : members.values()) {
			if(e.isOnline()){
				clearUserRoleData(e.getRoleId(), true);
			}
		}
		String stageId = KuafuArena1v1Constants.STAGE_ID_PREFIX + matchId;
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFU_ARENA_SINGLE) {
			return;// 当前地图不是领地战地图
		}
		for (KuafuMatchMember e : members.values()) {
			IRole role = stage.getElement(e.getRoleId(), ElementType.ROLE);
			if(role!=null){
				stage.leave(role);
			}
		}
		if(stage.isCanRemove()){
			StageManager.removeCopy(stage);
		}
		ChuanQiLog.info("matchId ={} kick all member end", matchId);
	}

	public void clearUserRoleData(Long userRoleId, boolean flag) {
		kuafuMatchManager.removeUserMatch(userRoleId);
		if (flag) {
			KuafuMsgSender.send2KuafuSource(userRoleId,
					InnerCmdType.KUAFU_ARENA_1V1_LEAVE_FB, null);
		}
		KuafuMsgSender.send2KuafuSource(userRoleId,
				ClientCmdType.AOI_ELEMENT_CLEAR, null);
		// 回收role
		dataContainer.removeData(GameConstants.COMPONENET_KUAFU_DATA,
				userRoleId.toString());
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
				null);
	}

	public void exit(Long userRoleId) {
		Long matchId = kuafuMatchManager.getMatchId(userRoleId);
		if (matchId == null) {
			return;
		}
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		KuafuMatchMember member = match.getMember(userRoleId);
		if (!member.isOnline()) {
			return;
		}
		String stageId = KuafuArena1v1Constants.STAGE_ID_PREFIX + matchId;
		// 回收stage
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFU_ARENA_SINGLE) {
			return;// 当前地图不是领地战地图
		}
		KuafuArenaFbStage kStage = (KuafuArenaFbStage) stage;
		if (!kStage.isEnd()) {
			someOneLose(userRoleId, kStage);
			scheduleExportService.cancelSchedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_FORCE_END + matchId);
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role!=null){
			stage.leave(role);
		}
		clearUserRoleData(userRoleId, true);
		member.setOnline(false);
		if (match.isAllOffline()) {
			kuafuMatchManager.removeMatchById(matchId);
			scheduleExportService.cancelSchedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_KICK_MEMBER + matchId);
			if(kStage.isCanRemove()){
				StageManager.removeCopy(kStage);
			}
		}
		ChuanQiLog
				.info("userRoleId={} exit in matchId={}", userRoleId, matchId);
	}

	public boolean handleOffline(Long userRoleId) {
		Long matchId = kuafuMatchManager.getMatchId(userRoleId);
		if (matchId == null) {
			return false;
		}
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return false;
		}
		ChuanQiLog.info("userRoleId={} offline in matchId={} start={}",
				userRoleId, matchId, match.isStart());
		KuafuMatchMember member = match.getMember(userRoleId);
		if(member==null){
			return false;
		}
		if (!member.isOnline()) {
			return false;
		}else{
			member.setOnline(false);
		}
		String stageId = KuafuArena1v1Constants.STAGE_ID_PREFIX + matchId;
		// 回收stage
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return true;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFU_ARENA_SINGLE) {
			return true;// 当前地图不是领地战地图
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role!=null){
			stage.leave(role);
		}
		if (!match.isStart()) {
			return true;
		}
		KuafuArenaFbStage kStage = (KuafuArenaFbStage) stage;
		if (!kStage.isEnd()) {
			someOneLose(userRoleId, kStage);
			scheduleExportService.cancelSchedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_FORCE_END + matchId);
		}
		clearUserRoleData(userRoleId, false);
		member.setOnline(false);
		if (match.isAllOffline()) {
			kuafuMatchManager.removeMatchById(matchId);
			scheduleExportService.cancelSchedule(
					GameConstants.DEFAULT_ROLE_ID.toString(),
					GameConstants.COMPONENT_KUAFU_ARENA_KICK_MEMBER + matchId);
			
		}
		return true;
	}

	public boolean isKuafuArenaStart(Long userRoleId) {
		Long matchId = kuafuMatchManager.getMatchId(userRoleId);
		if (matchId == null) {
			return false;
		}
		KuafuMatch match = kuafuMatchManager.getMatchById(matchId);
		if (match == null) {
			return false;
		}
		return match.isStart();
	}
}
