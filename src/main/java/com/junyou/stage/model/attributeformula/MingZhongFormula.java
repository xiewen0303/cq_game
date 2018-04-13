package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 命中几率
 * @author LiuYu
 * @date 2015-1-29 下午2:03:24
 */
public class MingZhongFormula extends AbsAttributeFormula implements IAttributeFormula {

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//x13+x14
		long x13 = destination.getEffectAttribute(EffectType.x13.name());
		long x14 = destination.getEffectAttribute(EffectType.x14.name());
		long x46 = destination.getEffectAttribute(EffectType.x46.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		long mingzhong = x13*(EffectType.getAttBase()+x46)/EffectType.getAttBase()+x14;
		mingzhong = mingzhong*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setMingZhong(mingzhong < 0 ? 0 : mingzhong);
	}
}
