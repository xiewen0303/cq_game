package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 多人副本领奖日志
 * @author chenjianye
 * @date 2015-4-30
 */
public class MaiguFubenRewardLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public MaiguFubenRewardLogEvent(long userRoleId,String roleName,JSONArray goods,long exp,int money,int fubenId) {
		super(LogPrintHandle.MAIGU_FUBEN_AWARD);
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.goods = goods;
		this.fubenId = fubenId;
		
	}

	private long userRoleId;
	private String roleName;
	private JSONArray goods;
	private int fubenId;
	private long exp;
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
	
	public long getExp() {
		return exp;
	}

	public int getMoney() {
		return money;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}