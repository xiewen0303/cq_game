package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 棋盘
 * @author wind
 */
public class QinPanLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; //玩家角色ID
	private String roleName; //玩家名称
	private JSONArray receiveItems;//物品信息
	 
	
	public QinPanLogEvent(Long userRoleId,  String roleName, JSONArray receiveItems) {
			super(LogPrintHandle.QINPAN_LOG);
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