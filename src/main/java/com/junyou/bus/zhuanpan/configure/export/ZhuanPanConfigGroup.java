package com.junyou.bus.zhuanpan.configure.export;

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
public class ZhuanPanConfigGroup {
	
	private Map<Integer, ZhuanPanConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private Integer gold;//转一次消耗的元宝数
	private String needitem;//转一次可以消耗的道具ID
	private List<Object[]> goldList;//可配次数元宝消耗
	private Integer maxCount = 9999;
	private Integer maxGe;//最大格位
	
	private List<Object[]> duiHuanData;//兑换数据
	
	private Map<Integer, Integer> zpMap;//转盘Map
	
	public Map<Integer, Integer> getZpMap() {
		return zpMap;
	}
	public void setZpMap(Map<Integer, Integer> zpMap) {
		this.zpMap = zpMap;
	}
	public List<Object[]> getDuiHuanData() {
		return duiHuanData;
	}
	public void setDuiHuanData(List<Object[]> duiHuanData) {
		this.duiHuanData = duiHuanData;
	}
	public Integer getMaxGe() {
		return maxGe;
	}
	public void setMaxGe(Integer maxGe) {
		this.maxGe = maxGe;
	}
	public String getNeeditem() {
		return needitem;
	}
	public void setNeeditem(String needitem) {
		this.needitem = needitem;
	}
	public Map<Integer, ZhuanPanConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<Integer, ZhuanPanConfig> configMap) {
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
	
	public List<Object[]> getGoldList() {
		return goldList;
	}
	public void setGoldList(List<Object[]> goldList) {
		this.goldList = goldList;
	}



	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}



	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	
	
}
