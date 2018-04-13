package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 无视防御
 * @author LiuYu
 * @date 2015-3-19 上午11:01:26
 */
public class NoDefFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		long x9 = destination.getEffectAttribute(EffectType.x9.name());
		destination.setNoDef(x9);
	}

}
