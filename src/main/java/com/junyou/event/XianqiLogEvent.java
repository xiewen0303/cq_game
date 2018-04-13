/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 仙洞升级日志
 * @Author Yang Gao
 * @Since 2016-10-27
 * @Version 1.1.0
 */
public class XianqiLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    private long userRoleId;
    /**
     * 角色名称
     */
    private String userName;
    /**
     * 升级前的等级
     */
    private int beforeLevel;
    /**
     * 升级后的等级
     */
    private int afterLevel;
    /**
     * 升级前的经验
     */
    private long beforeExp;
    /**
     * 升级后的经验
     */
    private long afterExp;
    /**
     * 升级的道具消耗
     */
    private JSONArray goods;

    public XianqiLogEvent(long userRoleId, String userName, int beforeLevel, int afterLevel, long beforeExp, long afterExp, JSONArray goods) {
        super(LogPrintHandle.XIANDONG_LOG);
        this.userRoleId = userRoleId;
        this.userName = userName;
        this.beforeLevel = beforeLevel;
        this.afterLevel = afterLevel;
        this.beforeExp = beforeExp;
        this.afterExp = afterExp;
        this.goods = goods;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBeforeLevel() {
        return beforeLevel;
    }

    public void setBeforeLevel(int beforeLevel) {
        this.beforeLevel = beforeLevel;
    }

    public int getAfterLevel() {
        return afterLevel;
    }

    public void setAfterLevel(int afterLevel) {
        this.afterLevel = afterLevel;
    }

    public long getBeforeExp() {
        return beforeExp;
    }

    public void setBeforeExp(long beforeExp) {
        this.beforeExp = beforeExp;
    }

    public long getAfterExp() {
        return afterExp;
    }

    public void setAfterExp(long afterExp) {
        this.afterExp = afterExp;
    }

    public JSONArray getGoods() {
        return goods;
    }

    public void setGoods(JSONArray goods) {
        this.goods = goods;
    }

}
