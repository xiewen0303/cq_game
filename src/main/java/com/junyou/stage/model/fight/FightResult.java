/**
 * 
 */
package com.junyou.stage.model.fight;

import java.util.List;

import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightResult;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.skill.buff.Buff;
import com.junyou.stage.model.skill.harm.Harm;
import com.junyou.stage.model.skill.harm.HarmType;
import com.junyou.utils.lottery.MathUtils;

/**
 * @description 战斗结果
 * @author ShiJie Chi
 * @created 2011-12-9上午9:49:59
 */
public class FightResult implements IFightResult {
	
	private IHarm harm;
	
	private List<IBuff> buffs;
	
	public FightResult() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param harm
	 */
	public FightResult(IHarm harm) {
		this.harm = harm;
	}

	@Override
	public void execute(IFighter target, IFighter attacker, ISkill skill) {
		
		if(isSelfTarget(target, attacker)){
			executeSelf(target, attacker, skill);
		}else{
			executeNomal(target, attacker, skill);
		}
	}

	
	
	
	
	/**
	 * 自己是否是目标(攻击者和目标都是同一个人)
	 * @param target
	 * @param attacker
	 * @return true : 是
	 */
	private boolean isSelfTarget(IFighter target, IFighter attacker){
		if(target.getId().equals(attacker.getId())){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 正常执行
	 * @param target 被攻击者
	 * @param attacker 攻击者
	 * @param skill
	 */
	private void executeNomal(IFighter target, IFighter attacker, ISkill skill){
		if(null != harm){
			
			//仇恨
			if(HarmType.isZhiLiaoHarmType(harm.getType())){
				//乙受到治疗后，生命值 H = max( 乙生命上限, H + HP)
				long hp = MathUtils.max(target.getFightAttribute().getMaxHp(), target.getFightAttribute().getCurHp() + harm.getVal());
				target.getFightAttribute().setCurHp(hp);
				
				Integer hatredVal = skill.getSkillHatred().cal1(harm.getVal());
				//对某个单位进行治疗，治疗所造成的仇恨加到
				//目标内部仇恨列表中的元素
				target.getHatredManager().addInsideHatred(hatredVal);
				
			}else {
				//计算仇恨
				Integer hatredVal1 = skill.getSkillHatred().cal1(harm.getVal());
				Integer hatredVal2 = skill.getSkillHatred().cal2(harm.getVal());
				attacker.getHatredManager().addActiveHatred(target, hatredVal2);
				
				target.getHatredManager().addPassiveHatred(attacker, hatredVal1);
				//伤害执行
				target.getFightAttribute().acceptHarm(harm);
			}
		}
		
		if(!target.getStateManager().isDead()){
			
			//buff执行
			if(null != buffs){
				for(IBuff buff : buffs){
					target.getBuffManager().addBuff(buff);
//					target.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
				}
			}
		}
	}
	
	private void executeSelf(IFighter target, IFighter attacker, ISkill skill){
			
		
		if(!target.getStateManager().isDead()){
			
			if(harm != null && HarmType.isZhiLiaoHarmType(harm.getType())){
				target.getFightAttribute().acceptHarm(harm);
			}
			
			//buff执行
			if(null != buffs){
				for(IBuff buff : buffs){
					target.getBuffManager().addBuff(buff);
//					target.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
				}
			}
		}
		
	}
	
	
	public void setHarm(IHarm harm) {
		this.harm = harm;
	}

	public void setBuffs(List<IBuff> buffs) {
		this.buffs = buffs;
	}

	@Override
	public boolean isMiss() {
		
		if((null != buffs && buffs.size() > 0) || (null != harm && (!harm.getType().equals(HarmType.SHANBI)))){
			
			return false;
			
		}
		
		
		return true;
	}

	@Override
	public void reboundExecute(IFighter target, IFighter attacker, ISkill skill) {
		
		if(null != harm){
			
			Harm reboundHarm = new Harm(skill, target, attacker, harm.getType(), harm.getVal());
			//反弹伤害执行
			attacker.getFightAttribute().acceptHarm(reboundHarm);
		}
		
		if(!attacker.getStateManager().isDead()){
			
			if(null != harm){
				
				//仇恨
				IFighter hostTarget = attacker;
				IFighter hostAttacker = target;
				
				//仇恨执行
				SkillConfig skillConfig = skill.getSkillConfig();
				if(HarmType.isZhiLiaoHarmType(harm.getType())){
					Integer hatredVal = skill.getSkillHatred().cal1(harm.getVal());
					//目标内部仇恨列表中的元素增加仇恨
					target.getHatredManager().addInsideHatred(hatredVal);
				}else{
					
					//计算仇恨
					Integer hatredVal = skill.getSkillHatred().cal1(harm.getVal());
					
					hostAttacker.getHatredManager().addActiveHatred(hostTarget, 1);
					hostTarget.getHatredManager().addPassiveHatred(hostAttacker, hatredVal);
					
				}
				
			}
			
			//buff执行
			if(null != buffs){
				for(IBuff _buff : buffs){
					
					Buff buff = (Buff)_buff;
					buff.setOwner(attacker);
					buff.setAttackerId(target.getId());
					
					attacker.getBuffManager().addBuff(buff);
				}
			}
		}
		
	}

	@Override
	public IHarm getHarm() {
		return harm;
	}
	

}
