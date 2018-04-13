package com.junyou.bus.wenquan.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class WenQuanPaiMingJiangLiConfig extends AbsVersion {
	private int min;
	private int max;
	private Map<String, Integer> jiangitem;

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public Map<String, Integer> getJiangitem() {
		return jiangitem;
	}

	public void setJiangitem(Map<String, Integer> jiangitem) {
		this.jiangitem = jiangitem;
	}

}
