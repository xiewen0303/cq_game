package com.junyou.stage.model.skill.harmcalculator;

import com.junyou.bus.lianyuboss.stage.LianyuBossStage;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.MathUtils;

/**
 * 暴击
 * @author LiuYu
 * @date 2015-1-29 下午6:49:59
 */
public class BaoJi {
	
	public static boolean isBaoji(IFighter fighter, ISkill skill, IFighter target) {
		
		float  rate = 1;
		boolean attackerIsLianyuMonster  = false; //攻击者是不是门派炼狱怪物
		if(fighter.getStage() instanceof LianyuBossStage){
			LianyuBossStage tStage = (LianyuBossStage) fighter.getStage();
			if(ElementType.isMonster(fighter.getElementType())){
				attackerIsLianyuMonster  = true;
			} 
			rate = tStage.getRate();
		}
		/**
		 * 暴击伤害公式
		 *     min(max(（自己暴击-目标韧性)/10000,0),45%)+5%
		 */
		long baoji = fighter.getFightAttribute().getBaoJi();
		long renxing = target.getFightAttribute().getKangBao();
		
		baoji= (long)(baoji *(attackerIsLianyuMonster?rate:1));
		renxing = (long)(renxing* (attackerIsLianyuMonster?1:rate));
		
		baoji = MathUtils.min(MathUtils.max(baoji - renxing, 0) , 5900);
		baoji += 100;
		
		
		return baoji > Lottery.roll(EffectType.getAttBase());
	}
}