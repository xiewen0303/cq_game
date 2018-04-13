package com.junyou.bus.daomoshouzha.configure.export;

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
public class DaoMoShouZhaConfigGroup {
	
	private Map<Integer, DaoMoShouZhaConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private Integer gold;//转一次消耗的元宝数
	private Integer maxCount = 9999;
	private List<Object[]> showList;//物品显示

	private Map<Integer, Integer> zpMap;//转盘Map
	
	public Map<Integer, Integer> getZpMap() {
		return zpMap;
	}
	public void setZpMap(Map<Integer, Integer> zpMap) {
		this.zpMap = zpMap;
	}
	public Map<Integer, DaoMoShouZhaConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<Integer, DaoMoShouZhaConfig> configMap) {
		this.configMap = configMap;
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
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	
	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	public List<Object[]> getShowList() {
		return showList;
	}
	public void setShowList(List<Object[]> showList) {
		this.showList = showList;
	}
	
}
