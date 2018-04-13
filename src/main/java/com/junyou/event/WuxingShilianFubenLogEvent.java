package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *@Description 五行试炼副本日志
 *@Author Yang Gao
 *@Since 2016-6-22
 *@Version 1.1.0
 */
public class WuxingShilianFubenLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private long userRoleId;// 玩家编号
    private String name; // 玩家昵称
    private int killMonsterNum;// 击杀怪物数量
    private JSONArray goods; // 奖励道具

    public WuxingShilianFubenLogEvent(long userRoleId, String name, int killMonsterNum, JSONArray goods) {
        super(LogPrintHandle.WUXING_SHILIAN_FUBEN);
        this.userRoleId = userRoleId;
        this.name = name;
        this.killMonsterNum = killMonsterNum;
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

    public int getKillMonsterNum() {
        return killMonsterNum;
    }

    public void setKillMonsterNum(int killMonsterNum) {
        this.killMonsterNum = killMonsterNum;
    }

    public JSONArray getGoods() {
        return goods;
    }

    public void setGoods(JSONArray goods) {
        this.goods = goods;
    }

}