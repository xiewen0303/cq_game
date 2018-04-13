package com.junyou.bus.shizhuang.configure.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;


/**
 * 
 * @description 时装进阶
 *
 * @author ChenXiaobing
 * @date 2016-11-21 14:55:39
 */
public class ShiZhuangJinJieConfig implements IGoodsCheckConfig{

	private int id;
	private int level;
	private int needmoney;
	private Map<String,Integer> costItem;
	private int gold;
	private int bgold;
	private int sex;
	private String mallid;
	private Map<String,Long> attrs;
	
	public String getMallid() {
		return mallid;
	}
	public void setMallid(String mallid) {
		this.mallid = mallid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<String, Integer> getCostItem() {
		return costItem;
	}
	public void setCostItem(Map<String, Integer> costItem) {
		this.costItem = costItem;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getBgold() {
		return bgold;
	}
	public void setBgold(int bgold) {
		this.bgold = bgold;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getNeedmoney() {
		return needmoney;
	}
	public void setNeedmoney(int needmoney) {
		this.needmoney = needmoney;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	@Override
	public String getConfigName() {
		return "ShiZhuangJinJie--"+id;
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = new ArrayList<>();
		list.add(costItem);
		return list;
	}
	
}
