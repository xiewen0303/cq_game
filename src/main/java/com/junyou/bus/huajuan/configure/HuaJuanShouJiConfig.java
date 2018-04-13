package com.junyou.bus.huajuan.configure;

import java.util.Map;

public class HuaJuanShouJiConfig {
	private int liebiaoid;
	private int type;
	// 品质
	private int num;
	private Map<String, Long> attrs;
	public int getLiebiaoid() {
		return liebiaoid;
	}
	public void setLiebiaoid(int liebiaoid) {
		this.liebiaoid = liebiaoid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

}
