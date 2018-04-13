package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 暴击倍率
 * @author LiuYu
 * @date 2015-7-11 下午8:09:06
 */
public class BaoJi2Formula extends AbsAttributeFormula implements IAttributeFormula {

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		long x28 = destination.getEffectAttribute(EffectType.x28.name());
		destination.setBaoJi2(x28);
	}
}