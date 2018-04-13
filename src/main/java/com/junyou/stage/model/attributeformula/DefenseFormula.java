package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 防御
 * @author LiuYu
 * @date 2015-1-29 下午2:05:29
 */
public class DefenseFormula  extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//x7+x8
		long x7 = destination.getEffectAttribute(EffectType.x7.name());
		long x8 = destination.getEffectAttribute(EffectType.x8.name());
		long x43 = destination.getEffectAttribute(EffectType.x43.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		long x50 = destination.getEffectAttribute(EffectType.x50.name());
		long def = x7*(EffectType.getAttBase()+x43)/EffectType.getAttBase()+x8;
		def = def*(EffectType.getAttBase()-x50)/EffectType.getAttBase();
		def = def*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setDefense(def < 0 ? 0 : def);
	}
}
