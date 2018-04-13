/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.bag.service.useprop.rule;

import com.junyou.bus.bag.service.useprop.IUsePropCallBack;

/**
 * 
 *@Description 添加魔宫烈焰精华道具
 *@Author Yang Gao
 *@Since 2016-10-28
 *@Version 1.1.0
 */
public class AddMglyJinghuaCheckRule implements IUsePropCheckRule {

    @Override
    public void propHandle(IUsePropCallBack callback) {
        callback.addMglyJinghuaHandler();
    }

}
