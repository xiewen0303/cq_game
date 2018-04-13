/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;
import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 * @Description QQ游戏大厅特权新手礼包
 * @Author Yang Gao
 * @Since 2016-7-6
 * @Version 1.1.0
 */
public class QqGameXinShouPublicConfig extends AdapterPublicConfig {
    private Map<String, Integer> xinShouItem;

    public Map<String, Integer> getXinShouItem() {
        return xinShouItem;
    }

    public void setXinShouItem(Map<String, Integer> xinShouItem) {
        this.xinShouItem = xinShouItem;
    }
}
