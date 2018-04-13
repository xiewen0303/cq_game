package com.junyou.bus.huanhua.configure;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;
public class YuJianHuanHuaBiaoConfig extends AbsVersion {
	private int id;
	private String needitem;
	private String name;
	private Map<String, Long> attrs;// 基础属性
	private boolean czd = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNeeditem() {
		return needitem;
	}

	public void setNeeditem(String needitem) {
		this.needitem = needitem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public boolean isCzd() {
		return czd;
	}

	public void setCzd(boolean czd) {
		this.czd = czd;
	}

}
