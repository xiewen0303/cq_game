package com.junyou.stage.model.element.monster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.bus.activityboss.manage.ActivityBossSort;
import com.junyou.bus.activityboss.manage.BossHurtRank;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 显示伤害排行的抽奖怪物
 * @author LiuYu
 * 2015-8-24 下午2:46:35
 */
public abstract class AbsHurtRankMonster extends Monster{
	public AbsHurtRankMonster(Long id, String teamId,MonsterConfig monsterConfig) {
		super(id, teamId, monsterConfig);
	}
	
	protected Map<Long,BossHurtRank> bossHurts = new ConcurrentHashMap<>();
	protected List<BossHurtRank> ranks = new ArrayList<>();
	private Object[] rankData;
	
	public BossHurtRank getBossHurtRank(Long userRoleId){
		return bossHurts.get(userRoleId);
	}
	
	public Set<Long> getHurtRoleIds(){
		return bossHurts.keySet();
	}
	
	public List<BossHurtRank> getRanks(){
		if(ranks.size() > 1){
			Collections.sort(ranks,new ActivityBossSort());
		}
		return ranks;
	}
	
	/**
	 * 获得排行数据给客服端展示
	 * @param bossHurtRanks
	 * @return
	 */
	private Object[] getRankDatas(){
		List<BossHurtRank> bossHurtRanks = getRanks();
		List<Object[]> list = new ArrayList<>();
		for (int i = 0;i < bossHurtRanks.size();i++) {
			BossHurtRank bossHurtRank = bossHurtRanks.get(i);
			bossHurtRank.setRank(i + 1);
			if(i < GameConstants.MAX_HURT_RANK){
				list.add(new Object[]{bossHurtRank.getRoleName(),bossHurtRank.getHurt()});
			}
		}
		return list.toArray();
	}
	
	public void noticeRankData(){
		rankData = null;
		for (IStageElement element : getStage().getSurroundElements(getPosition(), ElementType.ROLE)) {
			IRole role = (IRole)element;
			Object[] result = getRankData(getId(role));
			if(result != null){
				StageMsgSender.send2One(role.getId(), getNoticeCmd(), result);
			}
		}
	}
	
	/**
	 * 获取排行数据
	 * @param userRoleId	排行主键（个人排行为角色id，公会排行为公会id)
	 * @return
	 */
	public Object[] getRankData(Long userRoleId){
		Object[] result = new Object[2];
		if(rankData == null){
			rankData = getRankDatas();
		}
		if(rankData.length < 1){
			return null;
		}
		result[1] = rankData;
		if(userRoleId != null){
			BossHurtRank bossHurtRank = bossHurts.get(userRoleId);
			if(bossHurtRank != null){
				result[0] = new Object[]{bossHurtRank.getRank(),bossHurtRank.getHurt()};
			}
		}
		return result;
	}
	public boolean isShowHurtRank(){
		return true;
	}
	public abstract Long getId(IRole role);
	public abstract short getNoticeCmd();
	public abstract short getCloseRankCmd();
}
