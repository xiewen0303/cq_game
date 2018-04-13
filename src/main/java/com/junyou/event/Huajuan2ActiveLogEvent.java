/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 新版本画卷激活日志记录
 * @Author Yang Gao
 * @Since 2016-9-13
 * @Version 1.1.0
 */
public class Huajuan2ActiveLogEvent extends AbsGameLogEvent {
    private static final long serialVersionUID = 1L;
    /* 用户编号 */
    private Long userRoleId;
    /* 用户名称 */
    private String userName;
    /* 消耗道具格式(goodsId:count) */
    private JSONArray consumeItem;
    /* 激活画卷配置编号 */
    private Integer activeConfigId;

    public Huajuan2ActiveLogEvent(Long userRoleId, String userName, Integer activeConfigId, JSONArray consumeItem) {
        super(LogPrintHandle.HUAJUAN2_ACTIVE);
        this.userRoleId = userRoleId;
        this.userName = userName;
        this.activeConfigId = activeConfigId;
        this.consumeItem = consumeItem;
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

    public Integer getActiveConfigId() {
        return activeConfigId;
    }

    public void setActiveConfigId(Integer activeConfigId) {
        this.activeConfigId = activeConfigId;
    }

    public JSONArray getConsumeItem() {
        return consumeItem;
    }

    public void setConsumeItem(JSONArray consumeItem) {
        this.consumeItem = consumeItem;
    }

}
