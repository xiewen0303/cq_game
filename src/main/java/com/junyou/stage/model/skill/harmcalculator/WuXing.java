/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.stage.model.skill.harmcalculator;

import com.junyou.constants.GameConstants;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.monster.WuxingFubenMonster;
import com.junyou.stage.model.element.role.IRole;


/**
 * 
 *@Description 
 *            五行属性伤害计算处理
 *@Author Yang Gao
 *@Since 2016-4-15 下午4:09:46
 *@Version 1.1.0
 */
public class WuXing {

    /**
     * @Description 计算战斗攻击方对防御者的五行属性比例值
     * @param attacker 攻击者
     * @param target  防御者
     * @return 
     *  0=无任何属性加成</br>
     *  正数=攻击加成百分比</br>
     *  负数=攻击减少百分比</br>
     */
    public static float getWuXingRate(IFighter attacker, IFighter target) {
        int attackerWuXing = 0;// 攻击者五行属性类型
        if(ElementType.isRole(attacker.getElementType())){
            IRole role = (IRole)attacker;
            Integer attkWxType = role.getBusinessData().getWuxingType();
            attackerWuXing = null == attkWxType ? 0 : attkWxType.intValue();
        }
        else if(ElementType.isMonster(attacker.getElementType()) && attacker instanceof WuxingFubenMonster){
            WuxingFubenMonster monster = (WuxingFubenMonster)attacker;
            Integer attkWxType = monster.getMonsterWxType();
            attackerWuXing = null == attkWxType ? 0 : attkWxType.intValue();
        }
        
        int targetWuXing = 0;// 防御者五行属性类型
        if(ElementType.isRole(target.getElementType())){
            IRole role = (IRole)target;
            Integer targetWxType = role.getBusinessData().getWuxingType();
            targetWuXing = null == targetWxType ? 0 : targetWxType.intValue();
        }
        else if(ElementType.isMonster(target.getElementType()) && target instanceof WuxingFubenMonster){
            WuxingFubenMonster monster = (WuxingFubenMonster)target;
            Integer targetWxType = monster.getMonsterWxType();
            targetWuXing = null == targetWxType ? 0 : targetWxType.intValue();
        }
        
        if(0 == attackerWuXing  && 0 == targetWuXing){// 战斗双方均没有五行属性
            return 0;
        }
        
        long wuXingVal = 0L;// 五行属性加成值
        IFightAttribute atkFightAttribute = attacker.getFightAttribute();
        IFightAttribute targetFightAttribute = target.getFightAttribute();
        
        switch (attackerWuXing) {
        case GameConstants.WUXING_GOLD:
            if (0 == targetWuXing || GameConstants.WUXING_WOOD == targetWuXing) {
                wuXingVal = atkFightAttribute.getWxGoldAddRate();
            } else if (GameConstants.WUXING_FIRE == targetWuXing) {
                wuXingVal = -(targetFightAttribute.getWxGoldSubRate());
            }
            break;
        case GameConstants.WUXING_WOOD:
            if (0 == targetWuXing || GameConstants.WUXING_EARTH == targetWuXing) {
                wuXingVal = atkFightAttribute.getWxWoodAddRate();
            } else if (GameConstants.WUXING_GOLD == targetWuXing) {
                wuXingVal = -(targetFightAttribute.getWxWoodSubRate());
            }
            break;
        case GameConstants.WUXING_WATER:
            if (0 == targetWuXing || GameConstants.WUXING_FIRE == targetWuXing) {
                wuXingVal = atkFightAttribute.getWxWaterAddRate();
            } else if (GameConstants.WUXING_EARTH == targetWuXing) {
                wuXingVal = -(targetFightAttribute.getWxWaterSubRate());
            }
            break;
        case GameConstants.WUXING_FIRE:
            if (0 == targetWuXing || GameConstants.WUXING_GOLD == targetWuXing) {
                wuXingVal = atkFightAttribute.getWxFireAddRate();
            } else if (GameConstants.WUXING_WATER == targetWuXing) {
                wuXingVal = -(targetFightAttribute.getWxFireSubRate());
            }
            break;
        case GameConstants.WUXING_EARTH:
            if (0 == targetWuXing || GameConstants.WUXING_WATER == targetWuXing) {
                wuXingVal = atkFightAttribute.getWxEarthAddRate();
            } else if (GameConstants.WUXING_WOOD == targetWuXing) {
                wuXingVal = -(targetFightAttribute.getWxEarthSubRate());
            }
            break;
        default:// 攻击者无属性
            switch (targetWuXing) {
            case GameConstants.WUXING_GOLD:
                wuXingVal = -(targetFightAttribute.getWxWoodSubRate());
                break;
            case GameConstants.WUXING_WOOD:
                wuXingVal = -(targetFightAttribute.getWxEarthSubRate());
                break;
            case GameConstants.WUXING_WATER:
                wuXingVal = -(targetFightAttribute.getWxFireSubRate());
                break;
            case GameConstants.WUXING_FIRE:
                wuXingVal = -(targetFightAttribute.getWxGoldSubRate());
                break;
            case GameConstants.WUXING_EARTH:
                wuXingVal = -(targetFightAttribute.getWxWaterSubRate());
                break;
            default:
                break;
            }
            break;
        }
        return wuXingVal / 10000F;
    }
}
