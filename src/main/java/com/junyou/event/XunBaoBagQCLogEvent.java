package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 寻宝背包取出物品
 * @author wind
 */
public class XunBaoBagQCLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId;
	private String roleName; 
	private JSONArray receiveItems;//取出物品
	
	public XunBaoBagQCLogEvent(Long userRoleId, String roleName,
			JSONArray receiveItems) {
		super(LogPrintHandle.XUNBAO_BAG_QC);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.receiveItems = receiveItems;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public JSONArray getReceiveItems() {
		return receiveItems;
	}
	
}