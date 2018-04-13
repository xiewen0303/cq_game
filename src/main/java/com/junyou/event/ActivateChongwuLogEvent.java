package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 激活宠物
 * 
 * @author dsh
 * @date 2015-6-5
 */
public class ActivateChongwuLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private int chongwuId;
	private JSONArray consumeItem;// 消耗物品

	public ActivateChongwuLogEvent(long userRoleId,Integer chongwuId,JSONArray consumeItem) {
		super(LogPrintHandle.CHONGWU_ACTIVATE);
		this.userRoleId = userRoleId;
		this.chongwuId = chongwuId;
		this.consumeItem = consumeItem;
	}

	public long getUserRoleId() {
		return userRoleId;
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