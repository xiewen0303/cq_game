package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 37V计划
 * @author lxn
 */
public class _370VplanRewardLogEvent extends AbsGameLogEvent {
 
	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private int value; //值的含义， 0每日礼包=VIP等级，1等级礼包=领取多少级的礼包，2累计消费礼包=消费的元宝值
	private JSONArray item;// 获得物品
	private int giftType; // 领取的是那种类型的礼包,0每日礼包,1等级礼包,2累计消费礼包
	
 
	public _370VplanRewardLogEvent(long userRoleId, JSONArray item,int giftType,int value) {
		super(LogPrintHandle._360_VPLAN_GIFT);
		this.userRoleId = userRoleId;
		this.item = item;
		this.giftType  = giftType;
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	 
	public JSONArray getItem() {
		return item;
	}
	public void setItem(JSONArray item) {
		this.item = item;
	}
	public int getGiftType() {
		return giftType;
	}
	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	 

}