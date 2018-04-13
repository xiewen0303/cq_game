package com.junyou.event;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 通天之路淬炼
 * @date 2015-6-5
 */
public class TongtianRoadCuilianLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray consumeItem;// 消耗物品,这里可为空数组[]
	private Map<String, Long> jiangliAttrMap; //获得的属性,可以为空。只有淬炼值达到最大值才会奖励属性
	private int addCuilian; //此次淬炼获取的淬炼值
	private int totalCuilian;//此次淬炼总值
	private int consumeCuilian;//此次消耗掉的淬炼值
	

	public TongtianRoadCuilianLogEvent(long userRoleId, JSONArray consumeItem, Map<String, Long> jiangliAttrMap,int addCuilian,int totalCuilian,int consumeCuilian ) {
		super(LogPrintHandle.TONGTIAN_ROAD_LOG);
		this.userRoleId = userRoleId;
		this.consumeItem = consumeItem;
		this.jiangliAttrMap  = jiangliAttrMap;
		this.addCuilian  = addCuilian;
		this.totalCuilian= totalCuilian;
		this.consumeCuilian = consumeCuilian;
	}

	public int getAddCuilian() {
		return addCuilian;
	}
	public void setAddCuilian(int addCuilian) {
		this.addCuilian = addCuilian;
	}
	public int getTotalCuilian() {
		return totalCuilian;
	}
	public void setTotalCuilian(int totalCuilian) {
		this.totalCuilian = totalCuilian;
	}
	public int getConsumeCuilian() {
		return consumeCuilian;
	}
	public void setConsumeCuilian(int consumeCuilian) {
		this.consumeCuilian = consumeCuilian;
	}
	public long getUserRoleId() {
		return userRoleId;
	}

	public Map<String, Long> getJiangliAttrMap() {
		return jiangliAttrMap;
	}
	public void setJiangliAttrMap(Map<String, Long> jiangliAttrMap) {
		this.jiangliAttrMap = jiangliAttrMap;
	}
	public JSONArray getConsumeItem() {
		return consumeItem;
	}

	public void setConsumeItem(JSONArray consumeItem) {
		this.consumeItem = consumeItem;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}