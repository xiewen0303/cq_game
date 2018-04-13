package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 生命
 * @author LiuYu
 * @date 2015-1-29 上午11:55:56
 */
public class MaxHpFormula extends AbsAttributeFormula implements IAttributeFormula {

	@Override
	public void refreshAttribute(IFightAttribute destination) {
		//x1*(10000+x3)/10000+x2
		long x1 = destination.getEffectAttribute(EffectType.x1.name());
		long x2 = destination.getEffectAttribute(EffectType.x2.name());
		long x3 = destination.getEffectAttribute(EffectType.x3.name());
		long x42 = destination.getEffectAttribute(EffectType.x42.name());
		
		long maxHp = x1 * (EffectType.getAttBase() + x3)/EffectType.getAttBase() + x2;
		if (maxHp < 1) {
			maxHp = 1l;
		}
		maxHp = maxHp*(EffectType.getAttBase()+x42)/EffectType.getAttBase();
		
		destination.setMaxHp(maxHp);
		
		long curHp = destination.getCurHp();
		if(curHp > maxHp){
			curHp = maxHp;
		}
		
		destination.setCurHp(curHp);		
	}

}
