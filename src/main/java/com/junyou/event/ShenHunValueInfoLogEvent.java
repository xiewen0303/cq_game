package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 神魂值消耗,获得信息
 * 满级了玩家神魂值不会增加，但是日志照常有
 * @date 2015-6-5
 */
public class ShenHunValueInfoLogEvent extends AbsGameLogEvent {
	private static final long serialVersionUID = 1L;
	private long userRoleId;
	private int category;// 0消耗神魂值，1获得神魂值
	private int way;  //获得|消耗的方式,0消耗神魂丹道具获得，1竞技场获得，2升级消耗
	private int value;  //神魂值
	private int level;//当前等级

	public ShenHunValueInfoLogEvent(long userRoleId, int category, int value,int way,int level) {
		super(LogPrintHandle.SHEN_HUN_VALUE_INFO_LOG);
		this.category = category;
		this.userRoleId = userRoleId;
		this.value = value;
		this.way = way;
		this.level  = level;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getWay() {
		return way;
	}
	public void setWay(int way) {
		this.way = way;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

 

 

}