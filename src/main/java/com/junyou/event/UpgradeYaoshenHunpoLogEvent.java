package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 升级妖神魂魄
 * @date 2015-6-5
 */
public class UpgradeYaoshenHunpoLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray consumeItem;// 消耗物品
	private int jie;
	private int ceng;

	public UpgradeYaoshenHunpoLogEvent(long userRoleId, JSONArray consumeItem,
			int jie, int ceng) {
		super(LogPrintHandle.YAOSHEN_HUNPO_LOG);
		this.userRoleId = userRoleId;
		this.jie = jie;
		this.ceng = ceng;
		this.consumeItem = consumeItem;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getJie() {
		return jie;
	}

	public void setJie(int jie) {
		this.jie = jie;
	}

	public int getCeng() {
		return ceng;
	}

	public void setCeng(int ceng) {
		this.ceng = ceng;
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