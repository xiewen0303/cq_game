package com.junyou.bus.shenmo.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.shenmo.constants.ShenmoConstants;
import com.junyou.bus.shenmo.util.KuafuMatch4v4;
import com.junyou.bus.shenmo.util.KuafuMatch4v4Manager;
import com.junyou.bus.shenmo.util.KuafuMatch4v4Member;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ShenMoPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.shenmo.entity.ShenMoStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.number.LongUtils;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuArena4v4KuafuService {

	@Autowired
	private KuafuMatch4v4Manager kuafuMatch4v4Manager;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;

	public ShenMoPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_SHENMO);
	}

	public void recieveRoleData(Object[] data) {
		Object[] roleData = (Object[]) data[0];
		Long userRoleId = LongUtils.obj2long(roleData[1]);
		String userRoleName = (String) (roleData[2]);
		Long matchId = LongUtils.obj2long(data[1]);
		if (roleData[0] == null) {// 如果跨服数据未同步过来，视为进入失败
			KuafuMsgSender.send2KuafuSource(userRoleId,
					InnerCmdType.KUAFU_ARENA_4V4_ENTER_STAGE_FAIL, null);
			ChuanQiLog.error(
					"userRoleId={} recieve data fail in 4v4 matchId={}",
					userRoleId, matchId);
			BusMsgSender.send2BusInner(userRoleId,
					InnerCmdType.INNER_KUAFU_LEAVE, null);
			return;
		}
		Integer jifen = (Integer) data[2];
		Integer camp = (Integer) data[3];
		String serverId = (String) data[4];
		kuafuMatch4v4Manager.enterMatch(userRoleId, userRoleName, matchId,
				jifen, camp, serverId, roleData);
		publicRoleStateExportService.change2PublicOnline(userRoleId);
	}

	public void enterStage(Long matchId) {
		// 判断人员是否到齐，
		KuafuMatch4v4 match = kuafuMatch4v4Manager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		Map<Long, KuafuMatch4v4Member> members = match.getMembers();
		int size = members.size();
		if (size < 8) {
			// 有人没进来
			kuafuMatch4v4Manager.removeMatchById(matchId);
			// 通知已进入的人解散
			for (KuafuMatch4v4Member e : members.values()) {
				kuafuMatch4v4Manager.removeUserMatch(e.getRoleId());
				KuafuMsgSender.send2KuafuSource(e.getRoleId(),
						InnerCmdType.KUAFU_ARENA_4V4_ENTER_STAGE_FAIL, null);
				ChuanQiLog.error(
						"userRoleId={} enter 4v4 stage fail in matchId={}",
						e.getRoleId(), matchId);
				BusMsgSender.send2BusInner(e.getRoleId(),
						InnerCmdType.INNER_KUAFU_LEAVE, null);
			}
			return;
		}
		ShenMoPublicConfig publicConfig = getPublicConfig();
		String stageId = ShenmoConstants.STAGE_ID_PREFIX + matchId;
		int mapId = publicConfig.getMap();
		MapConfig mapConfig = mapConfigExportService.load(mapId);
		ShenMoStage stage = publicFubenStageFactory.createShenMoStage(stageId,
				mapConfig);
		StageManager.addStageCopy(stage);
		stage.startStopSchedule();
		for (KuafuMatch4v4Member e : members.values()) {
			if (!e.isOnline()) {
				continue;
			}
			IRole role = null;
			try {
				role = RoleFactory.createKuaFu(e.getRoleId(), null,
						dataContainer.getData(
								GameConstants.COMPONENET_KUAFU_DATA, e
										.getRoleId().toString()));
			} catch (Exception ex) {
				BusMsgSender.send2BusInner(e.getRoleId(),
						InnerCmdType.INNER_KUAFU_LEAVE, null);
				continue;
			}
			RoleState roleState = dataContainer.getData(
					GameModType.STAGE_CONTRAL, e.getRoleId().toString());
			if (null == roleState) {
				roleState = new RoleState(e.getRoleId());
				dataContainer.putData(GameModType.STAGE_CONTRAL, e.getRoleId()
						.toString(), roleState);
			}
			int[] birthPoint = stage.initMember((Role) role, e.getServerId(),
					e.getCamp());
			if (birthPoint == null) {
				ChuanQiLog
						.error("shenmo birthPoint error camp={}", e.getCamp());
				BusMsgSender.send2BusInner(e.getRoleId(),
						InnerCmdType.INNER_KUAFU_LEAVE, null);
				continue;
			}
			Object[] applyEnterData = new Object[] { publicConfig.getMap(),
					birthPoint[0], birthPoint[1], MapType.SHENMO_FUBEN_MAP,
					null, null, matchId };
			// 传送前加一个无敌状态
			role.getStateManager().add(new NoBeiAttack());
			role.setChanging(true);
			KuafuMsgSender.send2KuafuSource(role.getId(),
					ClientCmdType.AOI_ELEMENT_CLEAR, null);
			StageMsgSender.send2StageControl(role.getId(),
					InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
			KuafuMsgSender.send2KuafuSource(e.getRoleId(),
					InnerCmdType.KUAFU_ARENA_4V4_ENTER_XIAOHEIWU, null);
		}
		// 启动开始的定时
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.KUAFU_ARENA_4V4_START_TO_KUAFU, matchId);
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_START + matchId,
				runable, publicConfig.getDaojishi2(), TimeUnit.SECONDS);
	}

	public void start(Long matchId) {
		KuafuMatch4v4 match = kuafuMatch4v4Manager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		ChuanQiLog.info("matchId={} start", matchId);
		match.setStart(true);
		Map<Long, KuafuMatch4v4Member> members = match.getMembers();
		for (KuafuMatch4v4Member e : members.values()) {
			if (e.isOnline()) {
				KuafuMsgSender.send2KuafuSource(e.getRoleId(),
						InnerCmdType.KUAFU_ARENA_4V4_PK_START, null);
			}
		}
	}

	public void exit(Long userRoleId) {
		Long matchId = kuafuMatch4v4Manager.getMatchId(userRoleId);
		if (matchId == null) {
			return;
		}
		KuafuMatch4v4 match = kuafuMatch4v4Manager.getMatchById(matchId);
		if (match == null) {
			return;
		}
		KuafuMatch4v4Member member = match.getMember(userRoleId);
		if (!member.isOnline()) {
			return;
		}
		String stageId = ShenmoConstants.STAGE_ID_PREFIX + matchId;
		// 回收stage
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.SHENMO_FUBEN) {
			return;// 当前地图不是领地战地图
		}
		ShenMoStage kStage = (ShenMoStage) stage;
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role != null) {
			kStage.leave(role);
		}
		member.setOnline(false);
		if (match.isAllOffline()) {
			kuafuMatch4v4Manager.removeMatchById(matchId);
			if(kStage.isCanRemove()){
				StageManager.removeCopy(kStage);
			}
			kStage.cancelSchedule();
		}
		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.KUAFU_ARENA_4V4_LEAVE_FB, null);
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
				null);
		ChuanQiLog
				.info("userRoleId={} exit in matchId={}", userRoleId, matchId);
	}

	public boolean handleOffline(Long userRoleId) {
		Long matchId = kuafuMatch4v4Manager.getMatchId(userRoleId);
		if (matchId == null) {
			return false;
		}
		KuafuMatch4v4 match = kuafuMatch4v4Manager.getMatchById(matchId);
		if (match == null) {
			return false;
		}
		ChuanQiLog.info("userRoleId={} offline in 4v4 matchId={} start={}",
				userRoleId, matchId, match.isStart());
		KuafuMatch4v4Member member = match.getMember(userRoleId);
		if (member == null) {
			return false;
		}
		if (!member.isOnline()) {
			return false;
		} else {
			member.setOnline(false);
		}
		String stageId = ShenmoConstants.STAGE_ID_PREFIX + matchId;
		// 回收stage
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return true;// 场景不存在
		}

		if (stage.getStageType() != StageType.SHENMO_FUBEN) {
			return true;// 当前地图不是领地战地图
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role != null) {
			stage.leave(role);
		}
		if (!match.isStart()) {
			return true;
		}
		member.setOnline(false);
		if (match.isAllOffline()) {
			kuafuMatch4v4Manager.removeMatchById(matchId);
			if(stage.isCanRemove()){
				StageManager.removeCopy(stage);
			}
			ShenMoStage kStage = (ShenMoStage) stage;
			kStage.cancelSchedule();
		}
		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.KUAFU_ARENA_4V4_LEAVE_FB, null);
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
				null);
		return true;
	}

	public boolean isKuafuArenaStart(Long userRoleId) {
		Long matchId = kuafuMatch4v4Manager.getMatchId(userRoleId);
		if (matchId == null) {
			return false;
		}
		KuafuMatch4v4 match = kuafuMatch4v4Manager.getMatchById(matchId);
		if (match == null) {
			return false;
		}
		return match.isStart();
	}
}
