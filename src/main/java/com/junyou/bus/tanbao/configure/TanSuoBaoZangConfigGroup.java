package com.junyou.bus.tanbao.configure;

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
public class TanSuoBaoZangConfigGroup {
	
	private Map<Integer, TanSuoBaoZangConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private String showItem;//展示道具ID
	
	private List<Object[]> goldList;//探索需要花费的元宝集合
	
	private List<Object[]> wangChengList;//探索王城奖励
	
	private Integer open;//直接开启的格位
	
	private Integer openGold;//直接开启需要消耗的元宝
	
	private String md5Version;
	
	private Map<Integer, Object[]> wangChengItem;//探索王城领取物品
	
	private Integer maxCount = 9999;
	
	public Map<Integer, Object[]> getWangChengItem() {
		return wangChengItem;
	}
	public Object[] getWangChengInfo(Integer suoyin){
		return wangChengItem.get(suoyin);
	}
	
	public void setWangChengItem(Map<Integer, Object[]> wangChengItem) {
		this.wangChengItem = wangChengItem;
	}

	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	public Map<Integer, TanSuoBaoZangConfig> getConfigMap() {
		return configMap;
	}
	
	public TanSuoBaoZangConfig getTanSuoBaoZangConfig(Integer suoyin){
		return configMap.get(suoyin);
	}
	

	public void setConfigMap(Map<Integer, TanSuoBaoZangConfig> configMap) {
		this.configMap = configMap;
	}

	public String getShowItem() {
		return showItem;
	}
	public void setShowItem(String showItem) {
		this.showItem = showItem;
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

	public List<Object[]> getGoldList() {
		return goldList;
	}

	public void setGoldList(List<Object[]> goldList) {
		this.goldList = goldList;
	}

	public List<Object[]> getWangChengList() {
		return wangChengList;
	}

	public void setWangChengList(List<Object[]> wangChengList) {
		this.wangChengList = wangChengList;
	}

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}

	public Integer getOpenGold() {
		return openGold;
	}

	public void setOpenGold(Integer openGold) {
		this.openGold = openGold;
	}

	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
}
