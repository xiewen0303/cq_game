/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *@Description 新版本画卷2的升星日志记录
 *@Author Yang Gao
 *@Since 2016-9-13
 *@Version 1.1.0
 */
public class HuaJuan2UpgradeLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = -2991422721991917609L;

    /* 用户编号 */
    private Long userRoleId;
    /* 用户名称 */
    private String userName;
    /* 道具消耗goodsId:count */
    private JSONArray consumeItem;
    /* 升星之前的画卷配置id */
    private Integer beforeConfigId;
    /* 升星之后的画卷配置id */
    private Integer afterConfigId;
    
    public HuaJuan2UpgradeLogEvent(Long userRoleId, String userName, JSONArray consumeItem, Integer beforeConfigId, Integer afterConfigId) {
        super(LogPrintHandle.HUAJUAN2_UPGREADE);
        this.userRoleId = userRoleId;
        this.userName = userName;
        this.consumeItem = consumeItem;
        this.beforeConfigId = beforeConfigId;
        this.afterConfigId = afterConfigId;
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

    public Integer getBeforeConfigId() {
        return beforeConfigId;
    }

    public void setBeforeConfigId(Integer beforeConfigId) {
        this.beforeConfigId = beforeConfigId;
    }

    public Integer getAfterConfigId() {
        return afterConfigId;
    }

    public void setAfterConfigId(Integer afterConfigId) {
        this.afterConfigId = afterConfigId;
    }
    
    

    
}
