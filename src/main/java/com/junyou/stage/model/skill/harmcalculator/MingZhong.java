package com.junyou.stage.model.skill.harmcalculator;

import com.junyou.bus.lianyuboss.stage.LianyuBossStage;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.MathUtils;

/**
 * 命中
 * @author LiuYu
 * @date 2015-1-29 下午2:22:53
 */
public class MingZhong {
	
	public static boolean isMingZhong(IFighter fighter, ISkill skill, IFighter target) {
		float  rate = 1;
		boolean attackerIsLianyuMonster  = false; //攻击者是不是门派炼狱怪物
		if(fighter.getStage() instanceof LianyuBossStage){
			LianyuBossStage tStage = (LianyuBossStage) fighter.getStage();
			if(ElementType.isMonster(fighter.getElementType())){
				attackerIsLianyuMonster  = true;
			} 
			 rate = tStage.getRate();
		}
		long mingzhong = fighter.getFightAttribute().getMingZhong();
		long wushiSB = fighter.getFightAttribute().getEffectAttribute(EffectType.x12.name());
		long targetSB = target.getFightAttribute().getShanBi();
		
		mingzhong= (long)(mingzhong *(attackerIsLianyuMonster?rate:1));
		wushiSB = (long)(wushiSB* (attackerIsLianyuMonster?rate:1));
		targetSB = (long)(targetSB* (attackerIsLianyuMonster?1:rate));
		
		mingzhong = mingzhong - targetSB * (EffectType.getAttBase() - wushiSB) /EffectType.getAttBase();
		
		
//		if(fighter.isPve(target)){
//			/**PVE
//			 * min(max(（自己命中-目标闪避*(10000-自己无视闪避%）/10000)）/10000,0),35%)+60%
//			 */
//			mingzhong = MathUtils.max(mingzhong, 0);
//			mingzhong = MathUtils.min(mingzhong, 3500);
//			mingzhong += 6000;
//		}else{
			/**PVP
			 * max(min（(自己命中-目标闪避*(10000-自己无视闪避%）/10000))/10000,0）,-0.45)+95%
			 */
			mingzhong = MathUtils.min(mingzhong, 0);
			mingzhong = MathUtils.max(mingzhong, -4500);
			mingzhong += 9500;
//		}
		
		return mingzhong > Lottery.roll(EffectType.getAttBase());
	}
}
