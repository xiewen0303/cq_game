package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 多人副本领奖日志
 * @author chenjianye
 * @date 2015-4-30
 */
public class MoreFubenRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public MoreFubenRewardLogEvent(long userRoleId,String roleName,JSONArray goods,int exp,int money,int fubenId) {
		super(LogPrintHandle.MORE_FUBEN_AWARD);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.goods = goods;
		this.fubenId = fubenId;
		
	}

	private long userRoleId;
	private String roleName;
	private JSONArray goods;
	private int fubenId;
	private int exp;
	private int money;

	public long getUserRoleId() {
		return userRoleId;
	}

	public int getFubenId() {
		return fubenId;
	}

	public String getRoleName() {
		return roleName;
	}

	public JSONArray getGoods() {
		return goods;
	}
	
	public int getExp() {
		return exp;
	}

	public int getMoney() {
		return money;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}