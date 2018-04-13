package com.junyou.bus.chongwu.configure.export;

import java.util.Map;

public class ChongWuShengJiBiaoConfig {
	private int cwlevel;
	private long cwexp;
	private Map<String, Long> attrs;
	public int getCwlevel() {
		return cwlevel;
	}
	public void setCwlevel(int cwlevel) {
		this.cwlevel = cwlevel;
	}
	public long getCwexp() {
		return cwexp;
	}
	public void setCwexp(long cwexp) {
		this.cwexp = cwexp;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	
}
