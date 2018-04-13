/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;
import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;
/**
 * @Description QQ空间特权每日礼包
 * @Author Yang Gao
 * @Since 2016-7-6
 * @Version 1.1.0
 */
public class QqQzoneEveryDayPublicConfig extends AdapterPublicConfig {

    private Map<String, Integer> everyDayItem;

    public Map<String, Integer> getEveryDayItem() {
        return everyDayItem;
    }

    public void setEveryDayItem(Map<String, Integer> everyDayItem) {
        this.everyDayItem = everyDayItem;
    }

}