package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 交易日志记录
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-19 下午2:43:05
 */
public class TradeLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	
	private long userRoleId1;//玩家1
	private String roleName1;//玩家1角色名称
	
	private long userRoleId2;//玩家2
	private String roleName2;//玩家2角色名称
	 
	private JSONArray  goods1;//玩家1 交易给 玩家2 的物品
	private long yb1;//玩家1 交易给 玩家2 的元宝
	
	private JSONArray  goods2;//玩家2 交易给 玩家1 的物品
	private long yb2;//玩家2 交易给 玩家1 的元宝 
		
	public TradeLogEvent(long userRoleId1, String roleName1,
			long userRoleId2, String roleName2, JSONArray goods1,
			JSONArray goods2, long yb1, long yb2) {
		super(LogPrintHandle.TRADE);
		this.userRoleId1 = userRoleId1;
		this.roleName1 = roleName1;
		this.userRoleId2 = userRoleId2;
		this.roleName2 = roleName2;
		this.goods1 = goods1;
		this.goods2 = goods2;
		this.yb1 = yb1;
		this.yb2 = yb2;
	}

	public long getUserRoleId1() {
		return userRoleId1;
	}

	public String getRoleName1() {
		return roleName1;
	}

	public long getUserRoleId2() {
		return userRoleId2;
	}

	public String getRoleName2() {
		return roleName2;
	}

	public JSONArray getGoods1() {
		return goods1;
	}

	public long getYb1() {
		return yb1;
	}

	public JSONArray getGoods2() {
		return goods2;
	}

	public long getYb2() {
		return yb2;
	}

}