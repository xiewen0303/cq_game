package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * @Description 热发布超值兑换日志记录
 * @Author Yang Gao
 * @Since 2016-6-12
 * @Version 1.1.0
 */
public class RfbSuperDuiHuanLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private Long userRoleId; // 玩家角色ID
    private String roleName; // 玩家名称
    private int consumeGold; // 消耗的元宝
    private String goodsId; // 获得的物品ID
    private int count; // 获得的物品数量

    public RfbSuperDuiHuanLogEvent(Long userRoleId, String roleName, int consumeGold, String goodsId, int count) {
        super(LogPrintHandle.SUPERDUIHUAN_LOG);
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.consumeGold = consumeGold;
        this.goodsId = goodsId;
        this.count = count;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public int getConsumeGold() {
        return consumeGold;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public int getCount() {
        return count;
    }

}