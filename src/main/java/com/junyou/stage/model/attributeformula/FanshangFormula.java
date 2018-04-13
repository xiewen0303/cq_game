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
 *            神武装备<头盔>的被动效果——反伤<br/>
 *            反伤：受到攻击时一定概率触发对攻击者造成一定的伤害
 *@Author Yang Gao
 *@Since 2016-4-15 下午4:37:21
 *@Version 1.1.0
 */
public class FanshangFormula extends AbsAttributeFormula implements IAttributeFormula {

    @Override
    public void refreshAttribute(IFightAttribute destination) {
        long x62 = destination.getEffectAttribute(EffectType.x62.name());
        destination.setFanshangRate(x62);
        long x61 = destination.getEffectAttribute(EffectType.x61.name());
        destination.setFanshangVal(x61);
        long x63 = destination.getEffectAttribute(EffectType.x63.name());
        destination.setFanshangCd(x63);
        long x74 = destination.getEffectAttribute(EffectType.x74.name());
        destination.setKangFanshangRate(x74);
    }

}
