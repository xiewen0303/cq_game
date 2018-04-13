package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 心魔-魔神日志记录
 * @Author Yang Gao
 * @Since 2016-7-25
 * @Version 1.1.0
 */
public class XinmoMoshenLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private Long userRoleId;// 玩家id
    private String roleName;// 玩家昵称
    private Integer oldConfigId;// 操作之前的配置编号
    private Integer newConfigId;// 操作之后的配置编号
    private JSONArray consumeItem;// 消耗的道具(编号:数量)
    private Integer consumeJBVal;// 操作消耗的金币数量
    private Integer consumeYBVal;// 操作消耗的元宝数量
    private Integer consumeBYBVal;// 操作消耗的绑定元宝数量
    private Integer newBlessVal;// 操作之后的祝福值

    public XinmoMoshenLogEvent(Long userRoleId, String roleName, Integer oldConfigId, Integer newConfigId, JSONArray consumeItem, Integer consumeJBVal, Integer consumeYBVal, Integer consumeBYBVal, Integer newBlessVal) {
        super(LogPrintHandle.XINMO_MOSHEN_LOG);
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.oldConfigId = oldConfigId;
        this.newConfigId = newConfigId;
        this.consumeItem = consumeItem;
        this.consumeJBVal = consumeJBVal;
        this.consumeYBVal = consumeYBVal;
        this.consumeBYBVal = consumeBYBVal;
        this.newBlessVal = newBlessVal;
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

    public Integer getNewBlessVal() {
        return newBlessVal;
    }

    public void setNewBlessVal(Integer newBlessVal) {
        this.newBlessVal = newBlessVal;
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

}