package com.junyou.stage.model.stage.kuafuboss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuBossPublicConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.KuafuBossMonster;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;

public class KuafuBossStage extends PublicFubenStage {
	private DingShiActiveConfig dingShiActiveConfig;

	public DingShiActiveConfig getDingShiActiveConfig() {
		return dingShiActiveConfig;
	}

	public void setDingShiActiveConfig(DingShiActiveConfig dingShiActiveConfig) {
		this.dingShiActiveConfig = dingShiActiveConfig;
	}

	public KuafuBossStage(String id, Integer mapId, Integer lineNo,
			AOIManager aoiManager, PathInfoConfig pathInfoConfig,
			KuafuBossPublicConfig publicConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,
				StageType.KUAFUBOSS);
		this.publicConfig = publicConfig;
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		super.start();
	}

	private StageScheduleExecutor scheduleExecutor;
	private KuafuBossPublicConfig publicConfig;
	private KuafuBossMonster kuafuBossMonster;
	private boolean bossAlive = true;
	private Integer bossId;//这里的id是成长表里的id,第一次时候为null
	

	public Integer getBossId() {
		return bossId;
	}

	public void setBossId(Integer bossId) {
		this.bossId = bossId;
	}

	public boolean isBossAlive() {
		return bossAlive;
	}

	public void setBossAlive(boolean bossAlive) {
		this.bossAlive = bossAlive;
	}

	public StageScheduleExecutor getStageScheduleExecutor() {
		return scheduleExecutor;
	}

	public KuafuBossMonster getKuafuBossMonster() {
		return kuafuBossMonster;
	}

	public void startForceKickSchedule() {
		if (dingShiActiveConfig == null) {
			return;
		}
		long endTime = DatetimeUtil.getTheTime(
				dingShiActiveConfig.getEndTime1()[0],
				dingShiActiveConfig.getEndTime1()[1]);
		long cur = GameSystemTime.getSystemMillTime();
		StageTokenRunable runable = new StageTokenRunable(
				GameConstants.DEFAULT_ROLE_ID, getId(),
				InnerCmdType.KUAFUBOSS_FORCE_KICK, null);
		scheduleExecutor.schedule(getId(),
				GameConstants.COMPONENT_KUAFU_BOSS_FORCE_KICK, runable, endTime
						- cur, TimeUnit.MILLISECONDS);
	}

	public void cancelForceKickSchedule() {
		scheduleExecutor.cancelSchedule(getId(),
				GameConstants.COMPONENT_KUAFU_BOSS_FORCE_KICK);
	}

	public void startAddExpSchedule(Long userRoleId) {
		StageTokenRunable runable = new StageTokenRunable(userRoleId, getId(),
				InnerCmdType.KUAFUBOSS_ADD_EXP_DINGSHI, new Object[] { getId(),
						publicConfig.getJiangexp() });
		scheduleExecutor.schedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_BOSS_ADD_EXP, runable,
				publicConfig.getExptime(), TimeUnit.SECONDS);
	}

	private void cancelAddExpSchedule(Long userRoleId) {
		scheduleExecutor.cancelSchedule(userRoleId.toString(),
				GameConstants.COMPONENT_KUAFU_BOSS_ADD_EXP);
	}

	private List<Long> roleIds = new ArrayList<Long>();

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public boolean isRegister(Long userRoleId) {
		return roleIds.contains(userRoleId);
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if (ElementType.isRole(element.getElementType())) {
			startAddExpSchedule(element.getId());
			if (!roleIds.contains(element.getId())) {
				roleIds.add(element.getId());
			}
		}
		if (ElementType.isMonster(element.getElementType())) {
			kuafuBossMonster = (KuafuBossMonster) element;
		}
	}

	@Override
	public void leave(IStageElement element) {
		super.leave(element);
		if (ElementType.isRole(element.getElementType())) {
			cancelAddExpSchedule(element.getId());
		}
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
		return true;
	}

	@Override
	public boolean isCanHasChongwu() {
		return true;
	}

	@Override
	public boolean isCanUseShenQi() {
		return true;
	}

	@Override
	public boolean isCanRemove() {
		return !isOpen() && getAllRoleIds().length < 1;
	}

	public void enterNotice(Long userRoleId) {
		StageMsgSender.send2One(
				userRoleId,
				ClientCmdType.ENTER_KUAFU_BOSS,
				new Object[] { AppErrorCode.SUCCESS,
						dingShiActiveConfig.getCalcEndSecondTime(),
						bossAlive ? 0 : 1 });
	}

	public void exitNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.EXIT_KUAFU_BOSS,
				AppErrorCode.OK);
	}

	@Override
	public boolean isFubenMonster() {
		return false;
	}

	@Override
	public Short getBackFuHuoCmd() {
		return InnerCmdType.KUAFUBOSS_AUTO_FUHUO;
	}

	@Override
	public Integer getQzFuhuoSecond() {
		return publicConfig.getFuhuotime();
	}

	private static Random random = new Random();

	public int[] getRevivePoint() {
		List<int[]> zuobiao = publicConfig.getZuobiao1();
		return zuobiao.get(random.nextInt(zuobiao.size()));
	}

	@Override
	public boolean isCanDazuo() {
		return false;
	}

}