package com.junyou.public_.guild.entity;
/**
 * 门派商店
 * @author LiuYu
 * 2015-7-20 下午4:21:43
 */
public class GuildDuihuanConfig {
	private String goodsId;
	private int maxCount;
	private int needGong;
	private int needLevel;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public int getNeedGong() {
		return needGong;
	}
	public void setNeedGong(int needGong) {
		this.needGong = needGong;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	
}
