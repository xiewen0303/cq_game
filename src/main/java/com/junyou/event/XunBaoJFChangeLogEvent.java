package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 寻宝积分兑换
 * @author wind
 */
public class XunBaoJFChangeLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId;
	private String roleName;
	private int consumeJF;//寻宝积分
	private String consumeGoodsId;
	private int consumeCount;
	
	private JSONArray receiveItems;//获得物品

	public XunBaoJFChangeLogEvent( Long userRoleId, String roleName,
			int consumeJF, String consumeGoodsId, int consumeCount,
			JSONArray receiveItems) {
		super(LogPrintHandle.XUNBAO_JFDH);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.consumeJF = consumeJF;
		this.consumeGoodsId = consumeGoodsId;
		this.consumeCount = consumeCount;
		this.receiveItems = receiveItems;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public int getConsumeJF() {
		return consumeJF;
	}

	public String getConsumeGoodsId() {
		return consumeGoodsId;
	}

	public int getConsumeCount() {
		return consumeCount;
	}

	public JSONArray getReceiveItems() {
		return receiveItems;
	}
}