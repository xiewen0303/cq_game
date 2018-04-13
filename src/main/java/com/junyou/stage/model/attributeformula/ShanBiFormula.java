package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 闪避几率
 * @author LiuYu
 * @date 2015-1-29 下午2:07:38
 */
public class ShanBiFormula extends AbsAttributeFormula implements IAttributeFormula {

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//x10+x11
		long x10 = destination.getEffectAttribute(EffectType.x10.name());
		long x11 = destination.getEffectAttribute(EffectType.x11.name());
		long x45 = destination.getEffectAttribute(EffectType.x45.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		long shanbi = x10*(EffectType.getAttBase()+x45)/EffectType.getAttBase()+x11;
		shanbi = shanbi*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setShanBi(shanbi < 0 ? 0 : shanbi);
	}

}
