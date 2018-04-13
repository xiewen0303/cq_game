package com.junyou.bus.chongwu.configure.export;

import java.util.Map;

/**
 * 
 * @Description 宠物技能配置
 * @Author Yang Gao
 * @Since 2016-8-29
 * @Version 1.1.0
 */
public class ChongWuJiNengBiaoConfig {
    /* 技能id标识 */
    private String id;
    /* 技能等级 */
    private int level;
    /* 消耗的银两 */
    private int needMoney;
    /* 消耗的道具大类Id */
    private String itemId;
    /* 消耗的道具数量 */
    private int itemNum;
    /* 购买消耗道具需要的元宝 */
    private int itemGold;
    /* 购买消耗道具需要的绑定元宝 */
    private int itemBgold;
    /* 增加的属性加成,包括战力值 */
    private Map<String, Long> attrMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(int needMoney) {
        this.needMoney = needMoney;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public int getItemGold() {
        return itemGold;
    }

    public void setItemGold(int itemGold) {
        this.itemGold = itemGold;
    }

    public int getItemBgold() {
        return itemBgold;
    }

    public void setItemBgold(int itemBgold) {
        this.itemBgold = itemBgold;
    }

    public Map<String, Long> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Long> attrMap) {
        this.attrMap = attrMap;
    }

}
