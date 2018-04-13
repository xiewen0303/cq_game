package com.junyou.stage.model.fight.statistic;

import java.util.HashMap;
import java.util.Map;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.harm.HarmUtils;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * 
 * @description
 * @author LiuJuan
 * @created 2011-12-15 下午01:50:23
 */
public class MonsterFightStatistic extends AbsFightStatistic implements IMonsterFightStatistic {
	
	
	private Map<Long,Integer> harmStatistic;
	
	public MonsterFightStatistic(IFighter fighter) {
		super(fighter);
	}

	@Override
	public Map<Long, Integer> pullHarmStatistic() {
		
		Map<Long,Integer> harmStatistic = this.harmStatistic;
		
		this.harmStatistic = null;
		
		return harmStatistic;
	}
	
	@Override
	public void addHarm(IHarm harm) {
		super.addHarm(harm);
		
		if(harm.getVal() > 0){

			if(HarmUtils.belong2Role(harm.getAttacker())){
				
				if(null == harmStatistic){
					harmStatistic = new HashMap<Long, Integer>();
				}
				
				IRole role = HarmUtils.getBelongRole(harm.getAttacker());
				Integer val = harmStatistic.get(role.getId());
				if(null == val){
					val = 0;
				}
				
				Long total = val * 1L + harm.getVal();
				if(total > Integer.MAX_VALUE){
					total = Integer.MAX_VALUE * 1L;
				}
				
				harmStatistic.put(role.getId(), total.intValue());
				
				Monster monster = (Monster) harm.getTarget();
				//阵营战雕像死亡
				if(monster.getMonsterType() == GameConstants.CAMP_TYPE){
					StageMsgSender.send2StageInner(role.getId(), role.getStage().getId(), InnerCmdType.S_CAMP_DEAD, new Object[]{GameConstants.KILL_MONSTER});
				}
			}
			
		}
	}
	
}
