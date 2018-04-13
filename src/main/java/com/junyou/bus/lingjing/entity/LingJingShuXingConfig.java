package com.junyou.bus.lingjing.entity;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-6-28 下午4:13:13
 */
public class LingJingShuXingConfig {
	private int type;
	private int rank;
	private long need;
	private Map<String,Long> attribute;
	private Object[] successMsg;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public Map<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	public long getNeed() {
		return need;
	}
	public void setNeed(long need) {
		this.need = need;
	}
	public Object[] getSuccessMsg() {
		if(successMsg == null){
			successMsg = new Object[]{1,type};
		}
		return successMsg;
	}
}
