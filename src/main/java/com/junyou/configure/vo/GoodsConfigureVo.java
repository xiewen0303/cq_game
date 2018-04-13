package com.junyou.configure.vo;

/**
 */
public class GoodsConfigureVo {

	private String goodsId;
	
	private int goodsCount;
	
	//强化等级
	private int qhLevel;

	public GoodsConfigureVo(String goodsId, int goodsCount) {
		this.goodsId = goodsId;
		this.goodsCount = goodsCount;
	}

	public int getQhLevel() {
		return qhLevel;
	}

	public void setQhLevel(int qhLevel) {
		this.qhLevel = qhLevel;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	
}
