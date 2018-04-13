package com.junyou.bus.jewel.configure;


/**
 * 宝石合成表
 * @author lxn
 */
public class HechengConfig {
    
	private String id1;
	private String nextLevelid;
	private int needmoney;
	private int neednum;  //合成一个高级宝石消耗数
	
	 
	 
	public String getId1() {
		return id1;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public String getNextLevelid() {
		return nextLevelid;
	}
	public void setNextLevelid(String nextLevelid) {
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
