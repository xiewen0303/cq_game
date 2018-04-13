package com.junyou.stage.model.skill.harm;

import com.junyou.constants.GameConstants;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.role.DeadInfo;

public class HarmPrintUtils {
	
	/**
	 * 伤害信息
	 */
	public static Object[] harmMsg(IHarm harm) {
		Integer harmTypeValue = harm.getType().getVal();
		if(harm.getHarmTypeValue() != null){
			harmTypeValue = harm.getHarmTypeValue();
		}
		
		/*int value = harm.getKouHpValue();
		if(HarmType.ZHILIAO.getVal().equals(harmTypeValue)){
			value = harm.getVal();
		}
		
		if(harm.getKouMpValue() == null && harm.getKouZyValue() == null){
			
			return new Object[]{
					harm.getTarget().getId(),
					harmTypeValue,
					value,
					harm.getTarget().getStateManager().isDead()
			};
		}else{
			return new Object[]{
					harm.getTarget().getId(),
					harmTypeValue,
					value,
					harm.getTarget().getStateManager().isDead(),
					harm.getKouMpValue(),
					harm.getKouZyValue()
			};
		}*/
		return new Object[]{
				harm.getTarget().getId(),
				harmTypeValue,
				harm.getVal(),
				harm.getTarget().getStateManager().isDead(),
				harm.getDdVal(),
				harm.getFsVal()
			};
	}

	/**
	 * 死亡信息
	 */
	public static Object deadMsg(IHarm harm) {
		
		IFighter killer = harm.getAttacker();
//		if(ElementType.isFaBao(killer.getElementType())){
//			killer = ((IFaBao)killer).getOwner();
//		}
		
		if(ElementType.isMonster(harm.getTarget().getElementType())){
			return new Object[]{
					killer.getId(),
					killer.getName()
			}; 
		}else{
			return new Object[]{
					killer.getId(),
					killer.getName()
			};
		}
	}
	public static Object deadMsg(IFighter owner,DeadInfo deadInfo) {
			if(deadInfo.killedByBoss()){
				return new Object[]{GameConstants.KILL_BY_MONSTER,
						deadInfo.getKillerId(),
						deadInfo.getKillerName()
				};
			}else{
				return new Object[]{GameConstants.KILL_BY_ROLE,
						deadInfo.getKillerId(),
						deadInfo.getKillerName()
				};
			}
	}

	/**
	 * 杀手(玩家)信息
	 */
	public static Object killerMsg(IHarm harm) {
		
		IFighter killer = harm.getAttacker();
//		if(ElementType.isFaBao(killer.getElementType())){
//			killer = ((IFaBao)killer).getOwner();
//		}
		
		return new Object[]{
				killer.getId(),
				killer.getName()
		};
	}

}
