package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *促销奖励领取日志
 */
public class CuxiaoRewardGetLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;  
	private JSONArray item;// 获得物品, 可为空
	private int state; //1免费领取，2元宝领取
	private int taskId;//任务id
	private int configId;//配置表id
	private int type1; //类别
	public CuxiaoRewardGetLogEvent(long userRoleId,int configId,int type1, JSONArray item,int taskId,int state ) {
		super(LogPrintHandle.CUXIAO_REWARD);
		this.userRoleId = userRoleId;
		this.item = item;
		this.state = state;
		this.taskId = taskId;
		this.configId = configId;
	}

	public int getType1() {
		return type1;
	}
	public void setType1(int type1) {
		this.type1 = type1;
	}
	 public int getConfigId() {
		return configId;
	}
	 public void setConfigId(int configId) {
		this.configId = configId;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
 
	public JSONArray getItem() {
		return item;
	}

	public void setItem(JSONArray item) {
		this.item = item;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

}