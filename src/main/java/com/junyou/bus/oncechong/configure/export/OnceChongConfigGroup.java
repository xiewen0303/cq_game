package com.junyou.bus.oncechong.configure.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 单笔充值
 */
public class OnceChongConfigGroup {
	//configId = OnceChongConfig
	private Map<Integer, OnceChongConfig> configMap = new HashMap<>();
	
	private String des;//活动描述
	private String pic;//背景图

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Map<Integer, OnceChongConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, OnceChongConfig> configMap) {
		this.configMap = configMap;
	}
}