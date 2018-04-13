package com.junyou.bus.suoyaota.entity;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-7-28 上午10:44:17
 */
public class SuoYaoTaSlotConfig {
	private int ceng;
	private Map<GoodsConfigureVo,Integer> itemOdds;//格位权重
	private int itemAllOdd;//格位权重总值
	private int odd;//格位权重
	public int getCeng() {
		return ceng;
	}
	public void setCeng(int ceng) {
		this.ceng = ceng;
	}
	public Map<GoodsConfigureVo, Integer> getItemOdds() {
		return itemOdds;
	}
	public void setItemOdds(Map<GoodsConfigureVo, Integer> itemOdds) {
		this.itemOdds = new ReadOnlyMap<>(itemOdds);
	}
	public int getItemAllOdd() {
		return itemAllOdd;
	}
	public void setItemAllOdd(int itemAllOdd) {
		this.itemAllOdd = itemAllOdd;
	}
	public int getOdd() {
		return odd;
	}
	public void setOdd(int odd) {
		this.odd = odd;
	}
	
	
}
