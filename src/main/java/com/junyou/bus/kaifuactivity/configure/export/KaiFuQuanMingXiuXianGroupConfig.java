package com.junyou.bus.kaifuactivity.configure.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class KaiFuQuanMingXiuXianGroupConfig {
	
	private Map<Integer, KaiFuQuanMingXiuXianConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//活动底图
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Map<Integer, KaiFuQuanMingXiuXianConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, KaiFuQuanMingXiuXianConfig> configMap) {
		this.configMap = configMap;
	}
}
