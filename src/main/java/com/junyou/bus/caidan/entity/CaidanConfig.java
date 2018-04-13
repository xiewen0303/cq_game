package com.junyou.bus.caidan.entity;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.utils.collection.ReadOnlyMap;


public class CaidanConfig {

	private Integer id;
	private Integer gold;
	private Integer score;
	private Integer lucky;
	private Map<GoodsConfigureVo,Integer> itemMap;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getLucky() {
		return lucky;
	}
	public void setLucky(Integer lucky) {
		this.lucky = lucky;
	}
	public Map<GoodsConfigureVo, Integer> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<GoodsConfigureVo, Integer> itemMap) {
		this.itemMap = new ReadOnlyMap<>(itemMap);
	}
	
}
