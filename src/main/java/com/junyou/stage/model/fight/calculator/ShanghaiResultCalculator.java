package com.junyou.stage.model.fight.calculator;


import com.junyou.bus.lianyuboss.stage.LianyuBossStage;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightResultCalculator;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.skill.harm.Harm;
import com.junyou.stage.model.skill.harm.HarmType;
import com.junyou.stage.model.skill.harmcalculator.BaoJi;
import com.junyou.stage.model.skill.harmcalculator.DiDang;
import com.junyou.stage.model.skill.harmcalculator.FanShang;
import com.junyou.stage.model.skill.harmcalculator.WuXing;
import com.junyou.utils.lottery.MathUtils;

/**
 * 基础伤害计算结果 
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-27 下午6:32:36
 */
public class ShanghaiResultCalculator extends AbsFightResultCalculator implements IFightResultCalculator {

	@Override
	protected IHarm harmCalculate(ISkill skill, IFighter attacker,
			IFighter target) {
		IHarm harm = null;
		if(ElementType.isBiaoChe(target.getElementType())){
			harm = harmFigterBiaoCheCalculate(skill, attacker, target);
		}else if(ElementType.isPet(target.getElementType())){
			harm = harmFigterTangBaoCalculate(skill, attacker, target);
		}else{
			harm = harmFighterCalculate(skill, attacker, target);
		}
		return harm;
	}
	/**
	 * 糖宝伤害
	 * @param skill
	 * @param attacker
	 * @param target
	 * @return
	 */
	private IHarm harmFigterTangBaoCalculate(ISkill skill, IFighter attacker, IFighter target){
		
		HarmType harmType = HarmType.PUTONG;
		Integer harmTypeValue = HarmType.PUTONG.getVal();
		
		Pet pet = (Pet) target;
		
		IHarm harm = new Harm(skill, attacker, target, harmType, pet.getHarm());
		harm.setHarmTypeValue(harmTypeValue);
		
		return harm;
	}
	/**
	 * 镖车伤害
	 * @param skill
	 * @param attacker
	 * @param target
	 * @return
	 */
	private IHarm harmFigterBiaoCheCalculate(ISkill skill, IFighter attacker, IFighter target){
		
		HarmType harmType = HarmType.PUTONG;
		Integer harmTypeValue = HarmType.PUTONG.getVal();
		
		Biaoche biaoChe = (Biaoche) target;
		
		int damage = biaoChe.getHarm();

		IHarm harm = new Harm(skill, attacker, target, harmType, damage);
		harm.setHarmTypeValue(harmTypeValue);

        biaoChe.setBiaoceState(2);
        biaoChe.setLastHurtTime(System.currentTimeMillis());

		return harm;
	}
	
	/**
	 * 公式
	 * @param skill
	 * @param attacker
	 * @param target
	 * @return
	 */
 private IHarm harmFighterCalculate(ISkill skill, IFighter attacker, IFighter target) {
     float rate = 1;
     boolean attackerIsLianyuMonster = false; // 攻击者是不是门派炼狱怪物
     if (attacker.getStage() instanceof LianyuBossStage) {
         LianyuBossStage tStage = (LianyuBossStage) attacker.getStage();
         if (ElementType.isMonster(attacker.getElementType())) {
             attackerIsLianyuMonster = true;
         }
         rate = tStage.getRate();
     }
     HarmType harmType = HarmType.PUTONG;
     Integer harmTypeValue = HarmType.PUTONG.getVal();

     IFightAttribute atkFightAttribute = attacker.getFightAttribute();
     IFightAttribute targetFightAttribute = target.getFightAttribute();

     /**
      * （技能系数*max（攻击方攻击-防御方防御*（10000-攻击方无视防御%）/10000，攻击方攻击*0.2）+技能伤害加成）*(1+自己伤害加成%-对方伤害减免%)+(自己伤害增加-对方伤害减免)
      */
     long att = atkFightAttribute.getAttack();
     long def = targetFightAttribute.getDefense();
     long wushiDef = atkFightAttribute.getNoDef();
     long baojiRate = atkFightAttribute.getBaoJi2();
     long zengshang = atkFightAttribute.getZengShang();
     long jianshang = targetFightAttribute.getJianShang();
     long shangAdd = atkFightAttribute.getShangAdd();
     long shangDes = atkFightAttribute.getShangDef();
     long chengfa = atkFightAttribute.getChengfa();

     att = (long) (att * (attackerIsLianyuMonster ? rate : 1));
     def = (long) (def * (attackerIsLianyuMonster ? 1 : rate));

     // 计算生效防御值
     if (skill.isNoDef()) {
         def = 0;
     } else {
         def = def * (EffectType.getAttBase() - wushiDef) / EffectType.getAttBase();
     }
     // 计算基础伤害值
     Float damage = MathUtils.max(att - def, att * 2 / 10) * skill.getSkillXueXiConfig().getFormulea() + skill.getSkillXueXiConfig().getFormuleb();
     // 计算最终伤害值
     damage = damage * (EffectType.getAttBase() + zengshang - jianshang) / EffectType.getAttBase();
     damage = damage * (EffectType.getAttBase() - chengfa) / EffectType.getAttBase();
     damage = damage + shangAdd - shangDes;

     long maxHp = targetFightAttribute.getMaxHp();
     maxHp = (long) (maxHp * (attackerIsLianyuMonster ? 1 : rate));
     damage += maxHp * skill.getSkillXueXiConfig().getFormulec() / EffectType.getAttBase();
     
     // 计算五行属性加成
     damage *= (1 + WuXing.getWuXingRate(attacker, target));
     
     // 是否暴击判断
     if (BaoJi.isBaoji(attacker, skill, target)) {
         harmType = HarmType.BAOJI;
         harmTypeValue |= HarmType.BAOJI.getVal();
         // 暴击最终伤害
         damage = damage * (EffectType.getAttBase() + baojiRate) / EffectType.getAttBase();
     }
     
     // 获取真实攻击方
     IFighter aFighter = attacker;
     if (ElementType.isPet(attacker.getElementType())) {
         Pet pet = (Pet) attacker;
         aFighter = pet.getOwner();
     }
     
     // 防御方触发神武装备的反击效果
     long fsVal = 0L;
     if (FanShang.checkFanShang(attacker,target)) {
         harmType = HarmType.FANJI;
         harmTypeValue |= HarmType.FANJI.getVal();

         // 反伤真实值
         float fsRateVal = targetFightAttribute.getFanshangVal() / 10000F;
         fsVal = Math.round(damage * fsRateVal);
         long fsMaxVal = targetFightAttribute.getMaxHp();
         fsVal = fsVal > fsMaxVal ? fsMaxVal : fsVal;

         // 后台同步防御者对攻击者的反击伤害值
         IHarm fsHarm = new Harm(skill, aFighter, target, HarmType.PUTONG, MathUtils.max(fsVal, 1));
         atkFightAttribute.hurt(fsHarm);
     }

     // 防御方触发神武装备的抵挡效果
     long ddVal = 0;
     if(DiDang.checkDiDang(target)){
         harmType = HarmType.DIDANG;
         harmTypeValue |= HarmType.DIDANG.getVal();

         // 抵伤真实值
         float dsRateVal = targetFightAttribute.getDidangVal() / 10000F;
         ddVal = Math.round(damage * dsRateVal);

         damage -= ddVal;
     }

     IHarm harm = new Harm(skill, aFighter, target, harmType, MathUtils.max(damage.longValue(), 1), ddVal, fsVal);
     harm.setHarmTypeValue(harmTypeValue);
     return harm;
 }


 
 
}