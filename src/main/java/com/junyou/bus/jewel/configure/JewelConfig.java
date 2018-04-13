package com.junyou.bus.jewel.configure;

import java.util.Map;

/**
 * 宝石表
 * @author lxn
 */
public class JewelConfig {
    
	//id	level	type	nextid	needmoney	neednub
	private Integer id;
	private int  level;
	private int type;
	private Integer nextLevelid;
	private int needmoney;
	private int neednum;  //合成一个高级宝石消耗数
	private Map<String, Long> attrMap; //属性
	
	public Map<String, Long> getAttrMap() {
		return attrMap;
	}
	public void setAttrMap(Map<String, Long> attrMap) {
		this.attrMap = attrMap;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Integer getNextLevelid() {
		return nextLevelid;
	}
	public void setNextLevelid(Integer nextLevelid) {
		this.nextLevelid = nextLevelid;
	}
	public int getNeedmoney() {
		return needmoney;
	}
	public void setNeedmoney(int needmoney) {
		this.needmoney = needmoney;
	}
	public int getNeednum() {
		return neednum;
	}
	public void setNeednum(int neednum) {
		this.neednum = neednum;
	}
	
	
}
