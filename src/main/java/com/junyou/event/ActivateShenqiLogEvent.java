package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 激活神器日志
 * 
 * @author dsh
 * @date 2015-6-5
 */
public class ActivateShenqiLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private int id;
	private JSONArray consumeItem;// 消耗物品

	public ActivateShenqiLogEvent(long userRoleId, int id, JSONArray consumeItem) {
		super(LogPrintHandle.SHENQI_ACTIVATE);
		this.userRoleId = userRoleId;
		this.id = id;
		this.consumeItem = consumeItem;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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