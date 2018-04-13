package com.junyou.bus.bag.export;

import java.io.Serializable;

 
/**
 * 物品入背包实例
 * @author: wind
 * @email: 18221610336@163.com
 * @version: 2014-12-26下午2:55:24
 * @Description:
 */
public class RoleItemInput implements Serializable{

	private static final long serialVersionUID = 1L;

	private long guid;

	private String goodsId;

	private Integer count;  

	private Integer currentDurability;

	private Long expireTime;

	private Integer jinyouLevel = 0;

	private Integer qhLevel = 0;

//	private String jianding;

//	private String gems;

//	private Integer openKongCount;

	private boolean newSlot = false;

	private long itemCreateTime;
	
	private String otherData;
	
	private Integer randomAttrs;//随机属性

	public Integer getRandomAttrs() {
		return randomAttrs;
	}


	public void setRandomAttrs(Integer randomAttrs) {
		this.randomAttrs = randomAttrs;
	}


	public long getGuid() {
		return guid;
	}


	public void setGuid(long guid) {
		this.guid = guid;
	}


	public String getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}


	public Integer getCurrentDurability() {
		return currentDurability;
	}


//	public void setCurrentDurability(Integer currentDurability) {
//		this.currentDurability = currentDurability;
//	}


	public Long getExpireTime() {
		return expireTime;
	}


	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}


	public Integer getJinyouLevel() {
		return jinyouLevel;
	}


	public void setJinyouLevel(Integer jinyouLevel) {
		this.jinyouLevel = jinyouLevel;
	}


	public Integer getQhLevel() {
		return qhLevel;
	}


	public void setQhLevel(Integer qhLevel) {
		this.qhLevel = qhLevel;
	}


//	public String getJianding() {
//		return jianding;
//	}
//
//
//	public void setJianding(String jianding) {
//		this.jianding = jianding;
//	}


//	public String getGems() {
//		return gems;
//	}
//
//
//	public void setGems(String gems) {
//		this.gems = gems;
//	}
//
//
//	public Integer getOpenKongCount() {
//		return openKongCount;
//	}
//
//
//	public void setOpenKongCount(Integer openKongCount) {
//		this.openKongCount = openKongCount;
//	}


	public boolean isNewSlot() {
		return newSlot;
	}


	public void setNewSlot(boolean newSlot) {
		this.newSlot = newSlot;
	}


	public long getItemCreateTime() {
		return itemCreateTime;
	}


	public void setItemCreateTime(long itemCreateTime) {
		this.itemCreateTime = itemCreateTime;
	}


	public String getOtherData() {
		return otherData;
	}


	public void setOtherData(String otherData) {
		this.otherData = otherData;
	}
}