package com.junyou.event;

import com.alibaba.fastjson.JSONArray;

/**
 * 仙剑升阶
 * 
 * @author wind
 * 
 */
public class XianJianLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private Long userRoleId;
    private Integer consumeMoney;// 消耗金币
    private Integer consumeGold;// 消耗元宝
    private JSONArray consumeItem; // 消耗物品
    private int beginLevel; // 之前等级
    private int endLevel; // 之后等级
    /**
     * @author Yang Gao
     */
    private int oldZfzVal; // 操作之前的祝福值
    private int newZfzVal; // 操作之后的祝福值

    public XianJianLogEvent(int type, Long userRoleId, Integer consumeMoney, Integer consumeGold, JSONArray consumeItem, int beginLevel, int endLevel, int oldZfzVal, int newZfzVal) {
        super(type);
        this.userRoleId = userRoleId;
        this.consumeMoney = consumeMoney;
        this.consumeGold = consumeGold;
        this.consumeItem = consumeItem;
        this.beginLevel = beginLevel;
        this.endLevel = endLevel;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
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