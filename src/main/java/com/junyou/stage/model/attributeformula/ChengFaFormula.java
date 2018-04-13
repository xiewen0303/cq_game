package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * PK惩罚
 * @author LiuYu
 * @date 2015-6-12 下午7:36:02
 */
public class ChengFaFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//ZhanLi
		long x48 = destination.getEffectAttribute(EffectType.x48.name());
		destination.setChengfa(x48);
	}
}