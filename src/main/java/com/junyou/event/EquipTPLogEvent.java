package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 装备提品
 * @author LiuYu
 * @date 2015-7-17 下午1:43:35
 */
public class EquipTPLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId;
	private Long guid;
	private String roleName;
	private String goodsId;
	private JSONArray consumeItem;
	private int beforeTipin;//之前提品值
	private int afterTipin;//之后提品值
	private String endGoodsId;	//提品后的物品ID（没变化则为null)
	
	public EquipTPLogEvent(Long userRoleId, Long guid,
			String roleName, String goodsId, JSONArray consumeItem,
			int beforeTipin,int afterTipin,String endGoodsId) {
		super(LogPrintHandle.EQUIP_TIPIN);
		this.userRoleId = userRoleId;
		this.guid = guid;
		this.roleName = roleName;
		this.goodsId = goodsId;
		this.consumeItem = consumeItem;
		this.beforeTipin = beforeTipin;
		this.afterTipin = afterTipin;
		this.endGoodsId = endGoodsId;
	}
	
	public Long getUserRoleId() {
		return userRoleId;
	}
	public Long getGuid() {
		return guid;
	}
	public String getRoleName() {
		return roleName;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public JSONArray getConsumeItem() {
		return consumeItem;
	}
	public int getBeforeTipin() {
		return beforeTipin;
	}
	public int getAfterTipin() {
		return afterTipin;
	}
	public String getEndGoodsId() {
		return endGoodsId;
	}
	
}