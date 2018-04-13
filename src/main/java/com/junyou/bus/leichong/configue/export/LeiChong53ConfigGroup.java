package com.junyou.bus.leichong.configue.export;

import java.util.HashMap;
import java.util.Map;

/**
 *	充值返利53  
 */
public class LeiChong53ConfigGroup {
	
	private Map<Integer, LeiChong53Config> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private int max;
	
	public Map<Integer, LeiChong53Config> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, LeiChong53Config> configMap) {
		this.configMap = configMap;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

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
}
