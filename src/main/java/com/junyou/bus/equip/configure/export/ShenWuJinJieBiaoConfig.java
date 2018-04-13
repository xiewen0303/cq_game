package com.junyou.bus.equip.configure.export;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 神武装备升阶表 
 *
 * @author ZHONGDIAN
 * @date 2016-03-22 16:51:37
 */
public class ShenWuJinJieBiaoConfig extends AbsVersion {

	private String id;

	private Integer needmoney;

	private Integer successrate;

	private String nextid;

	private String needitem;
	
	private Integer count;

	private Integer gold;
	
	private Integer bgold;
	
	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getBgold() {
		return bgold;
	}

	public void setBgold(Integer bgold) {
		this.bgold = bgold;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Integer getNeedmoney() {
		return needmoney;
	}

	public void setNeedmoney(Integer needmoney) {
		this.needmoney = needmoney;
	}
	public Integer getSuccessrate() {
		return successrate;
	}

	public void setSuccessrate(Integer successrate) {
		this.successrate = successrate;
	}
	public String getNextid() {
		return nextid;
	}

	public void setNextid(String nextid) {
		this.nextid = nextid;
	}
	public String getNeeditem() {
		return needitem;
	}

	public void setNeeditem(String needitem) {
		this.needitem = needitem;
	}

	public ShenWuJinJieBiaoConfig copy(){
		return null;
	}

}
