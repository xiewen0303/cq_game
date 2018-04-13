package com.junyou.stage.model.element.monster;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.bus.activityboss.manage.BossHurtRank;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.publicconfig.configure.export.NuqiPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.StageFightOutputWrapper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.state.DeadState;
import com.junyou.stage.qisha.entity.QiShaStage;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author LiuYu
 * 2015-8-24 上午11:13:32
 */
public class QiShaMonster extends AbsHurtRankMonster{
	
	private long nextNoticeTime;
	
	public QiShaMonster(Long id, String teamId, MonsterConfig monsterConfig) {
		super(id, teamId, monsterConfig);
	}
	
	public void addHurt(IRole role,long hurt){
		Long guildId = role.getBusinessData().getGuildId();
		if(guildId != null && guildId > 0){
			BossHurtRank rank = bossHurts.get(guildId);
			boolean newRank = false;
			if(rank == null){
				synchronized (bossHurts) {
					rank = bossHurts.get(guildId);
					if(rank == null){
						rank = new BossHurtRank();
						bossHurts.put(guildId, rank);
						newRank = true;
					}
				}
			}
			if(newRank){
				rank.setUserRoleId(guildId);
				rank.setRoleName(role.getBusinessData().getGuildName());
				rank.setHurt(hurt);
				ranks.add(rank);
			}else{
				rank.setHurt(rank.getHurt() + hurt);
			}
		}
	}
	
	@Override
	public void deadHandle(IHarm harm) {
		super.setDeadTime(GameSystemTime.getSystemMillTime());
		scheduleDisappearHandle(harm);
		
		try{
			//物品掉落
			IFighter originalBenefiter = harm.getAttacker().getOwner();//原始受益者
			
			IRole benefitRole = null;
			if(null != originalBenefiter && ElementType.isRole(originalBenefiter.getElementType())){
				benefitRole = (IRole)originalBenefiter;
			}
			
			if(null != benefitRole){
				//物品掉落
				StageMsgSender.send2StageInner(getId(),getStage().getId(), InnerCmdType.AI_GOODS_DROP, StageFightOutputWrapper.monsterGoodDrop(benefitRole,this));
				Map<Long, Integer> harmMap = getFightStatistic().pullHarmStatistic();
				//经验掉落
				StageMsgSender.send2StageInner(benefitRole.getId(), getStage().getId(), InnerCmdType.AI_EXP_DROP,StageFightOutputWrapper.monsterExpDrop(benefitRole,this,harmMap));
				//玩家杀怪统计
				if(benefitRole.getPet() == null){
					StageMsgSender.send2Bus(benefitRole.getId(), InnerCmdType.PET_ADD_PROGRESS,null);
				}
				NuqiPublicConfig config = PublicConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_NUQI);
				if(config != null){//杀怪加怒气
					int nuqi = benefitRole.getNuqi();
					if(nuqi < config.getMaxNq()){
						nuqi += config.getKillAdd();
						if(nuqi <= config.getMaxNq()){
							benefitRole.setNuqiNotice(nuqi);
						}
					}
				}
				killGift(benefitRole.getId());
			}
			StageMsgSender.send2Many(getStage().getSurroundRoleIds(getPosition()), ClientCmdType.QISHA_BOSS_RANK_CLOSE, null);
		}catch(Exception e){
			ChuanQiLog.error("monter dead error", e);
		}
		if(getAi() != null){
			getAi().stop();
		}
		getHatredManager().clear();
		getBuffManager().clearBuffsByDead();
		getStateManager().add(new DeadState(harm));
	}
	
	private void killGift(Long userRoleId){
		String userRoleIds = null;
		try{
			List<BossHurtRank> list = getRanks();
			if(list.size() > 0){
				Long guildId = list.get(0).getUserRoleId();
				QiShaStage stage = (QiShaStage)getStage();
				userRoleIds = stage.getGuildRoleIds(guildId);
			}
		}catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		StageMsgSender.send2Bus(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.QISHA_BOSS_DEAD, new Object[]{getMonsterId(),userRoleId,userRoleIds,getName()});
	}

	@Override
	public Long getId(IRole role) {
		return role.getBusinessData().getGuildId();
	}

	@Override
	public short getNoticeCmd() {
		return ClientCmdType.QISHA_BOSS_DAMAGE_RANK;
	}
	@Override
	public short getCloseRankCmd() {
		return ClientCmdType.QISHA_BOSS_RANK_CLOSE;
	}
	
	/**
	 * 怪物回血(处理额外心跳事件)
	 */
	public void monsterHuiFuHp(){
		super.monsterHuiFuHp();
		if(nextNoticeTime < GameSystemTime.getSystemMillTime()){
			nextNoticeTime = GameSystemTime.getSystemMillTime() + 5000;
			noticeRankData();
		}
	}
	@Override
	protected void scheduleDisappearHandle(IHarm harm) {
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{getId(),getElementType().getVal()});
		getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
	}
}
