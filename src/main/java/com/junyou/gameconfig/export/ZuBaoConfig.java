package com.junyou.gameconfig.export;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @description 组包 
 *
 * @author LiuJuan
 * @date 2012-3-16 下午3:20:18
 */
public class ZuBaoConfig{

	private String dropId;
	private List<ZuBaoOdds> zubaoOdds = new ArrayList<ZuBaoOdds>();
	
	public class ZuBaoOdds{
		private String goodsId;
		private int goodsCount;
		private float rate;
		
		public ZuBaoOdds(String goodsId, float rate, int goodsCount) {
			this.goodsId = goodsId;
			this.goodsCount = goodsCount;
			this.rate = rate;
		}
		public String getGoodsId() {
			return goodsId;
		}
		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}
		public float getRate() {
			return rate;
		}
		public void setRate(float rate) {
			this.rate = rate;
		}
		public int getGoodsCount() {
			return goodsCount;
		}
		public void setGoodsCount(int goodsCount) {
			this.goodsCount = goodsCount;
		}
	}

	public String getDropId() {
		return dropId;
	}

	public void setDropId(String dropId) {
		this.dropId = dropId;
	}

	public List<ZuBaoOdds> getZubaoOdds() {
		return zubaoOdds;
	}

	public void setZubaoOdds(List<ZuBaoOdds> zubaoOdds) {
		this.zubaoOdds = zubaoOdds;
	}
	
	public ZuBaoOdds newZuBaoConfig(String goodsId, int goodsCount, Float rate) {
		return new ZuBaoOdds(goodsId, rate, goodsCount);
	}
}
