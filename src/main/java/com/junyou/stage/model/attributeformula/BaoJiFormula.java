package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 暴击几率
 * @author LiuYu
 * @date 2015-1-29 下午2:06:11
 */
public class BaoJiFormula extends AbsAttributeFormula implements IAttributeFormula {

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//x15+x16
		long x15 = destination.getEffectAttribute(EffectType.x15.name());
		long x16 = destination.getEffectAttribute(EffectType.x16.name());
		long x44 = destination.getEffectAttribute(EffectType.x44.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		long baoji = x15*(EffectType.getAttBase()+x44)/EffectType.getAttBase()+x16;
		baoji = baoji*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setBaoJi(baoji < 0 ? 0 : baoji);
	}
}