package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *领取首冲红包 
 */
public class HongbaoGetLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;   //收红包人
	private long sendUserRoleId; //发送红包人
	private JSONArray item;// 获得物品

	public HongbaoGetLogEvent(long userRoleId, JSONArray item,Long sendUserRoleId ) {
		super(LogPrintHandle.HONGBAO_GIFT);
		this.userRoleId = userRoleId;
		this.item = item;
		this.sendUserRoleId  = sendUserRoleId;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public long getSendUserRoleId() {
		return sendUserRoleId;
	}
	public void setSendUserRoleId(long sendUserRoleId) {
		this.sendUserRoleId = sendUserRoleId;
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