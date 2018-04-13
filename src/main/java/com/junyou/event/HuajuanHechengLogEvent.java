package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 宝石/道具合成系统日志
 * 
 * @date 2015-6-5
 */
public class HuajuanHechengLogEvent extends AbsGameLogEvent {
	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private JSONArray consumeItem;// 消耗物品
	private String gainItemId;
	private int gainItemNum;

	public HuajuanHechengLogEvent(long userRoleId, JSONArray consumeItem,
			String gainItemId, int gainItemNum) {
		super(LogPrintHandle.HUAJUAN_HECHENG);
		this.userRoleId = userRoleId;
		this.consumeItem = consumeItem;
		this.gainItemId = gainItemId;
		this.gainItemNum = gainItemNum;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public JSONArray getConsumeItem() {
		return consumeItem;
	}

	public void setConsumeItem(JSONArray consumeItem) {
		this.consumeItem = consumeItem;
	}

	public String getGainItemId() {
		return gainItemId;
	}

	public void setGainItemId(String gainItemId) {
		this.gainItemId = gainItemId;
	}

	public int getGainItemNum() {
		return gainItemNum;
	}

	public void setGainItemNum(int gainItemNum) {
		this.gainItemNum = gainItemNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}