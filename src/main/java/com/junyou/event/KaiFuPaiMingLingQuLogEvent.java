package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 新开服排名活动日志
 * @author LiuYu
 * @date 2015-3-30 下午5:17:30
 */
public class KaiFuPaiMingLingQuLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public KaiFuPaiMingLingQuLogEvent(long userRoleId, String name, JSONArray receiveItems, int stype) {
		super(LogPrintHandle.KAIFU_ACTITY_LQ);
		this.userRoleId = userRoleId;
		this.name = name;
		this.receiveItems = receiveItems;
		this.stype = stype;
	}

	private long userRoleId;
	private String name;
	private JSONArray receiveItems;
	private int stype;


	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public JSONArray getReceiveItems() {
		return receiveItems;
	}
	public int getStype() {
		return stype;
	}
	
	
}