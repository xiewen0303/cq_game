package com.junyou.bus.happycard.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HappyCardGroupConfig {
	private int chongzhi;
	private String desc;
	private Map<Integer, HappyCardConfig> configMap = new HashMap<Integer, HappyCardConfig>();
	private List<Integer> idList = new ArrayList<Integer>();
	private Object[] ybArray;
	private Object[] vo;

	public int getChongzhi() {
		return chongzhi;
	}

	public void setChongzhi(int chongzhi) {
		this.chongzhi = chongzhi;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Map<Integer, HappyCardConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, HappyCardConfig> configMap) {
		this.configMap = configMap;
	}

	private String md5Version;

	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public HappyCardConfig getConfig(Integer id) {
		return configMap.get(id);
	}

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

	public Object[] getYbArray() {
		return ybArray;
	}

	public void setYbArray(Object[] ybArray) {
		this.ybArray = ybArray;
	}

	public int getTimes(int ybNum) {
		int idListSize = idList.size();
		int ret = 0;
		int left = ybNum;
		for (int i = 0; i < idListSize; i++) {
			if (left < 0) {
				break;
			}
			Integer id = idList.get(i);
			HappyCardConfig config = getConfig(id);
			if (left >= config.getMoney()) {
				ret = id;
			}
			left = left - config.getMoney();
		}
		return ret;
	}
}
