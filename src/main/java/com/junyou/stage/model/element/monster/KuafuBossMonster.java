package com.junyou.stage.model.element.monster;

import java.util.concurrent.TimeUnit;

import com.junyou.bus.activityboss.manage.BossHurtRank;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * @author LiuYu 2015-8-24 上午11:13:32
 */
public class KuafuBossMonster extends AbsHurtRankMonster {

	public KuafuBossMonster(Long id, String teamId, MonsterConfig monsterConfig) {
		super(id, teamId, monsterConfig);
	}

	@Override
	protected void scheduleDisappearHandle(IHarm harm) {
		// 启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage()
				.getId(), InnerCmdType.AI_DISAPPEAR, new Object[] { getId(),
				getElementType().getVal() });
		this.getScheduler().schedule(getId().toString(),
				GameConstants.COMPONENT_AI_RETRIEVE, runable,
				disappearDuration, TimeUnit.SECONDS);

		// 取消排行榜刷新定时器
		this.getScheduler().cancelSchedule(this.getId().toString(),
				GameConstants.KUAFUBOSS_REFRESH_RANK);
	}

	/**
	 * 定时刷排行榜
	 */
	public void schedulerRefreshRank() {
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage()
				.getId(), InnerCmdType.KUAFU_BOSS_RANK, getId());
		this.getScheduler().schedule(this.getId().toString(),
				GameConstants.KUAFUBOSS_REFRESH_RANK, runable,
				GameConstants.KUAFUBOSS_RANK_DELAY, TimeUnit.MILLISECONDS);
	}

	/**
	 * 新增伤害值
	 * 
	 * @param roleId
	 * @param addHurt
	 */
	public void addHurt(IRole role, long addHurt) {
		long roleId = role.getId();
		BossHurtRank bossHurtRank = bossHurts.get(roleId);
		if (bossHurtRank == null) {
			bossHurtRank = new BossHurtRank();
			bossHurtRank.setHurt(addHurt);
			bossHurtRank.setRoleName(role.getName());
			bossHurtRank.setUserRoleId(roleId);

			bossHurts.put(roleId, bossHurtRank);
			ranks.add(bossHurtRank);
		}
		bossHurtRank.setHurt(bossHurtRank.getHurt() + addHurt);
	}
	
	public void deadHandle(IHarm harm) {
		super.deadHandle(harm);
		StageMsgSender.send2StageInner( harm.getAttacker().getId(), getStage().getId(), InnerCmdType.KUAFUBOSS_DEAD, null);
	}

	@Override
	public Long getId(IRole role) {
		return role.getId();
	}

	@Override
	public short getNoticeCmd() {
		return ClientCmdType.KUAFU_BOSS_RANK;
	}

	@Override
	public short getCloseRankCmd() {
		return ClientCmdType.KUAFU_BOSS_RANK_CLOSE;
	}
}
