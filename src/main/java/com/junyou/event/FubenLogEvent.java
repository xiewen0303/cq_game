package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 副本日志
 * @author LiuYu
 * @date 2015-3-30 上午10:33:38
 */
public class FubenLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	
	public FubenLogEvent(long userRoleId, String name, int fubenId,JSONArray goods, int fubenType,int exp,int money) {
		super(LogPrintHandle.FUBEN);
		this.userRoleId = userRoleId;
		this.name = name;
		this.fubenId = fubenId;
		this.goods = goods;
		this.exp = exp;
		this.money = money;
		this.fubenType = fubenType;
	}

	private long userRoleId;
	private String name;
	private int fubenId;
	private JSONArray goods;
	private int exp;
	private int money;
	private int fubenType;//挑战/扫荡
	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public int getFubenId() {
		return fubenId;
	}
	public JSONArray getGoods() {
		return goods;
	}
	public int getFubenType() {
		return fubenType;
	}
	public int getExp() {
		return exp;
	}
	public int getMoney() {
		return money;
	}

	
}