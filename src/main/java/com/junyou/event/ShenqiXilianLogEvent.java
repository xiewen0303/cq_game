package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *神器洗练日志
 * @date 2015-6-5
 */
public class ShenqiXilianLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L; 
	private long userRoleId;
	private JSONArray item;// 消耗的物品
	private int auto; // auto=0：材料不足不自动购买，1：材料不足自动购买
	private int consumeType;//consumeType=1普通洗练，2元宝洗练，
	private int needMoney;
	private int needGold;
	private int shenqiId; //洗练的神器id
	

	public ShenqiXilianLogEvent(long userRoleId,int shenqiId,int auto,int consumeType, int needMoney,int needGold, JSONArray item) {
		super(LogPrintHandle.SHENQI_XILIAN);
		this.userRoleId = userRoleId;
		this.item = item;
		this.auto = auto;
		this.consumeType = consumeType;
		this.needGold= needGold;
		this.needMoney = needMoney;
		this.item = item;
		this.shenqiId = shenqiId;
	}

	public int getShenqiId() {
		return shenqiId;
	}
	public void setShenqiId(int shenqiId) {
		this.shenqiId = shenqiId;
	}
	public int getAuto() {
		return auto;
	}

	public void setAuto(int auto) {
		this.auto = auto;
	}

	public int getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(int consumeType) {
		this.consumeType = consumeType;
	}

	public int getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}

	public int getNeedGold() {
		return needGold;
	}

	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public JSONArray getItem() {
		return item;
	}

	public void setItem(JSONArray item) {
		this.item = item;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}