package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 团购日志记录
 * @Author Yang Gao
 * @Since 2016-6-12
 * @Version 1.1.0
 */
public class TuanGouDuiHuanLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private Long userRoleId; // 玩家角色ID
    private String roleName; // 玩家名称
    private int consumeGold; // 消耗的元宝
    private JSONArray items; // 获得的物品集合

    public TuanGouDuiHuanLogEvent(Long userRoleId, String roleName, int consumeGold, JSONArray items) {
        super(LogPrintHandle.TUANGOU_LOG);
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.consumeGold = consumeGold;
        this.items = items;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public int getConsumeGold() {
        return consumeGold;
    }

    public String getRoleName() {
        return roleName;
    }

    public JSONArray getItems() {
        return items;
    }

}