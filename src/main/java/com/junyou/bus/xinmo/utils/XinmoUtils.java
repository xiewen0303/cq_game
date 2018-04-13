/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.utils;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.xinmo.constants.XinmoConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;

/**
 *@Description 心魔工具类
 *@Author Yang Gao
 *@Since 2016-6-29
 *@Version 1.1.0
 */
public class XinmoUtils {

    /**
     * 心魔是否可以升级 
     * @param need_type
     * @return
     */
    public static boolean canShengji(int need_type){
        return need_type == XinmoConstants.CONSUME_TYPE_SHENGJI;
    }
    
    /**
     * 心魔是否可以突破 
     * @param xinmo_type
     * @return
     */
    public static boolean canTupo(int need_type) {
        return need_type == XinmoConstants.CONSUME_TYPE_TUPO;
    }

    /**
     * 心魔是否可以凝神 
     * @param xinmo_type
     * @return
     */
    public static boolean canNingshen(int need_type){
        return need_type == XinmoConstants.CONSUME_TYPE_NINGSHEN;
    }
    
    /**
     * 检查是否可以操作 
     * @param action_type 操作类型
     * @param need_type 消耗类型
     * @return null=可以操作;不为空=输出errorCode
     */
    public static Object[] isCanCheck(int action_type, int need_type){
        Object[]  checkResult = null;
        switch (action_type) {
        case XinmoConstants.ACTION_TYPE_SHENGJI:
            if(!canShengji(need_type)) checkResult = AppErrorCode.XINMO_CANNOT_SHENGJI;
            break;
        case XinmoConstants.ACTION_TYPE_TUPO:
            if(!canTupo(need_type)) checkResult = AppErrorCode.XINMO_CANNOT_TUPO;
            break;
        case XinmoConstants.ACTION_TYPE_NINGSHEN:
            if(!canNingshen(need_type)) checkResult = AppErrorCode.XINMO_CANNOT_NINGSHEN;
            break;
        default:
            break;
        }
        return checkResult;
    }
    
    /**
     * 检查等级是否满足要求
     * @param action_type 操作类型
     * @param xinmoLevel 玩家心魔等级
     * @param maxLevel 配置最大等级
     * @return
     */
    public static Object[] levelChack(int action_type, int xinmoLevel, int maxLevel){
        Object[]  checkResult = null;
        switch (action_type) {
        case XinmoConstants.ACTION_TYPE_SHENGJI:
            if(xinmoLevel >= maxLevel) checkResult = AppErrorCode.XINMO_HAS_MAX_LEVEL;
            break;
        case XinmoConstants.ACTION_TYPE_TUPO:
        case XinmoConstants.ACTION_TYPE_NINGSHEN:
            if(xinmoLevel != maxLevel) checkResult = AppErrorCode.XINMO_LEVEL_ENOUGH;
            break;
        default:
            break;
        }
        return checkResult;
    }

    /**
     * 获取心魔操作消耗的道具类型 
     * @param action_type
     * @return
     */
    public static Integer getActionCheckGoodsCategory(int action_type){
        Integer category = null;
        switch (action_type) {
        case XinmoConstants.ACTION_TYPE_SHENGJI:
        case XinmoConstants.ACTION_TYPE_TUPO:
            category = GoodsCategory.XINMO_SJ_OR_TUPO_ITEM;
            break;
        case XinmoConstants.ACTION_TYPE_NINGSHEN:
            category = GoodsCategory.XINMO_NINGSHEN_ITEM;
            break;
        default:
            break;
        }
        return category;
    }
    
    /**
     *  获取心魔操作的日志类型
     * @param action_type
     * @return
     */
    public static int getActionConsumeLogPrintHandle(int action_type){
        int logPrintHandle = 0;
        switch (action_type) {
        case XinmoConstants.ACTION_TYPE_SHENGJI:
            logPrintHandle = LogPrintHandle.CONSUME_XINMO_SHENGJI;
            break;
        case XinmoConstants.ACTION_TYPE_TUPO:
            logPrintHandle = LogPrintHandle.CONSUME_XINMO_TUPO;
            break;
        case XinmoConstants.ACTION_TYPE_NINGSHEN:
            logPrintHandle = LogPrintHandle.CONSUME_XINMO_NINGSHEN;
            break;
        default:
            break;
        }
        return logPrintHandle;
    }
    
    /**
     * 获取心魔操作的日志备注类型
     * @param action_type
     * @return
     */
    public static int getActionConsumeBeiZhu(int action_type){
        int beizhu = 0;
        switch (action_type) {
        case XinmoConstants.ACTION_TYPE_SHENGJI:
            beizhu = LogPrintHandle.CBZ_XINMO_SHENGJI;
            break;
        case XinmoConstants.ACTION_TYPE_TUPO:
            beizhu = LogPrintHandle.CBZ_XINMO_TUPO;
            break;
        case XinmoConstants.ACTION_TYPE_NINGSHEN:
            beizhu = LogPrintHandle.CBZ_XINMO_NINGSHEN;
            break;
        default:
            break;
        }
        return beizhu;
    }
    
    /**
     * 获取心魔操作的QQ消费类型
     * @param action_type
     * @return
     */
    public static String getActionQQXiaoFei(int action_type){
        String xiaofei = null;
        switch (action_type) {
        case XinmoConstants.ACTION_TYPE_SHENGJI:
            xiaofei = QQXiaoFeiType.CONSUME_XINMO_SHENGJI;
            break;
        case XinmoConstants.ACTION_TYPE_TUPO:
            xiaofei = QQXiaoFeiType.CONSUME_XINMO_TUPO;
            break;
        case XinmoConstants.ACTION_TYPE_NINGSHEN:
            xiaofei = QQXiaoFeiType.CONSUME_XINMO_NINGSHEN;
            break;
        default:
            break;
        }
        return xiaofei;
    }
    
    /**
     * 获取心魔操作的日志备注类型
     * @param action_type
     * @return
     */
    public static int getActionGoodsSource(int action_type){
        int source = 0;
        switch (action_type) {
        case XinmoConstants.ACTION_TYPE_SHENGJI:
            source = GoodsSource.XINMO_SHENGJI;
            break;
        case XinmoConstants.ACTION_TYPE_TUPO:
            source = GoodsSource.XINMO_TUPO;
            break;
        case XinmoConstants.ACTION_TYPE_NINGSHEN:
            source = GoodsSource.XINMO_NINGSHEN;
            break;
        default:
            break;
        }
        return source;
    }
}
