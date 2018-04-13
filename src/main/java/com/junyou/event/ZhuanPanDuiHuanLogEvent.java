package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 幸运转盘兑换
 * @author wind
 */
public class ZhuanPanDuiHuanLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; //玩家角色ID
	private int consumeGold; //消耗的积分
	private String roleName; //玩家名称
	private String goodsId; //获得的物品ID
	private int count;		//获得的物品数量
	 
	
	public ZhuanPanDuiHuanLogEvent(Long userRoleId, int consumeGold, String roleName, 
			String goodsId, int count) {
			super(LogPrintHandle.ZHUANGPAN_LOG_DUIHUAN);
			this.userRoleId = userRoleId;
			this.consumeGold = consumeGold;
			this.roleName = roleName;
			this.goodsId = goodsId;
			this.count = count;
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

	public String getGoodsId() {
		return goodsId;
	}


	public int getCount() {
		return count;
	}


}