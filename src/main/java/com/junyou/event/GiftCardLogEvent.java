package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 签到领奖日志
 * @author chenjianye
 * @date 2015-4-16
 */
public class GiftCardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public GiftCardLogEvent(long userRoleId,String roleName,JSONArray goods, String cardNo,int money,int bindGold) {
		super(LogPrintHandle.GIFT_CARD);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.goods = goods;
		this.cardNo = cardNo;
		this.money = money;
		this.bindGold = bindGold;
	}

	private long userRoleId;
	private String roleName;
	private JSONArray goods;
	private String cardNo;
	private int money;
	private int bindGold;

	public long getUserRoleId() {
		return userRoleId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public String getRoleName() {
		return roleName;
	}

	public JSONArray getGoods() {
		return goods;
	}
	
	public int getMoney() {
		return money;
	}

	public int getBindGold() {
		return bindGold;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}