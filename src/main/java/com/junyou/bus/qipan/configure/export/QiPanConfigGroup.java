package com.junyou.bus.qipan.configure.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class QiPanConfigGroup {
	
	private Map<Integer, QiPanConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private Integer xfValue;
	private Integer maxCount;

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public Integer getXfValue() {
		return xfValue;
	}

	public void setXfValue(Integer xfValue) {
		this.xfValue = xfValue;
	}

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

	public Map<Integer, QiPanConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, QiPanConfig> configMap) {
		this.configMap = configMap;
	}
	
	
}
