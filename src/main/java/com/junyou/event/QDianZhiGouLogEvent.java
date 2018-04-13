package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * Q点直购
 * @author wind
 */
public class QDianZhiGouLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; //玩家角色ID
	private int qDian; //消耗的Q点
	private int qDaiJin; //消耗的代金卷
	private String roleName; //玩家名称
	private String goodsId; //购买的物品ID
	private int count;		//购买的物品数量
	 
	
	public QDianZhiGouLogEvent(Long userRoleId, int qDian,int qDaiJin, String roleName, 
			String goodsId, int count) {
			super(LogPrintHandle.QDIAN_ZHIGOU);
			this.userRoleId = userRoleId;
			this.qDian = qDian;
			this.qDaiJin = qDaiJin;
			this.roleName = roleName;
			this.goodsId = goodsId;
			this.count = count;
	} 

	public Long getUserRoleId() {
		return userRoleId;
	}

	public int getQDian() {
		return qDian;
	}
	
	public int getqDaiJin() {
		return qDaiJin;
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