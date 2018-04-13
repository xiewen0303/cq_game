package com.junyou.bus.shenqi.configure.export;

import java.util.Map;

public class ShuXingZhuFuConfig {
	private Integer leixing;
	private Integer level;
	private Map<String, Long> attrs;

	public Integer getLeixing() {
		return leixing;
	}

	public void setLeixing(Integer leixing) {
		this.leixing = leixing;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

}
