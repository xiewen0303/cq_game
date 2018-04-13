package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 五行技能副本日志
 * @Author Yang Gao
 * @Since 2016-5-4
 * @Version 1.1.0
 */
public class WuxingSkillFubenLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private long userRoleId;// 玩家编号
    private String name; // 玩家昵称
    private int layerId; // 通关层次ID
    private JSONArray goods; // 奖励道具
    private long exp; // 奖励经验
    private long money; // 奖励银两
    private long zq; // 奖励真气
    private int addBuff;// 奖励增益buff值
    private int subBuff;// 奖励减伤buff值
    private int logType;// 日志类型 (1=普通挑战;2=扫荡通关)

    public WuxingSkillFubenLogEvent(long userRoleId, String name, int layerId, long money, long exp, long zq, int addBuff, int subBuff, JSONArray goods, int logType) {
        super(LogPrintHandle.WUXING_SKILL_FUBEN);
        this.userRoleId = userRoleId;
        this.name = name;
        this.layerId = layerId;
        this.money = money;
        this.exp = exp;
        this.zq = zq;
        this.goods = goods;
        this.addBuff = addBuff;
        this.subBuff = subBuff;
        this.logType = logType;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLayerId() {
        return layerId;
    }

    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    public JSONArray getGoods() {
        return goods;
    }

    public void setGoods(JSONArray goods) {
        this.goods = goods;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getZq() {
        return zq;
    }

    public void setZq(long zq) {
        this.zq = zq;
    }

    public int getAddBuff() {
        return addBuff;
    }

    public void setAddBuff(int addBuff) {
        this.addBuff = addBuff;
    }

    public int getSubBuff() {
        return subBuff;
    }

    public void setSubBuff(int subBuff) {
        this.subBuff = subBuff;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

}