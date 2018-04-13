package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.HashMap;
import java.util.Map;


/**
 * 寻宝公共数据表
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-2-6 下午6:16:32
 */
public class XunBaoPublicConfig extends AdapterPublicConfig{
	
	private int maxCapacity;
	private String needGoodsId;//寻宝需要消耗的物品
	private int needGold;//当没有钥匙时,需要使用元宝来抵消钥匙的消耗
	private Map<Integer,Integer> typeCount=new HashMap<>();
	private int pageSize; //寻宝仓库每页的数量总数
	private int xbjifen;//寻宝积分
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	public Integer getTimes(int type){
		return typeCount.get(type);
	}
	public String getNeedGoodsId() {
		return needGoodsId;
	}
	public void setNeedGoodsId(String needGoodsId) {
		this.needGoodsId = needGoodsId;
	}
	public int getNeedGold() {
		return needGold;
	}
	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}   
	
	public void addTypeCount(int key,int value){
		typeCount.put(key, value);
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getXbjifen() {
		return xbjifen;
	}
	public void setXbjifen(int xbjifen) {
		this.xbjifen = xbjifen;
	} 
	
	
}