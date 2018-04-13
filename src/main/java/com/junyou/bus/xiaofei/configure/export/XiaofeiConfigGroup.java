package com.junyou.bus.xiaofei.configure.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class XiaofeiConfigGroup {
	
	private Map<Integer, XiaofeiConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private Integer MaxPeople = 30;//排名最大人数 
	
	private List<Object[]> dataList;
	
	public List<Object[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<Object[]> dataList) {
		this.dataList = dataList;
	}

	public Integer getMaxPeople() {
		return MaxPeople;
	}

	public void setMaxPeople(Integer maxPeople) {
		MaxPeople = maxPeople;
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

	public Map<Integer, XiaofeiConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, XiaofeiConfig> configMap) {
		this.configMap = configMap;
	}
	
	
}
