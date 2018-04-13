/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.jueban.configure;

import java.util.Map;

/**
 * @Description 热发布-绝版礼包活动配置
 * @Author Yang Gao
 * @Since 2016-8-1
 * @Version 1.1.0
 */
public class RefabuJuebanConfig {

    /* 编号 */
    private int id;
    /* 背景资源 */
    private String res;
    /* 花费元宝 */
    private int needGold;
    /* 购买道具集合 */
    private Map<String, Integer> itemMap;
    /* 道具字符展示数据 */
    private String itemString;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public int getNeedGold() {
        return needGold;
    }

    public void setNeedGold(int needGold) {
        this.needGold = needGold;
    }

    public Map<String, Integer> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, Integer> itemMap) {
        this.itemMap = itemMap;
    }

    public String getItemString() {
        return itemString;
    }

    public void setItemString(String itemString) {
        this.itemString = itemString;
    }

}
