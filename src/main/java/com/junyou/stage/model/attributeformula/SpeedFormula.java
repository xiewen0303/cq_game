package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;

/**
 * 移动速度
 */
public class SpeedFormula extends AbsAttributeFormula implements IAttributeFormula{

	@Override
	public void refreshAttribute(IFightAttribute destination) {

		//x19+x20
		long x19 = destination.getEffectAttribute(EffectType.x19.name());
		long x20 = destination.getEffectAttribute(EffectType.x20.name());
		long x51 = destination.getEffectAttribute(EffectType.x51.name());
		long speed = x19 + x20;
		speed = speed*(EffectType.getAttBase()-x51)/EffectType.getAttBase();

		long x80 = destination.getEffectAttribute(EffectType.x80.name());
		if(x80 != 0){
			speed = x80;
		}else{
			speed = speed < 0 ? 0 : speed;
		}
		destination.setSpeed(speed);
	}

}
