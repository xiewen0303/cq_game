package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 增伤
 * @author LiuYu
 * @date 2015-3-19 上午11:05:25
 */
public class ZengShangFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		long value = destination.getEffectAttribute(EffectType.x26.name());
		destination.setZengShang(value);
	}

}
