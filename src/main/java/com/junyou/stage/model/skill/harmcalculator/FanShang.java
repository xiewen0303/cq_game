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
 *@Description
 *           神武装备<头盔>的反伤效果处理
 *@Author Yang Gao
 *@Since 2016-4-15 下午4:43:55
 *@Version 1.1.0
 */
public class FanShang {

    /**
     * @Description 检查是否触发反伤效果
     * @param target 触发对象
     * @return true=触发; false=未触发
     */
    public static boolean checkFanShang(IFighter attacker,IFighter target) {
        boolean flag = false;
        if (null != target) {
            if (ElementType.isRole(target.getElementType())) {
                IFightAttribute targetFightAttribute = target.getFightAttribute();
                float fsRate = targetFightAttribute.getFanshangRate() / 10000F;
                float kfs=  0;
                if(ElementType.isRole(attacker.getElementType())){
                	kfs = attacker.getFightAttribute().getKangFanshangRate() / 10000F;
                }
                fsRate = fsRate - kfs;
                if (fsRate > 0 && Lottery.roll(fsRate, Lottery.TENTHOUSAND)) {
                    long fsCd = targetFightAttribute.getFanshangCd() * 1000;
                    long nowTime = System.currentTimeMillis();
                    long fsTime = targetFightAttribute.getFanshangTime();
                    if (fsTime <= 0 || (fsCd > 0 && (nowTime - fsTime) > fsCd)) {
                        targetFightAttribute.setFanshangTime(nowTime);
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
}
