package com.junyou.bus.xunbao.configure;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author zhongdian 
 * @email  zhongdian@junyougame.com
 * @date 2015-10-8 下午2:24:39
 */
public class RfbXunBaoConfig {
	private int id;
	
	private Integer mincishu;

	private Integer maxcishu;
	
	private int allOdds;

	private Map<Object[],Integer> goodsOdds =new HashMap<Object[], Integer>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getMincishu() {
		return mincishu;
	}

	public void setMincishu(Integer mincishu) {
		this.mincishu = mincishu;
	}

	public Integer getMaxcishu() {
		return maxcishu;
	}

	public void setMaxcishu(Integer maxcishu) {
		this.maxcishu = maxcishu;
	}

	public int getAllOdds() {
		return allOdds;
	}

	public void setAllOdds(int allOdds) {
		this.allOdds = allOdds;
	}

	public Map<Object[], Integer> getGoodsOdds() {
		return goodsOdds;
	}

	public void addGoodsOdds(Object[] objects, int dropOdds) {
		goodsOdds.put(objects, dropOdds);
	}
}
