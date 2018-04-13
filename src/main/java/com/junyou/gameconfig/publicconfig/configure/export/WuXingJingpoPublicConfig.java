/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * @Description 五行魔神精魄公共数据配置信息
 * @Author Yang Gao
 * @Since 2016-5-12
 * @Version 1.1.0
 */
public class WuXingJingpoPublicConfig extends AdapterPublicConfig {

    /* 窥探天机需要元宝 */
    private int needGold;
    /* 一键猎魄次数 */
    private int autoCount;
    /* 窥探天机达到的等级 */
    private int kttjLevel;

    public int getNeedGold() {
        return needGold;
    }

    public void setNeedGold(int needGold) {
        this.needGold = needGold;
    }

    public int getAutoCount() {
        return autoCount;
    }

    public void setAutoCount(int autoCount) {
        this.autoCount = autoCount;
    }

    public int getKttjLevel() {
        return kttjLevel;
    }

    public void setKttjLevel(int kttjLevel) {
        this.kttjLevel = kttjLevel;
    }

}
