/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.Map;

/**
 * @Description 心魔系统:<天炉炼丹>配置信息
 * @Author Yang Gao
 * @Since 2016-7-13
 * @Version 1.1.0
 */
public class TianLuLianDanBiaoConfig {

    /* 丹炉编号 */
    private int id;
    /* 升级丹炉需要花费的元宝 */
    private int needGold;
    /* 升级丹炉赠送的丹药仓库格位数 */
    private int giveSolt;
    /* 丹炉产出丹药的时间 */
    private int timing;
    /* 丹炉产出丹药的物品信息key=编号:value=概率 */
    private Map<String, Integer> itemConfig;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNeedGold() {
        return needGold;
    }

    public void setNeedGold(int needGold) {
        this.needGold = needGold;
    }

    public int getGiveSolt() {
        return giveSolt;
    }

    public void setGiveSolt(int giveSolt) {
        this.giveSolt = giveSolt;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }

    public Map<String, Integer> getItemConfig() {
        return itemConfig;
    }

    public void setItemConfig(Map<String, Integer> itemConfig) {
        this.itemConfig = itemConfig;
    }

}
