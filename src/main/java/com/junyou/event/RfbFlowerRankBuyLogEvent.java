package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 热发布鲜花榜购买花
 * @date 2015-6-5
 */
public class RfbFlowerRankBuyLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray item;// 获得物品
	private int consumeGold; //消耗金币

	public RfbFlowerRankBuyLogEvent(long userRoleId, JSONArray item, int consumeGold) {
		super(LogPrintHandle.FLOWER_RANK_BUY);
		this.userRoleId = userRoleId;
		this.item = item;
		this.consumeGold = consumeGold;
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