package com.junyou.event;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @Description 五行魔神升阶日志
 * @Author Yang Gao
 * @Since 2016-10-18
 * @Version 1.1.0
 */
public class WuxingMoshenLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private Long userRoleId;
    private int moshenType;// 1=玩家魔神操作;2=糖宝魔神操作
    private Integer consumeMoney;// 消耗金币
    private Integer consumeGold;// 消耗元宝
    private JSONArray consumeItem; // 消耗物品
    private int beginLevel; // 之前等级
    private int endLevel; // 之后等级
    private int oldZfzVal; // 操作之前的祝福值
    private int newZfzVal; // 操作之后的祝福值

    public WuxingMoshenLogEvent(int type, int moshenType, Long userRoleId, Integer consumeMoney, Integer consumeGold, JSONArray consumeItem, int beginLevel, int endLevel, int oldZfzVal, int newZfzVal) {
        super(type);
        this.moshenType = moshenType;
        this.userRoleId = userRoleId;
        this.consumeMoney = consumeMoney;
        this.consumeGold = consumeGold;
        this.consumeItem = consumeItem;
        this.beginLevel = beginLevel;
        this.endLevel = endLevel;
        this.oldZfzVal = oldZfzVal;
        this.newZfzVal = newZfzVal;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public int getMoshenType() {
        return moshenType;
    }

    public void setMoshenType(int moshenType) {
        this.moshenType = moshenType;
    }

    public Integer getConsumeMoney() {
        return consumeMoney;
    }

    public void setConsumeMoney(Integer consumeMoney) {
        this.consumeMoney = consumeMoney;
    }

    public Integer getConsumeGold() {
        return consumeGold;
    }

    public void setConsumeGold(Integer consumeGold) {
        this.consumeGold = consumeGold;
    }

    public JSONArray getConsumeItem() {
        return consumeItem;
    }

    public void setConsumeItem(JSONArray consumeItem) {
        this.consumeItem = consumeItem;
    }

    public int getBeginLevel() {
        return beginLevel;
    }

    public void setBeginLevel(int beginLevel) {
        this.beginLevel = beginLevel;
    }

    public int getEndLevel() {
        return endLevel;
    }

    public void setEndLevel(int endLevel) {
        this.endLevel = endLevel;
    }

    public int getOldZfzVal() {
        return oldZfzVal;
    }

    public void setOldZfzVal(int oldZfzVal) {
        this.oldZfzVal = oldZfzVal;
    }

    public int getNewZfzVal() {
        return newZfzVal;
    }

    public void setNewZfzVal(int newZfzVal) {
        this.newZfzVal = newZfzVal;
    }

}