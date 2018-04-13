package com.junyou.bus.kuafuluandou.configure;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 跨服大乱斗奖励表 
 *
 * @author ZHONGDIAN
 * @date 2016-02-17 15:16:10
 */
public class DaLuanDouJiangLiBiaoConfig extends AbsVersion{

	private Integer min;

	private Integer id;

	private Integer max;

	private Integer jiangitem;

	private Integer type;


	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}
	public Integer getJiangitem() {
		return jiangitem;
	}

	public void setJiangitem(Integer jiangitem) {
		this.jiangitem = jiangitem;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public DaLuanDouJiangLiBiaoConfig copy(){
		return null;
	}


}
