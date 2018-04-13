package com.junyou.bus.leihao.configure.export;

import java.util.HashMap;
import java.util.Map;

public class LeiHaoGroupConfig {
	private String bg;
	private String desc;
	private Map<Integer, LeiHaoConfig> configMap = new HashMap<Integer, LeiHaoConfig>();
	private Map<Integer, LeiHaoBGoldConfig> bGoldConfigMap = new HashMap<Integer, LeiHaoBGoldConfig>();

	private Object[] vo;
	private Object[] dayVo;
	
	private String md5Version;

	public String getBg() {
		return bg;
	}

	public void setBg(String bg) {
		this.bg = bg;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Map<Integer, LeiHaoConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, LeiHaoConfig> configMap) {
		this.configMap = configMap;
	}
	
	
	public Map<Integer, LeiHaoBGoldConfig> getbGoldConfigMap() {
		return bGoldConfigMap;
	}

	public void setbGoldConfigMap(Map<Integer, LeiHaoBGoldConfig> bGoldConfigMap) {
		this.bGoldConfigMap = bGoldConfigMap;
	}

	public Object[] getVo() {
		return vo;
	}

	public void setVo(Object[] vo) {
		this.vo = vo;
	}

	public Object[] getDayVo() {
		return dayVo;
	}

	public void setDayVo(Object[] dayVo) {
		this.dayVo = dayVo;
	}

	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

}
