/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 仙器觉醒升级日志
 * @Author Yang Gao
 * @Since 2016-10-27
 * @Version 1.1.0
 */
public class XianqiJuexingLogEvent extends AbsGameLogEvent {

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
     * 升级的仙器类型
     */
    private int xianqiType;
    /**
     * 升级前的等级
     */
    private int beforeLevel;
    /**
     * 升级后的等级
     */
    private int afterLevel;
    /**
     * 升级的道具消耗
     */
    private JSONArray goods;

    public XianqiJuexingLogEvent(long userRoleId, String userName, int xianqiType, int beforeLevel, int afterLevel, JSONArray goods) {
        super(LogPrintHandle.XIANDONG_JUEXING_LOG);
        this.userRoleId = userRoleId;
        this.userName = userName;
        this.xianqiType = xianqiType;
        this.beforeLevel = beforeLevel;
        this.afterLevel = afterLevel;
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

    public int getXianqiType() {
        return xianqiType;
    }

    public void setXianqiType(int xianqiType) {
        this.xianqiType = xianqiType;
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

    public JSONArray getGoods() {
        return goods;
    }

    public void setGoods(JSONArray goods) {
        this.goods = goods;
    }

}
