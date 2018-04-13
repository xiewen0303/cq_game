package com.junyou.bus.leichong.configue.export;

import java.util.Map;

/**
 * 累充53
 */
public class LeiChong53Config {
	
	private Integer id;
	private Integer itemcharge;			//累计充值额度
	private Map<String,Integer> itemreward;
	
	private Integer count = 1;			//可领取次数(默认一次)
	
	//奖励物品-服务自己用的(通用奖励和职业奖励已合并)
	private Integer returncharge; 		//充值额度
	private Integer returngold;   		//返还金币
	private Integer returnpercent;		//返还钻石比例
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getItemcharge() {
		return itemcharge;
	}
	public void setItemcharge(Integer itemcharge) {
		this.itemcharge = itemcharge;
	}
	public Map<String, Integer> getItemreward() {
		return itemreward;
	}
	public void setItemreward(Map<String, Integer> itemreward) {
		this.itemreward = itemreward;
	}
	public Integer getReturncharge() {
		return returncharge;
	}
	public void setReturncharge(Integer returncharge) {
		this.returncharge = returncharge;
	}
	public Integer getReturngold() {
		return returngold;
	}
	public void setReturngold(Integer returngold) {
		this.returngold = returngold;
	}
	public Integer getReturnpercent() {
		return returnpercent;
	}
	public void setReturnpercent(Integer returnpercent) {
		this.returnpercent = returnpercent;
	}
}
