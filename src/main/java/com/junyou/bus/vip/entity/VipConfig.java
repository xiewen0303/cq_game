package com.junyou.bus.vip.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;

public class VipConfig implements IGoodsCheckConfig{
	private int order;
	private int level;
	private int time;
	private long needExp;
	private int zplus;
	private ReadOnlyMap<String, Integer> tequanMap;
	private String titleId;
	private ReadOnlyMap<String, GoodsConfigureVo> levelItem;
	private ReadOnlyMap<String, Integer> weekItem;
	private ReadOnlyMap<String, Long> attribute;
	private int cuilianMoneyTimes;
	private int cuilianBgoldTimes;
	private int cuilianGoldTimes;
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getZplus() {
		return zplus;
	}
	public void setZplus(int zplus) {
		this.zplus = zplus;
	}
	public ReadOnlyMap<String, Integer> getTequanMap() {
		return tequanMap;
	}
	public void setTequanMap(Map<String, Integer> tequanMap) {
		this.tequanMap = new ReadOnlyMap<>(tequanMap);
	}
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	public ReadOnlyMap<String, GoodsConfigureVo> getLevelItem() {
		return levelItem;
	}
	public void setLevelItem(Map<String, GoodsConfigureVo> levelItem) {
		this.levelItem = new ReadOnlyMap<>(levelItem);
	}
	public ReadOnlyMap<String, Integer> getWeekItem() {
		return weekItem;
	}
	public void setWeekItem(Map<String, Integer> weekItem) {
		this.weekItem = new ReadOnlyMap<>(weekItem);
	}
	public long getNeedExp() {
		return needExp;
	}
	public void setNeedExp(long needExp) {
		this.needExp = needExp;
	}
	public ReadOnlyMap<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	public int getCuilianMoneyTimes() {
		return cuilianMoneyTimes;
	}
	public void setCuilianMoneyTimes(int cuilianMoneyTimes) {
		this.cuilianMoneyTimes = cuilianMoneyTimes;
	}
	public int getCuilianBgoldTimes() {
		return cuilianBgoldTimes;
	}
	public void setCuilianBgoldTimes(int cuilianBgoldTimes) {
		this.cuilianBgoldTimes = cuilianBgoldTimes;
	}
	public int getCuilianGoldTimes() {
		return cuilianGoldTimes;
	}
	public void setCuilianGoldTimes(int cuilianGoldTimes) {
		this.cuilianGoldTimes = cuilianGoldTimes;
	}
	@Override
	public String getConfigName() {
		return "VipConfig--" + order;
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = new ArrayList<>();
		if(levelItem != null){
			Map<String,Integer> levelMap = new HashMap<>();
			for (Map.Entry<String, GoodsConfigureVo> entry : levelItem.entrySet()) {
				levelMap.put(entry.getKey(), entry.getValue().getGoodsCount());
			}
			list.add(levelMap);
		}
		if(weekItem != null){
			list.add(weekItem);
		}
		return list;
	}
	
}
