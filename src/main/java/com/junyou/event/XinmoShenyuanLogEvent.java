package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 心魔-深渊副本日志记录
 * @Author Yang Gao
 * @Since 2016-8-19
 * @Version 1.1.0
 */

public class XinmoShenyuanLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;
    private long userRoleId;// 玩家id
    private String roleName;// 玩家昵称
    private int passFubenId;// 通关副本的配置编号
    private JSONArray goods; // 奖励道具
    private long exp; // 奖励经验
    private long money; // 奖励银两
    private long zq; // 奖励真气
    
    public XinmoShenyuanLogEvent( long userRoleId, String roleName, int passFubenId, JSONArray goods, long exp, long money, long zq) {
        super(LogPrintHandle.XINMO_SHENYUAN_FUBEN_LOG);
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.passFubenId = passFubenId;
        this.goods = goods;
        this.exp = exp;
        this.money = money;
        this.zq = zq;
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
    public int getPassFubenId() {
        return passFubenId;
    }
    public void setPassFubenId(int passFubenId) {
        this.passFubenId = passFubenId;
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
