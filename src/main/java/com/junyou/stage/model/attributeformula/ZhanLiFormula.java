package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 战斗力值
 *@author  LiNing
 *@created 2013-10-14下午7:18:22
 */
public class ZhanLiFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//ZhanLi
		long zhanLi = destination.getEffectAttribute(EffectType.zplus.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		zhanLi = zhanLi*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		destination.setZhanLi(zhanLi < 0 ? 0 : zhanLi);
	}
}