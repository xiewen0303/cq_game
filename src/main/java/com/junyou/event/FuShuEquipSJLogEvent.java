package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 装备强升级
 * @author wind
 * 
 */
public class FuShuEquipSJLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId;
	private Long guid;
	private String roleName;
	private String goodsId;
	private String costItemId1;
	private int itemCount;
	private int needJBVal;//升级所需要的金币数量
	private String beginGoodsId;	//升级之前物品ID
	private String endGoodsId;	//升级之后物品ID
	
	public FuShuEquipSJLogEvent(Long userRoleId, Long guid,
			String roleName, String goodsId, String costItemId1,
			int needJBVal,int itemCount, String beginGoodsId, String endGoodsId) {
		super(LogPrintHandle.FUSHU_EQUIP_SJ);
		this.userRoleId = userRoleId;
		this.guid = guid;
		this.roleName = roleName;
		this.goodsId = goodsId;
		this.needJBVal = needJBVal;
		this.beginGoodsId = beginGoodsId;
		this.endGoodsId = endGoodsId;
		this.costItemId1 = costItemId1;
		this.itemCount = itemCount;
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
	public int getNeedJBVal() {
		return needJBVal;
	}
	public void setNeedJBVal(int needJBVal) {
		this.needJBVal = needJBVal;
	}

	public String getCostItemId1() {
		return costItemId1;
	}

	public int getItemCount() {
		return itemCount;
	}
}