package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 攻击
 * @author LiuYu
 * @date 2015-1-29 上午9:59:11
 */
public class AttackFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//x4*(10000+x6)/10000+x5
		long x4 = destination.getEffectAttribute(EffectType.x4.name());
		long x5 = destination.getEffectAttribute(EffectType.x5.name());
		long x6 = destination.getEffectAttribute(EffectType.x6.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		long x49 = destination.getEffectAttribute(EffectType.x49.name());
		long att = x4*(EffectType.getAttBase()+x6)/EffectType.getAttBase()+x5;
		att = att*(EffectType.getAttBase()-x49)/EffectType.getAttBase();
		att = att*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setAttack(att < 0 ? 0 : att);
	}
}
