package com.junyou.bus.kuafu_boss.configure;

import java.util.Map;

public class KuafuBossRankRewardConfig {
	private Integer id;
	private Integer min;
	private Integer max;
	private Map<String, Integer> jiangitem;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Map<String, Integer> getJiangitem() {
		return jiangitem;
	}

	public void setJiangitem(Map<String, Integer> jiangitem) {
		this.jiangitem = jiangitem;
	}

}
