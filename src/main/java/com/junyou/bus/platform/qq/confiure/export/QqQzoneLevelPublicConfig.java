/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;
import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;
/**
 * @Description QQ空间特权等级礼包
 * @Author Yang Gao
 * @Since 2016-7-6
 * @Version 1.1.0
 */
public class QqQzoneLevelPublicConfig extends AdapterPublicConfig {

    private Map<Integer, Map<String, Integer>> levelItem;

    public Map<Integer, Map<String, Integer>> getLevelItem() {
        return levelItem;
    }

    public void setLevelItem(Map<Integer, Map<String, Integer>> levelItem) {
        this.levelItem = levelItem;
    }

}