package com.junyou.bus.marry.entity;


/**
 * @author LiuYu
 * 2015-8-10 下午9:44:05
 */
public class DingHunConfig {
	private int costMoney;//订婚消耗
	private int maxYF;
	private String itemId1;
	private int count;
	private int money;//单次消耗
	private int yfAdd;
	private int itemGold;
	private int itemBGold;
	private int qxCost;//取消订婚消耗
	public int getCostMoney() {
		return costMoney;
	}
	public void setCostMoney(int costMoney) {
		this.costMoney = costMoney;
	}
	public int getMaxYF() {
		return maxYF;
	}
	public void setMaxYF(int maxYF) {
		this.maxYF = maxYF;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getYfAdd() {
		return yfAdd;
	}
	public void setYfAdd(int yfAdd) {
		this.yfAdd = yfAdd;
	}
	public String getItemId1() {
		return itemId1;
	}
	public void setItemId1(String itemId1) {
		this.itemId1 = itemId1;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getItemGold() {
		return itemGold;
	}
	public void setItemGold(int itemGold) {
		this.itemGold = itemGold;
	}
	public int getItemBGold() {
		return itemBGold;
	}
	public void setItemBGold(int itemBGold) {
		this.itemBGold = itemBGold;
	}
	public int getQxCost() {
		return qxCost;
	}
	public void setQxCost(int qxCost) {
		this.qxCost = qxCost;
	}
	
	
}
