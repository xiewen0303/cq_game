package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 装备强升级
 * @author wind
 * 
 */
public class EquipSJLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId;
	private Long guid;
	private String roleName;
	private String goodsId;
	private JSONArray consumeItem;
	private int needJBVal;//升级所需要的金币数量
	private int consumeYBVal;//升级所需要的元宝数量
	private int consumeBYBVal;//升级所需要的绑定元宝数量
	private String beginGoodsId;	//升级之前物品ID
	private String endGoodsId;	//升级之后物品ID
	
	public EquipSJLogEvent(Long userRoleId, Long guid,
			String roleName, String goodsId, JSONArray consumeItem,
			int needJBVal,int consumeYBVal,int consumeBYBVal, String beginGoodsId, String endGoodsId) {
		super(LogPrintHandle.EQUIP_SJ);
		this.userRoleId = userRoleId;
		this.guid = guid;
		this.roleName = roleName;
		this.goodsId = goodsId;
		this.consumeItem = consumeItem;
		this.needJBVal = needJBVal;
		this.beginGoodsId = beginGoodsId;
		this.endGoodsId = endGoodsId;
		this.consumeYBVal = consumeYBVal;
		this.consumeBYBVal = consumeBYBVal;
	}
	
	
	public int getConsumeBYBVal() {
		return consumeBYBVal;
	}


	public int getConsumeYBVal() {
		return consumeYBVal;
	}
	public String getBeginGoodsId() {
		return beginGoodsId;
	}
	public void setBeginGoodsId(String beginGoodsId) {
		this.beginGoodsId = beginGoodsId;
	}
	public String getEndGoodsId() {
		return endGoodsId;
	}
	public void setEndGoodsId(String endGoodsId) {
		this.endGoodsId = endGoodsId;
	}
	public Long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Long getGuid() {
		return guid;
	}
	public void setGuid(Long guid) {
		this.guid = guid;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public JSONArray getConsumeItem() {
		return consumeItem;
	}
	public void setConsumeItem(JSONArray consumeItem) {
		this.consumeItem = consumeItem;
	}
	public int getNeedJBVal() {
		return needJBVal;
	}
	public void setNeedJBVal(int needJBVal) {
		this.needJBVal = needJBVal;
	}
}