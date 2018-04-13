package com.junyou.bus.superduihuan.configure.export;

import java.util.HashMap;
import java.util.Map;


public class SuperDuihuanGroupConfig{
	private String bg;
	private String desc;
	private Map<Integer, SuperDuihuanConfig> configMap = new HashMap<Integer, SuperDuihuanConfig>();
	
	private Object[] vo;
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
	public Map<Integer, SuperDuihuanConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<Integer, SuperDuihuanConfig> configMap) {
		this.configMap = configMap;
	}
	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	public Object[] getVo() {
		return vo;
	}
	public void setVo(Object[] vo) {
		this.vo = vo;
	}
	
	public SuperDuihuanConfig getConfig(Integer id){
		return configMap.get(id);
	}
	
}
