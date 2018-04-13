package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 
 * @Description 心魔斗场副本日志
 * @Author Yang Gao
 * @Since 2016-8-24
 * @Version 1.1.0
 */
public class XinmoDouchangLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;
    private long userRoleId;// 玩家id
    private String roleName;// 玩家昵称
    private int fightCount;// 今日挑战次数
    private int buyCount;// 今日购买次数
    private int killCount;// 击杀副本boss个数
    private long exp; // 奖励经验
    private long money; // 奖励银两
    private long zq; // 奖励真气
    private JSONArray goods; // 奖励道具

    public XinmoDouchangLogEvent(long userRoleId, String roleName, int fightCount, int buyCount, int killCount, long exp, long money, long zq, JSONArray goods) {
        super(LogPrintHandle.XINMO_DOUCHANG_FUBEN_LOG);
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.fightCount = fightCount;
        this.buyCount = buyCount;
        this.killCount = killCount;
        this.exp = exp;
        this.money = money;
        this.zq = zq;
        this.goods = goods;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getFightCount() {
        return fightCount;
    }

    public void setFightCount(int fightCount) {
        this.fightCount = fightCount;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
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

    public JSONArray getGoods() {
        return goods;
    }

    public void setGoods(JSONArray goods) {
        this.goods = goods;
    }

}
