package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 *  妖神附魔
 * @date 2015-6-5
 */
public class YaoshenFumoSjLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private JSONArray item;// 消耗物品
	private int consumeMoney; //消耗的银两
	private int jie;//霸体阶数
	private int index;//升级的格位
	private int indexLevel;//格位升级的等级
	private int caowei;
	
	
	

	public YaoshenFumoSjLogEvent(long userRoleId,int jie,int caowei,int index ,int indexLevel, JSONArray item,int consumeMoney ) {
		super(LogPrintHandle.YAOSHEN_HUMO_SJ);
		this.userRoleId = userRoleId;
		this.item = item;
		this.jie = jie;
		this.caowei = caowei;
		this.index=index;
		this.indexLevel = indexLevel;
		this.consumeMoney = consumeMoney;
	}
	public int getCaowei() {
		return caowei;
	}
	public void setCaowei(int caowei) {
		this.caowei = caowei;
	}
	public int getConsumeMoney() {
		return consumeMoney;
	}

	public void setConsumeMoney(int consumeMoney) {
		this.consumeMoney = consumeMoney;
	}
	public int getJie() {
		return jie;
	}

	public void setJie(int jie) {
		this.jie = jie;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndexLevel() {
		return indexLevel;
	}

	public void setIndexLevel(int indexLevel) {
		this.indexLevel = indexLevel;
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