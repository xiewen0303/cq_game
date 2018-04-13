package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 激活宠物
 * 
 * @author dsh
 * @date 2015-6-5
 */
public class UpgradeChongwuLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private int chongwuId;
	private JSONArray consumeItem;// 消耗物品
	private int gold;
	private int money;

	public UpgradeChongwuLogEvent(long userRoleId,Integer chongwuId,JSONArray consumeItem,int gold,int money) {
		super(LogPrintHandle.CHONGWU_UPDATE_JIE);
		this.userRoleId = userRoleId;
		this.chongwuId = chongwuId;
		this.gold = gold;
		this.money = money;
		this.consumeItem = consumeItem;
	}

	public long getUserRoleId() {
		return userRoleId;
	}
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getChongwuId() {
		return chongwuId;
	}

	public void setChongwuId(int chongwuId) {
		this.chongwuId = chongwuId;
	}

	public JSONArray getConsumeItem() {
		return consumeItem;
	}

	public void setConsumeItem(JSONArray consumeItem) {
		this.consumeItem = consumeItem;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}