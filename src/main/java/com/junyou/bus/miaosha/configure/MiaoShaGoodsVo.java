package com.junyou.bus.miaosha.configure;
/**
 * @author LiuYu
 * 2016-3-4 下午3:54:24
 */
public class MiaoShaGoodsVo {
	private String goodsId;
	private int count;
	private boolean jipin;
	private String client;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isJipin() {
		return jipin;
	}
	public void setJipin(boolean jipin) {
		this.jipin = jipin;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
}
