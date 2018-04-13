package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 抗暴几率(韧性)
 * @author LiuYu
 * @date 2015-1-29 下午2:05:55
 */
public class KangBaoFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//x17+x18
		long x17 = destination.getEffectAttribute(EffectType.x17.name());
		long x18 = destination.getEffectAttribute(EffectType.x18.name());
		long x47 = destination.getEffectAttribute(EffectType.x47.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		long renxing = x17*(EffectType.getAttBase()+x47)/EffectType.getAttBase()+x18;
		renxing = renxing*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setKangBao(renxing < 0 ? 0 : renxing);
	}
}
