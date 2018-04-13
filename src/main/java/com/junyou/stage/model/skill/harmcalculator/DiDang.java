/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.stage.model.skill.harmcalculator;

import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.utils.lottery.Lottery;


/**
 * 
 *@Description 
 *            神武装备<铠甲>的低档效果处理
 *@Author Yang Gao
 *@Since 2016-4-15 下午4:09:46
 *@Version 1.1.0
 */
public class DiDang {

    /**
     * @Description 检查是否触发抵挡效果
     * @param target 触发对象
     * @return true=触发; false=未触发
     */
    public static boolean checkDiDang(IFighter target) {
        boolean flag = false;
        if (null != target) {
            if (ElementType.isRole(target.getElementType())) {
                IFightAttribute targetFightAttribute = target.getFightAttribute();
                float ddRate = targetFightAttribute.getDidangRate();
                if (ddRate > 0 && Lottery.roll(ddRate, Lottery.TENTHOUSAND)) {
                    long ddCd = targetFightAttribute.getDidangCd() * 1000;
                    long nowTime = System.currentTimeMillis();
                    long ddTime = targetFightAttribute.getDidangTime();
                    if (ddTime <= 0 || (ddCd > 0 && (nowTime - ddTime) > ddCd)) {
                        targetFightAttribute.setDidangTime(nowTime);
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
}
