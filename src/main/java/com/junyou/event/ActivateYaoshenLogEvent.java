package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 激活妖神
 * 
 * @author dsh
 * @date 2015-6-5
 */
public class ActivateYaoshenLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray consumeItem;// 消耗物品
	private int bgold;
	private int gold;

	public ActivateYaoshenLogEvent(long userRoleId,JSONArray consumeItem,int bgold,int gold) {
		super(LogPrintHandle.YAOSHNE_ACTIVATE);
		this.userRoleId = userRoleId;
		this.bgold = bgold;
		this.gold = gold;
		this.consumeItem = consumeItem;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getBgold() {
		return bgold;
	}

	public void setBgold(int bgold) {
		this.bgold = bgold;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
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