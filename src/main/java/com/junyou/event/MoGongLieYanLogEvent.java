package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 
 * @Description 魔宫烈焰日志
 * @Author Yang Gao
 * @Since 2016-10-28
 * @Version 1.1.0
 */
public class MoGongLieYanLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    // 玩家id
    private long userRoleId;
    // 玩家昵称
    private String name;
    // 兑换之前的精华值
    private long beforeJinghua;
    // 兑换之后的精华值
    private long afterJinghua;
    // 兑换获得的物品
    private JSONArray goods;

    public MoGongLieYanLogEvent(long userRoleId, String name, long beforeJinghua, long afterJinghua, JSONArray goods) {
        super(LogPrintHandle.MOGONGLIEYAN_LOG);
        this.userRoleId = userRoleId;
        this.name = name;
        this.beforeJinghua = beforeJinghua;
        this.afterJinghua = afterJinghua;
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

    public long getBeforeJinghua() {
        return beforeJinghua;
    }

    public void setBeforeJinghua(long beforeJinghua) {
        this.beforeJinghua = beforeJinghua;
    }

    public long getAfterJinghua() {
        return afterJinghua;
    }

    public void setAfterJinghua(long afterJinghua) {
        this.afterJinghua = afterJinghua;
    }

    public JSONArray getGoods() {
        return goods;
    }

    public void setGoods(JSONArray goods) {
        this.goods = goods;
    }

}