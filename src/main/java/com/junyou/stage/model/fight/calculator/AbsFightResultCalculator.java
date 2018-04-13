package com.junyou.stage.model.fight.calculator;

import java.util.ArrayList;
import java.util.List;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.RoleBasePublicConfig;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.SkillEffectConfig;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightResult;
import com.junyou.stage.model.core.fight.IFightResultCalculator;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.fight.FightResult;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.skill.harm.Harm;
import com.junyou.stage.model.skill.harm.HarmType;
import com.junyou.stage.model.skill.harm.HarmUtils;
import com.junyou.stage.model.skill.harmcalculator.MingZhong;
import com.junyou.utils.lottery.Lottery;

public abstract class AbsFightResultCalculator implements IFightResultCalculator {

	/**
	 * 
	 */
	public static final float BAOJI_BEILV = 1.5f;
	public static final float GEDANG_BEILV = 0.5f;
	
	@Override
	public IFightResult calculate(ISkill skill, IFighter attacker,
			IFighter target) {
		FightResult result = new FightResult();
		IHarm harm = null;	
		
		
		if (FightResultCalculatorType.SHANGHAI.equals(skill.getDamageType()) && !checkMingZhong(skill,attacker,target)) {
			List<IBuff> buffs = calculateSkillBuffs(skill,attacker,target);
			result.setBuffs(buffs);
			
			harm = getMissHarm(skill,attacker,target);
			
		}else {
			//技能buff
			List<IBuff> buffs = calculateSkillBuffs(skill,attacker,target);
			
			if(!ElementType.isBiaoChe(target.getElementType())){
				//攻击者释放麻痹
				IBuff mbBuff = attackerMaBeiBuff(attacker, target);
				if(mbBuff != null){
					if(buffs == null){
						buffs = new ArrayList<IBuff>();
					}
					buffs.add(mbBuff);
				}
			}
			
			// target被攻击者是角色情况下判断触发被动buff
		   IBuff fyBuff = targetFangyuBuff(attacker, target);
		   if(null != fyBuff){
		       if(null == buffs){
		           buffs = new ArrayList<IBuff>();
		       }
		       buffs.add(fyBuff);
		   }
			
			result.setBuffs(buffs);
			
			//伤害计算
			harm = harmCalculate(skill,attacker,target);
		}
		result.setHarm(harm);
		
		return result;
	}


    /**
     * 被攻击玩家触发被动防御buff
     * 
     * @param target
     * @return
     */
    private IBuff targetFangyuBuff(IFighter attacker, IFighter target) {
        IBuff fyBuff = null;
        if (ElementType.isRole(target.getElementType())) {
            // 配置表填的是放大1W倍的,拿到之后先放小1W倍
            float fyRate = target.getFightAttribute().getEffectAttribute(EffectType.x55.name()) / 10000F;
            if (fyRate > 0 && Lottery.roll(fyRate, Lottery.TENTHOUSAND)) {
                RoleBasePublicConfig roleBasePublicConfig = StageConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_ROLE_BASE);
                long fyVal = target.getFightAttribute().getEffectAttribute(EffectType.x54.name());
                int fySecond = (int) target.getFightAttribute().getEffectAttribute(EffectType.x56.name()) * 1000;// 单位：毫秒
                fyBuff = BuffFactory.create(target, attacker, roleBasePublicConfig.getZiqibuff(), fyVal, fySecond);
            }
        }
        return fyBuff;
    }


    /**
	 * 攻击者释放麻痹
	 * @param attacker
	 * @param target
	 */
	private IBuff attackerMaBeiBuff(IFighter attacker,IFighter target){
		IBuff mbBuff = null;
		
		if(ElementType.isRole(attacker.getElementType())){
			//过滤自己
			if(HarmUtils.isSelfFigher(attacker, target)){
				return null;
			}
			
			float mbJiLv = attacker.getFightAttribute().getEffectAttribute(EffectType.x29.name());
			float mbSecond = attacker.getFightAttribute().getEffectAttribute(EffectType.x31.name());
			float x30 = target.getFightAttribute().getEffectAttribute(EffectType.x30.name());
			
			RoleBasePublicConfig roleBasePublicConfig = StageConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_ROLE_BASE);
			//攻击者有x23*(1-x30)的概率麻痹被攻击者
			mbJiLv = mbJiLv / 10000;//配置表填的是放大1W倍的,拿到之后先放小1W倍
			x30 = x30 / 10000;//配置表填的是放大1W倍的,拿到之后先放小1W倍
			//mbJiLv = mbJiLv * (1 - x30);
			mbJiLv = mbJiLv - x30;
			if(mbJiLv > 0 && Lottery.roll(mbJiLv, Lottery.TENTHOUSAND)){
				int duration = (int) (mbSecond * 1000);
				
				mbBuff = BuffFactory.create(target, attacker, roleBasePublicConfig.getMabiBuff(),duration);
			}
		}
		
		return mbBuff;
	}
	
	
	/**
	 * miss伤害
	 */
	protected IHarm getMissHarm(ISkill skill, IFighter attacker, IFighter target) {
		return new Harm(skill,attacker,target, HarmType.SHANBI, 0);
	}

	/**
	 * 计算技能是否命中
	 * @return true:命中 false:未命中
	 */
	private boolean checkMingZhong(ISkill skill, IFighter attacker, IFighter target) {
		if(skill.isNoCalcMZ()){
			return true;
		}else{
			return MingZhong.isMingZhong(attacker, skill, target);
		}
	}

	/**
	 * @description 伤害计算
	 */
	abstract protected IHarm harmCalculate(ISkill skill, IFighter attacker, IFighter target);


	/**
	 * 计算技能附带的可命中目标的buff集合
	 * 包括buff自身的命中计算和目标技能抵抗计算
	 */
	protected List<IBuff> calculateSkillBuffs(ISkill skill, IFighter attacker, IFighter target) {
		
		List<IBuff> result = null;
		
		List<SkillEffectConfig> skillEffectConfigs  = skill.getSkillXueXiConfig().getEffectConfigs();
		if(null != skillEffectConfigs){
			
			for(SkillEffectConfig effectConfig : skillEffectConfigs){
				
				//技能中BUFF几率 
				if(!Lottery.roll(effectConfig.getRate(), Lottery.HUNDRED)){
					continue;
				}
				
				String buffId = effectConfig.getAddeffect();				
				
				IBuff buff = BuffFactory.create(target,attacker,skill,buffId);
				if(buff == null){
					continue;
				}
				if(null == result){
					result = new ArrayList<IBuff>();
				}
				result.add(buff);
				
//				if(BuffMingZhong.isMingZhong(attacker, buff, target)){					
//					if(null == result){
//						result = new ArrayList<IBuff>();
//					}
//					result.add(buff);
//				}
				
			}
			
		}
		
		return result;
		
	}

}
