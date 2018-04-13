/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.stage.model.attributeformula;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.attributeformula.AbsAttributeFormula;
import com.junyou.stage.model.core.attributeformula.IAttributeFormula;


/**
 *@Description
 *           神武装备<铠甲>的被动效果——抵挡<br/>
 *           抵挡：受攻击时一定概率触发完全抵挡一定的伤害
 *@Author Yang Gao
 *@Since 2016-4-15 下午4:36:57
 *@Version 1.1.0
 */
public class DiDangFormula extends AbsAttributeFormula implements IAttributeFormula {

    @Override
    public void refreshAttribute(IFightAttribute destination) {
        long x58 = destination.getEffectAttribute(EffectType.x58.name());
        destination.setDidangRate(x58);
        long x59 = destination.getEffectAttribute(EffectType.x59.name());
        destination.setDidangVal(x59);
        long x60 = destination.getEffectAttribute(EffectType.x60.name());
        destination.setDidangCd(x60);
    }

}
