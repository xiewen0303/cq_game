package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 装备回收
 * 
 * @author wind
 * 
 */
public class EquipRecycleLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;
	
	private JSONArray recycleEquips;
	
	private Long userRoleId; 
	private String roleName; 
	private int rlz;
//	private long jbs=0;//金币
//	private int hss=0;//魂石
//	private long exps=0;//经验
	
	
	public EquipRecycleLogEvent(JSONArray recycleEquips,
			Long userRoleId, String roleName,int rlz){// long jbs, int hss, long exps) {
		super(LogPrintHandle.EQUIP_RECYCLE);
		this.recycleEquips = recycleEquips;
		this.userRoleId = userRoleId;
		this.roleName = roleName;
//		this.jbs = jbs;
//		this.hss = hss;
//		this.exps = exps;
		this.rlz=rlz;
	}
	
	public JSONArray getRecycleEquips() {
		return recycleEquips;
	}

//	public long getJbs() {
//		return jbs;
//	}
//
//	public int getHss() {
//		return hss;
//	}
//
//	public long getExps() {
//		return exps;
//	}

	public int getRlz() {
		return rlz;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}
	public String getRoleName() {
		return roleName;
	}
}