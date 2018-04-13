package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *qq管家奖励领取日志
 * @date 2015-6-5
 */
public class QqGuanjiaRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray item;// 获得物品
	private int category; //0新手礼包，1每日礼包

	public QqGuanjiaRewardLogEvent(long userRoleId, JSONArray item,int category) {
		super(LogPrintHandle.TENCENT_GUANJIA_GIFT);
		this.userRoleId = userRoleId;
		this.item = item;
		this.category  = category;
	}

	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
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