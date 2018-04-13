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
 *            五行属性计算
 *@Author Yang Gao
 *@Since 2016-4-15 下午4:37:21
 *@Version 1.1.0
 */
public class WuXingFormula extends AbsAttributeFormula implements IAttributeFormula {

    @Override
    public void refreshAttribute(IFightAttribute destination) {
        long x64 = destination.getEffectAttribute(EffectType.x64.name());
        destination.setWxFireAddRate(x64);
        long x65 = destination.getEffectAttribute(EffectType.x65.name());
        destination.setWxGoldAddRate(x65);
        long x66 = destination.getEffectAttribute(EffectType.x66.name());
        destination.setWxWoodAddRate(x66);
        long x67 = destination.getEffectAttribute(EffectType.x67.name());
        destination.setWxEarthAddRate(x67);
        long x68 = destination.getEffectAttribute(EffectType.x68.name());
        destination.setWxWaterAddRate(x68);
        
        long x69 = destination.getEffectAttribute(EffectType.x69.name());
        destination.setWxGoldSubRate(x69);
        long x70 = destination.getEffectAttribute(EffectType.x70.name());
        destination.setWxWoodSubRate(x70);
        long x71 = destination.getEffectAttribute(EffectType.x71.name());
        destination.setWxEarthSubRate(x71);
        long x72 = destination.getEffectAttribute(EffectType.x72.name());
        destination.setWxWaterSubRate(x72);
        long x73 = destination.getEffectAttribute(EffectType.x73.name());
        destination.setWxFireSubRate(x73);
    }

}
