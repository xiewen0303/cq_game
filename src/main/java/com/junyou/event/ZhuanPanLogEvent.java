package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 幸运转盘
 * @author wind
 */
public class ZhuanPanLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; //玩家角色ID
	private int consumeGold; //消耗的元宝
	private String roleName; //玩家名称
	private String goodsId; //获得的物品ID
	private int count;		//获得的物品数量
	 
	
	public ZhuanPanLogEvent(Long userRoleId, int consumeGold, String roleName, 
			String goodsId, int count) {
			super(LogPrintHandle.ZHUANGPAN_LOG);
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