package com.junyou.bus.kuafuarena1v1.configure.export;

import com.kernel.data.dao.AbsVersion;

public class GongxunDuihuanConfig extends AbsVersion {
	private Integer order;

	private String item;

	private int needgx;

	private int needdw;

	private int maxcount;

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getNeedgx() {
		return needgx;
	}

	public void setNeedgx(int needgx) {
		this.needgx = needgx;
	}

	public int getNeeddw() {
		return needdw;
	}

	public void setNeeddw(int needdw) {
		this.needdw = needdw;
	}

	public int getMaxcount() {
		return maxcount;
	}

	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}

}
