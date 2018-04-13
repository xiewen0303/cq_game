/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 新版本的画卷2分解日志记录
 * @Author Yang Gao
 * @Since 2016-9-13
 * @Version 1.1.0
 */
public class HuaJuan2FenjieLogEvent extends AbsGameLogEvent {
    private static final long serialVersionUID = -5393658806596260559L;

    /* 用户编号 */
    private Long userRoleId;
    /* 用户名称 */
    private String userName;
    /* 消耗道具(goodsId:count) */
    private JSONArray consumeItem;
    /* 分解前的等级 */
    private Integer beforeLevel;
    /* 分解前的等级经验值 */
    private Long beforeExp;
    /* 分解后的等级 */
    private Integer afterLevel;
    /* 分解后的等级经验值 */
    private Long afterExp;

    public HuaJuan2FenjieLogEvent(Long userRoleId, String userName, JSONArray consumeItem, Integer beforeLevel, Long beforeExp, Integer afterLevel, Long afterExp) {
        super(LogPrintHandle.HUAJUAN2_FENJIE);
        this.userRoleId = userRoleId;
        this.userName = userName;
        this.consumeItem = consumeItem;
        this.beforeLevel = beforeLevel;
        this.beforeExp = beforeExp;
        this.afterLevel = afterLevel;
        this.afterExp = afterExp;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public JSONArray getConsumeItem() {
        return consumeItem;
    }

    public void setConsumeItem(JSONArray consumeItem) {
        this.consumeItem = consumeItem;
    }

    public Integer getBeforeLevel() {
        return beforeLevel;
    }

    public void setBeforeLevel(Integer beforeLevel) {
        this.beforeLevel = beforeLevel;
    }

    public Long getBeforeExp() {
        return beforeExp;
    }

    public void setBeforeExp(Long beforeExp) {
        this.beforeExp = beforeExp;
    }

    public Integer getAfterLevel() {
        return afterLevel;
    }

    public void setAfterLevel(Integer afterLevel) {
        this.afterLevel = afterLevel;
    }

    public Long getAfterExp() {
        return afterExp;
    }

    public void setAfterExp(Long afterExp) {
        this.afterExp = afterExp;
    }

}
