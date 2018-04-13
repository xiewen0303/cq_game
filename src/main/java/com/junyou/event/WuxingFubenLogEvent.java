package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 五行副本日志
 * @Author Yang Gao
 * @Since 2016-4-21 下午8:12:50
 * @Version 1.1.0
 */
public class WuxingFubenLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private long userRoleId;
    private String name;
    private int fubenId;
    private JSONArray goods;
    private long exp;
    private long money;
    private long zq;

    public WuxingFubenLogEvent(long userRoleId, String name, int fubenId, long money, long exp, long zq, JSONArray goods) {
        super(LogPrintHandle.WUXING_FUBEN);
        this.userRoleId = userRoleId;
        this.name = name;
        this.fubenId = fubenId;
        this.money = money;
        this.exp = exp;
        this.zq = zq;
        this.goods = goods;
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

    public int getFubenId() {
        return fubenId;
    }

    public void setFubenId(int fubenId) {
        this.fubenId = fubenId;
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

}