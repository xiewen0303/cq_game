package com.junyou.bus.shoplimit.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @description 限时礼包
 */
public class XianShiLiBaoConfig extends AbsVersion{
	
	private Integer id;
	
	private Integer type ;
	
	private Integer needMoney ;
	
	private Map<String,Integer> jiangli;  

	private Integer time;
	
	private Integer totalTime;

	public Integer getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(Integer needMoney) {
		this.needMoney = needMoney;
	}
	
	public Map<String, Integer> getJiangli() {
		return jiangli;
	}

	public void setJiangli(Map<String, Integer> jiangli) {
		this.jiangli = jiangli;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
}
