package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 寻宝
 * @author wind
 */
public class XunBaoLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId; //玩家角色ID
	private int consumeGold; //消耗的元宝数
	private String roleName; //玩家名称
	private String goodsId; //消耗寻宝钥匙物品id
	private int count;		//消耗寻宝钥匙数量
	private JSONArray receiveItems;//寻宝获得的物品信息
	private int xbType; //寻宝类型
	private int xbCountType; //寻宝次数类型
	 
	
	public XunBaoLogEvent(Long userRoleId, int consumeGold, String roleName, 
			String goodsId, int count, JSONArray receiveItems,
			int xbType, int xbCountType) {
			super(LogPrintHandle.XUNBAO);
			this.userRoleId = userRoleId;
			this.consumeGold = consumeGold;
			this.roleName = roleName;
			this.goodsId = goodsId;
			this.count = count;
			this.receiveItems = receiveItems;
			this.xbType = xbType;
			this.xbCountType = xbCountType;
	} 

	public Long getUserRoleId() {
		return userRoleId;
	}

	public int getConsumeGold() {
		return consumeGold;
	}

	public String getRoleName() {
		return roleName;
	}


	public String getGoodsId() {
		return goodsId;
	}


	public int getCount() {
		return count;
	}


	public JSONArray getReceiveItems() {
		return receiveItems;
	}


	public int getXbType() {
		return xbType;
	}


	public int getXbCountType() {
		return xbCountType;
	} 
}