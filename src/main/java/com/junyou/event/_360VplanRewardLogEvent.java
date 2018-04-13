package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 360V计划
 * @author lxn
 */
public class _360VplanRewardLogEvent extends AbsGameLogEvent {
 
	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private int level; // 等级
	private JSONArray item;// 获得物品
	private int giftType; // 领取的是那种类型的礼包,0每日礼包,1升级礼包,2开通礼包,3消费礼包
	private int consumeGold; //giftType=3 ，消费金额
	

	public _360VplanRewardLogEvent(long userRoleId, JSONArray item,int giftType,int value) {
		this(userRoleId, item, giftType);
		if(giftType==3){
			this.level  = value;
		}else{
			this.consumeGold  = value;
		}
	}
	public _360VplanRewardLogEvent(long userRoleId, JSONArray item,int giftType) {
		super(LogPrintHandle._360_VPLAN_GIFT);
		this.userRoleId = userRoleId;
		this.item = item;
		this.giftType  = giftType;
	}
	
	public int getConsumeGold() {
		return consumeGold;
	}
	public void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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