package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 热发布秒杀活动
 * @author LiuYu
 * @date 2015-3-30 上午10:33:38
 */
public class MiaoShaLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	
	public MiaoShaLogEvent(long userRoleId, String name, int subId,int id, int gold, boolean first, boolean top10, JSONArray items) {
		super(LogPrintHandle.REFABU_MIAOSHA);
		this.userRoleId = userRoleId;
		this.name = name;
		this.subId = subId;
		this.id = id;
		this.gold = gold;
		this.first = first;
		this.top10 = top10;
		this.items = items;
	}
	private long userRoleId;
	private String name;
	private int subId;	//子活动id
	private int id;		//时段id
	private int gold;	//花费元宝
	private boolean first;	//是否是第一个抽中极品奖励的人
	private boolean top10;	//是否是前十个秒杀的人
	private JSONArray items;	//获得的奖励

	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public int getSubId() {
		return subId;
	}
	public int getId() {
		return id;
	}
	public int getGold() {
		return gold;
	}
	public boolean isFirst() {
		return first;
	}
	public boolean isTop10() {
		return top10;
	}
	public JSONArray getItems() {
		return items;
	}
	

	
	
}