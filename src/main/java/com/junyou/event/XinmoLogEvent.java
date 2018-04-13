package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 心魔日志记录
 * @Author Yang Gao
 * @Since 2016-6-29
 * @Version 1.1.0
 */
public class XinmoLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private Long userRoleId;// 玩家id
    private String roleName;// 玩家昵称
    private Integer actionType;// 操作类型:1=升级;2=突破;3=凝神
    private Integer oldConfigId;// 操作之前的配置编号
    private Integer newConfigId;// 操作之后的配置编号
    private JSONArray consumeItem;// 消耗的道具(编号:数量)
    private Integer consumeJBVal;// 操作消耗的金币数量
    private Integer consumeYBVal;// 操作消耗的元宝数量
    private Integer consumeBYBVal;// 操作消耗的绑定元宝数量
    private Integer oldXinmoExp;// 操作之前的心元力
    private Integer newXinmoExp;// 操作之前的心元力

    public XinmoLogEvent(Long userRoleId, String roleName, Integer actionType, Integer oldConfigId, Integer newConfigId, JSONArray consumeItem, Integer consumeJBVal, Integer consumeYBVal, Integer consumeBYBVal, Integer oldXinmoExp, Integer newXinmoExp) {
        super(LogPrintHandle.XINMO_LOG);
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.actionType = actionType;
        this.oldConfigId = oldConfigId;
        this.newConfigId = newConfigId;
        this.consumeItem = consumeItem;
        this.consumeJBVal = consumeJBVal;
        this.consumeYBVal = consumeYBVal;
        this.consumeBYBVal = consumeBYBVal;
        this.oldXinmoExp = oldXinmoExp;
        this.newXinmoExp = newXinmoExp;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getOldConfigId() {
        return oldConfigId;
    }

    public void setOldConfigId(Integer oldConfigId) {
        this.oldConfigId = oldConfigId;
    }

    public Integer getNewConfigId() {
        return newConfigId;
    }

    public void setNewConfigId(Integer newConfigId) {
        this.newConfigId = newConfigId;
    }

    public JSONArray getConsumeItem() {
        return consumeItem;
    }

    public void setConsumeItem(JSONArray consumeItem) {
        this.consumeItem = consumeItem;
    }

    public Integer getConsumeJBVal() {
        return consumeJBVal;
    }

    public void setConsumeJBVal(Integer consumeJBVal) {
        this.consumeJBVal = consumeJBVal;
    }

    public Integer getConsumeYBVal() {
        return consumeYBVal;
    }

    public void setConsumeYBVal(Integer consumeYBVal) {
        this.consumeYBVal = consumeYBVal;
    }

    public Integer getConsumeBYBVal() {
        return consumeBYBVal;
    }

    public void setConsumeBYBVal(Integer consumeBYBVal) {
        this.consumeBYBVal = consumeBYBVal;
    }

    public Integer getOldXinmoExp() {
        return oldXinmoExp;
    }

    public void setOldXinmoExp(Integer oldXinmoExp) {
        this.oldXinmoExp = oldXinmoExp;
    }

    public Integer getNewXinmoExp() {
        return newXinmoExp;
    }

    public void setNewXinmoExp(Integer newXinmoExp) {
        this.newXinmoExp = newXinmoExp;
    }

}