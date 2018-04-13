/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.Map;

/**
 * @Description 心魔系统:<凝神培元基础信息>
 * @Author Yang Gao
 * @Since 2016-6-27
 * @Version 1.1.0
 */
public class NingShenJiChuBiaoConfig {
    /* 编号id */
    private int id;
    /* 境界阶段 */
    private int category;
    /* 境界时期 */
    private int type;
    /* 境界等级 */
    private int level;
    /* 消耗银两 */
    private int money;
    /* 消耗道具大类ID */
    private String prop;
    /* 消耗道具大类数量 */
    private int count;
    /* 需要的条件类型 */
    private int needType;
    /* 需要的条件数值 */
    private int needVal;
    /* 消耗道具获得数值 */
    private int addVal;
    /* 公告code码 */
    private int ggCode;
    /* 道具消耗元宝价格 */
    private int gold;
    /* 道具消耗绑定元宝价格 */
    private int bgold;
    /* 心魔系统属性加成 */
    private Map<String, Long> xmAttrsMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAddVal() {
        return addVal;
    }

    public void setAddVal(int addVal) {
        this.addVal = addVal;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getBgold() {
        return bgold;
    }

    public void setBgold(int bgold) {
        this.bgold = bgold;
    }

    public int getNeedType() {
        return needType;
    }

    public void setNeedType(int needType) {
        this.needType = needType;
    }

    public int getNeedVal() {
        return needVal;
    }

    public void setNeedVal(int needVal) {
        this.needVal = needVal;
    }

    public Map<String, Long> getXmAttrsMap() {
        return xmAttrsMap;
    }

    public void setXmAttrsMap(Map<String, Long> xmAttrsMap) {
        this.xmAttrsMap = xmAttrsMap;
    }

    public int getGgCode() {
        return ggCode;
    }

    public void setGgCode(int ggCode) {
        this.ggCode = ggCode;
    }

}
