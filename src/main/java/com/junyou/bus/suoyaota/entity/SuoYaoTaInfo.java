package com.junyou.bus.suoyaota.entity;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;
/**
 * 锁妖塔物品奖励信息
 * @author LiuYu
 * @date 2015-8-3 下午4:15:36
 */
public class SuoYaoTaInfo {
	
	private long userRoleId;
	private Map<Integer,Map<Integer,GoodsConfigureVo>> items;
	private Object[] clientItems;
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Map<Integer, Map<Integer, GoodsConfigureVo>> getItems() {
		return items;
	}
	public void setItems(Map<Integer, Map<Integer, GoodsConfigureVo>> items) {
		this.items = items;
	}
	public Object[] getClientItems() {
		return clientItems;
	}
	public void setClientItems(Object[] clientItems) {
		this.clientItems = clientItems;
	}
	
}
