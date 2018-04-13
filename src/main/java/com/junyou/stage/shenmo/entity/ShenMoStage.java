package com.junyou.stage.shenmo.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.shenmo.configure.ShenMoCampConfig;
import com.junyou.bus.shenmo.configure.ShenMoScoreConfig;
import com.junyou.bus.shenmo.configure.export.ShenMoCampConfigExportService;
import com.junyou.bus.shenmo.configure.export.ShenMoScoreConfigExportService;
import com.junyou.bus.shenmo.util.KuafuMatch4v4Manager;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.publicconfig.configure.export.ShenMoPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;

/**
 * 神魔战场
 * 
 * @author LiuYu 2015-9-23 下午6:09:16
 */
public class ShenMoStage extends PublicFubenStage {

	public ShenMoStage(String id, Integer mapId, Integer lineNo,
			AOIManager aoiManager, PathInfoConfig pathInfoConfig,
			ShenMoPublicConfig publicConfig,
			ShenMoScoreConfigExportService shenMoScoreConfigExportService,
			ShenMoCampConfigExportService shenMoCampConfigExportService,
			KuafuMatch4v4Manager kuafuMatch4v4Manager) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,
				StageType.SHENMO_FUBEN);
		this.publicConfig = publicConfig;
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		this.shenMoScoreConfigExportService = shenMoScoreConfigExportService;
		this.shenMoCampConfigExportService = shenMoCampConfigExportService;
		this.kuafuMatch4v4Manager = kuafuMatch4v4Manager;
		super.start();
		initJingYan();
	}

	/**
	 * 场景定时器
	 */
	private StageScheduleExecutor scheduleExecutor;
	private ShenMoPublicConfig publicConfig;
	private ShenMoScoreConfigExportService shenMoScoreConfigExportService;
	private ShenMoCampConfigExportService shenMoCampConfigExportService;
	private KuafuMatch4v4Manager kuafuMatch4v4Manager;
	private int shenbingScore;
	private int xuemoScore;
	private long overTime;
	private boolean over;
	private int[] jingInfo = new int[] { 0, 0 };

	private Map<Long, ShenMoRole> roles = new HashMap<>();

	private Object[] getScoreRank() {
		if (roles.size() == 0) {
			return new Object[] {};
		} else {
			Object[] ret = new Object[roles.size()];
			List<ShenMoRole> list = new ArrayList<ShenMoRole>();
			for (ShenMoRole e : roles.values()) {
				list.add(e);
			}
			Collections.sort(list);
			for (int i = 0; i < list.size(); i++) {
				ShenMoRole e = list.get(i);
				ret[i] = new Object[] { i + 1, e.getScore(),
						e.getRole().getName(), e.getJing(), e.getKill(),
						e.getAssists(), e.getDead() };
			}
			return ret;
		}
	}
	private Object[] getMemberNames(){
		try{
			Object[] ret = new Object[2];
			if (roles.size() == 0) {
				return ret;
			} else {
				StringBuffer sb = new StringBuffer();
				for (ShenMoRole e : roles.values()) {
					if(e.getTeam()==GameConstants.SHENMO_TEAM_SHENBING){
						sb.append(e.getRole().getName()).append(",");
					}
				}
				ret[0] = sb.toString();
				sb = new StringBuffer();
				for (ShenMoRole e : roles.values()) {
					if(e.getTeam()==GameConstants.SHENMO_TEAM_XUEMO){
						sb.append(e.getRole().getName()).append(",");
					}
				}
				ret[1] = sb.toString();
				return ret;
			}
		}catch (Exception e) {
			ChuanQiLog.error("error exists when getMemberNames",e);
		}
		return null;
	}
	public int[] initMember(Role role, String serverId, Integer teamId) {
		if (teamId == null) {
			return null;
		}
		ShenMoCampConfig config = shenMoCampConfigExportService
				.getConfig(teamId);
		if (config == null) {
			return null;
		}
		int[] zb = config.getBrith();
		if (zb == null) {
			return null;
		}
		ShenMoRole shenMoRole = new ShenMoRole(role, teamId);
		shenMoRole.setServerId(serverId);
		roles.put(shenMoRole.getUserRoleId(), shenMoRole);
		return zb;
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		if (ElementType.isRole(element.getElementType())) {
			IRole role = (IRole)element;
			role.getFightAttribute().resetHp();
			KuafuMsgSender.send2One(element.getId(),
					ClientCmdType.SHENMO_OVER_TIME, new Object[] { overTime,
							roles.get(element.getId()).getTeam() });
		}
		super.enter(element, x, y);
	}

	@Override
	public void leave(IStageElement element) {
		if (!over && ElementType.isRole(element.getElementType())) {
			ShenMoRole role = roles.get(element.getId());
			KuafuMsgSender.send2KuafuSource(role.getServerId(),
					InnerCmdType.SHENMO_ESCAPE, role.getUserRoleId());
			if (role != null) {
				role.setExit(true);
			}
			int team = role.getTeam();
			boolean hasLive = false;
			for (ShenMoRole e : roles.values()) {
				if (e.getTeam() == team) {
					if (!e.isExit()) {
						hasLive = true;
						break;
					}
				}
			}
			if (!hasLive) {
				if (team == GameConstants.SHENMO_TEAM_SHENBING) {
					over(GameConstants.SHENMO_TEAM_XUEMO);
				} else if (team == GameConstants.SHENMO_TEAM_XUEMO) {
					over(GameConstants.SHENMO_TEAM_SHENBING);
				}
			}
		}
		super.leave(element);
	}

	public void beKill(Role role, IFighter killer) {
		if (over || killer == null) {
			return;
		}
		ShenMoRole dRole = roles.get(role.getId());
		dRole.setDead(dRole.getDead() + 1);
		if (ElementType.isRole(killer.getElementType())) {
			// 结算击杀
			ShenMoRole kRole = roles.get(killer.getId());
			kRole.setKill(kRole.getKill() + 1);
			addScore(kRole, publicConfig.getKillScore());

			// 结算助攻
			List<Long> ids = role.getHatredManager().getAllHaters();
			if (ids != null && ids.size() > 0) {
				for (Long id : ids) {
					if (id.equals(killer.getId())) {
						continue;
					}
					ShenMoRole aRole = roles.get(id);
					if (aRole != null && !aRole.isExit()
							&& aRole.getTeam() != dRole.getTeam()) {
						aRole.setAssists(aRole.getAssists() + 1);
						addScore(aRole, publicConfig.getAssScore());
					}
				}
			}

		}
	}

	public void killMonster(Monster monster, Long killerId) {
		if (over) {
			return;
		}
		ShenMoRole role = roles.get(killerId);
		if (role != null) {
			ShenMoScoreConfig config = shenMoScoreConfigExportService
					.getConfig(monster.getMonsterId());
			if (config == null) {
				return;
			}
			if (config.getTeamId() != role.getTeam()) {
				addScore(role, config.getKillScore());
			}
			if (config.getType() == GameConstants.SHENMO_TYPE_SHUIJING) {
				ShenMoShuiJing oldShuiJing = (ShenMoShuiJing) monster;
				if (oldShuiJing.getTeamId() != GameConstants.SHENMO_TEAM_SYSTEM) {
					jingInfo[oldShuiJing.getTeamId() - 1]--;
				}
				role.setJing(role.getJing() + 1);
				Point zb = monster.getPosition();
				ShenMoScoreConfig shuijing = shenMoScoreConfigExportService
						.getTeamShuiJing(role.getTeam());
				MonsterConfig monsterConfig = StageConfigureHelper
						.getMonsterExportService().load(shuijing.getId());
				if (monsterConfig != null) {
					ShenMoShuiJing shenMoShuiJing = MonsterFactory
							.createShenMoShuiJing(IdFactory.getInstance()
									.generateNonPersistentId(), monsterConfig,
									shuijing);
					super.enter(shenMoShuiJing, zb.getX(), zb.getY());
					shenMoShuiJing.startScoreSchedule();
					jingInfo[role.getTeam() - 1]++;
					KuafuMsgSender.send2Many(getAllRoleIds(),
							ClientCmdType.GET_KUAFU_ARENA_4V4_SCORE_CHANGE,
							jingInfo);
				}
			} else if (!ObjectUtil.strIsEmpty(config.getBuffId())) {
				for (ShenMoRole shenMoRole : new ArrayList<>(roles.values())) {
					if (!shenMoRole.isExit()
							&& shenMoRole.getTeam() == role.getTeam()) {
						IBuff buff = BuffFactory.create(shenMoRole.getRole(),
								shenMoRole.getRole(), null, config.getBuffId());
						if (buff == null) {
							continue;
						}
						shenMoRole.getRole().getBuffManager().addBuff(buff);
					}
				}
				if (config.getType() == GameConstants.SHENMO_TYPE_BOSS) {
					KuafuMsgSender.send2Many(getAllRoleIds(),
							ClientCmdType.GET_KUAFU_ARENA_4V4_BOSS_DEAD,
							role.getTeam());
				}
			}
		}
	}

	public void addScore(ShenMoRole role, int score) {
		synchronized (this) {
			if (over) {
				return;
			}
			role.setScore(role.getScore() + score);
			if (role.getTeam() == GameConstants.SHENMO_TEAM_SHENBING) {
				shenbingScore += score;
				if (shenbingScore >= publicConfig.getWinScore()) {
					over(GameConstants.SHENMO_TEAM_SHENBING);
				}
			} else if (role.getTeam() == GameConstants.SHENMO_TEAM_XUEMO) {
				xuemoScore += score;
				if (xuemoScore >= publicConfig.getWinScore()) {
					over(GameConstants.SHENMO_TEAM_XUEMO);
				}
			}
		}
	}

	public void addScore(int teamId, int score) {
		synchronized (this) {
			if (over) {
				return;
			}
			if (teamId == GameConstants.SHENMO_TEAM_SHENBING) {
				shenbingScore += score;
				if (shenbingScore >= publicConfig.getWinScore()) {
					over(GameConstants.SHENMO_TEAM_SHENBING);
				}
			} else if (teamId == GameConstants.SHENMO_TEAM_XUEMO) {
				xuemoScore += score;
				if (xuemoScore >= publicConfig.getWinScore()) {
					over(GameConstants.SHENMO_TEAM_XUEMO);
				}
			}
		}
	}

	/**
	 * 活动结束
	 * 
	 * @param winner
	 *            胜方队伍
	 */
	private void over(int winner) {
		over = true;
		cancelSchedule();
		noticeScoreChange();
		if (winner == GameConstants.SHENMO_TEAM_SYSTEM) {
			for (ShenMoRole role : roles.values()) {
				if (role.isExit()) {
					continue;
				}
				KuafuMsgSender.send2KuafuSource(role.getServerId(),
						InnerCmdType.KUAFU_ARENA_4V4_CALC_RESULT, new Object[] {
								role.getUserRoleId(), 0, shenbingScore,
								xuemoScore, getScoreRank(),getMemberNames() });
			}
		} else {
			for (ShenMoRole role : roles.values()) {
				if (role.isExit()) {
					continue;
				}
				if (winner == role.getTeam()) {
					KuafuMsgSender
							.send2KuafuSource(role.getServerId(),
									InnerCmdType.KUAFU_ARENA_4V4_CALC_RESULT,
									new Object[] { role.getUserRoleId(), 1,
											shenbingScore, xuemoScore,
											getScoreRank(),getMemberNames() });

				} else {
					KuafuMsgSender
							.send2KuafuSource(role.getServerId(),
									InnerCmdType.KUAFU_ARENA_4V4_CALC_RESULT,
									new Object[] { role.getUserRoleId(), 2,
											shenbingScore, xuemoScore,
											getScoreRank(),getMemberNames() });
				}
			}
		}
		startKickSchedule();
	}

	/**
	 * 启动结束定时
	 */
	public void startStopSchedule() {
		overTime = GameSystemTime.getSystemMillTime()
				+ publicConfig.getHdTime() * 1000;
		StageTokenRunable runable = new StageTokenRunable(
				GameConstants.DEFAULT_ROLE_ID, getId(),
				InnerCmdType.SHENMO_ACTIVE_STOP, null);
		scheduleExecutor.schedule(getId(),
				GameConstants.COMPONENT_SHENMO_GAME_OVER, runable,
				publicConfig.getHdTime(), TimeUnit.SECONDS);

		// 开启积分定时
		noticeScoreSchedule(true);
	}

	public void cancelSchedule() {
		scheduleExecutor.cancelSchedule(getId(),
				GameConstants.COMPONENT_SHENMO_GAME_OVER);
		scheduleExecutor.cancelSchedule(getId(),
				GameConstants.COMPONENT_SHENMO_NORICE_SCORE);
	}

	public void startKickSchedule() {
		StageTokenRunable runable = new StageTokenRunable(
				GameConstants.DEFAULT_ROLE_ID, getId(),
				InnerCmdType.SHENMO_KICK_ALL, null);
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_SHENMO_KICK,
				runable, publicConfig.getMianTime(), TimeUnit.SECONDS);
	}

	public void gameOver() {
		if (shenbingScore > xuemoScore) {
			over(GameConstants.SHENMO_TEAM_SHENBING);
		} else if (shenbingScore == xuemoScore) {
			over(GameConstants.SHENMO_TEAM_SYSTEM);
		} else {
			over(GameConstants.SHENMO_TEAM_XUEMO);
		}
	}

	public void kickAll() {
		for (IStageElement role : getAllElements(ElementType.ROLE)) {
			ChuanQiLog.info("kick in shenmo stage userRoleId={}", role.getId());
			leave(role);
			// StageMsgSender.send2StageControl(role.getId(),
			// InnerCmdType.S_APPLY_LEAVE_STAGE, null);
			KuafuMsgSender.send2KuafuSource(role.getId(),
					InnerCmdType.KUAFU_ARENA_4V4_LEAVE_FB, null);
			KuafuMsgSender.send2KuafuSource(role.getId(),
					ClientCmdType.AOI_ELEMENT_CLEAR, null);
			BusMsgSender.send2BusInner(role.getId(),
					InnerCmdType.INNER_KUAFU_LEAVE, null);
			Long matchId = kuafuMatch4v4Manager.getMatchId(role.getId());
			if (matchId != null) {
				kuafuMatch4v4Manager.removeUserMatch(role.getId());
				kuafuMatch4v4Manager.removeMatchById(matchId);
			}
		}
		if(isCanRemove()){
			StageManager.removeCopy(this);
		}
	}

	public void noticeScoreSchedule(boolean first) {
		if (!first) {
			noticeScoreChange();
		}
		// 开启下次定时
		StageTokenRunable runable = new StageTokenRunable(
				GameConstants.DEFAULT_ROLE_ID, getId(),
				InnerCmdType.SHENMO_SCHEDULE_NOTICE_SCORE, null);
		scheduleExecutor.schedule(getId(),
				GameConstants.COMPONENT_SHENMO_NORICE_SCORE, runable,
				GameConstants.SHENMO_NOTICE_SCORE_TIME, TimeUnit.MILLISECONDS);
	}

	private void noticeScoreChange() {
		Object[] shenbing = new Object[5];
		Object[] xuemo = new Object[5];
		int sbIndex = 0;
		int xmIndex = 0;
		for (ShenMoRole role : roles.values()) {
			if (role.getTeam() == GameConstants.SHENMO_TEAM_SHENBING) {
				shenbing[sbIndex++] = role.getScoreInfo();
			} else {
				xuemo[xmIndex++] = role.getScoreInfo();
			}
		}
		shenbing[4] = shenbingScore;
		xuemo[4] = xuemoScore;
		Object[] msg = new Object[] { shenbing, xuemo };
		KuafuMsgSender.send2Many(getAllRoleIds(),
				ClientCmdType.SHENMO_NOTICE_CUR_SCORE, msg);
	}

	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public boolean isCanPk() {
		return true;
	}

	@Override
	public boolean isCanHasTangbao() {
		return false;
	}

	@Override
	public boolean isCanHasChongwu() {
		return false;
	}

	@Override
	public boolean isCanUseShenQi() {
		return false;
	}

	@Override
	public boolean isCanRemove() {
		return over && getAllRoleIds().length < 1;
	}

	@Override
	public void enterNotice(Long userRoleId) {

	}

	@Override
	public void exitNotice(Long userRoleId) {

	}

	@Override
	public boolean isFubenMonster() {
		return false;
	}

	private void initJingYan() {
		List<int[]> list = publicConfig.getJyzb();
		if (list != null && list.size() > 0) {
			for (int[] zb : list) {
				ShenMoScoreConfig shuijing = shenMoScoreConfigExportService
						.getTeamShuiJing(GameConstants.SHENMO_TEAM_SYSTEM);
				MonsterConfig monsterConfig = StageConfigureHelper
						.getMonsterExportService().load(shuijing.getId());
				ShenMoShuiJing smsj = MonsterFactory.createShenMoShuiJing(
						IdFactory.getInstance().generateNonPersistentId(),
						monsterConfig, shuijing);
				enter(smsj, zb[0], zb[1]);
			}
		}
	}

	@Override
	public Short getBackFuHuoCmd() {
		return InnerCmdType.SHENMO_AUTO_FUHUO;
	}

	@Override
	public Integer getQzFuhuoSecond() {
		return publicConfig.getZdfh();
	}

	public int[] getRevivePoint(Long userRoleId) {
		ShenMoRole role = roles.get(userRoleId);
		if (role == null) {
			return null;
		}
		ShenMoCampConfig config = shenMoCampConfigExportService.getConfig(role
				.getTeam());
		if (config == null) {
			return null;
		}
		return config.getBrith();
	}

	@Override
	public boolean isCanDazuo() {
		return false;
	}

	public int getTeamId(Long userRoleId) {
		ShenMoRole role = roles.get(userRoleId);
		if (role == null) {
			return 0;
		}
		return role.getTeam();
	}
}
