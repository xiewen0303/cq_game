package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 宝石/道具合成系统日志
 * 
 * @date 2015-6-5
 */
public class HuajuanFenjieLogEvent extends AbsGameLogEvent {
	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private JSONArray consumeItem;// 消耗物品
	private int beforeExp;
	private int beforeLevel;
	private int afterExp;
	private int afterLevel;

	public HuajuanFenjieLogEvent(long userRoleId, JSONArray consumeItem,
			int beforeExp, int beforeLevel, int afterExp, int afterLevel) {
		super(LogPrintHandle.HUAJUAN_FENJIE);
		this.userRoleId = userRoleId;
		this.consumeItem = consumeItem;
		this.beforeExp = beforeExp;
		this.beforeLevel = beforeLevel;
		this.afterExp = afterExp;
		this.afterLevel = afterLevel;
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

	public int getBeforeExp() {
		return beforeExp;
	}

	public void setBeforeExp(int beforeExp) {
		this.beforeExp = beforeExp;
	}

	public int getBeforeLevel() {
		return beforeLevel;
	}

	public void setBeforeLevel(int beforeLevel) {
		this.beforeLevel = beforeLevel;
	}

	public int getAfterExp() {
		return afterExp;
	}

	public void setAfterExp(int afterExp) {
		this.afterExp = afterExp;
	}

	public int getAfterLevel() {
		return afterLevel;
	}

	public void setAfterLevel(int afterLevel) {
		this.afterLevel = afterLevel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}