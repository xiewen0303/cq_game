package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 神秘商店
 * @author wind
 */
public class SMSDBuyLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; //玩家角色ID
	private int consumeGold; //消耗的货币数
	private int consumeType; //消耗的货币类型
	private String roleName; //玩家名称
	private String goodsId; //购买的物品ID
	private int count;		//购买的物品数量
	 
	
	public SMSDBuyLogEvent(Long userRoleId, int consumeGold,int consumeType, String roleName, 
			String goodsId, int count) {
			super(LogPrintHandle.SMSD_BUY);
			this.userRoleId = userRoleId;
			this.consumeGold = consumeGold;
			this.consumeType = consumeType;
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

	public int getConsumeType() {
		return consumeType;
	}

	public String getGoodsId() {
		return goodsId;
	}


	public int getCount() {
		return count;
	}


}