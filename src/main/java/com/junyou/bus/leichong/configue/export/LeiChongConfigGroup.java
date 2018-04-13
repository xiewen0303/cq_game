package com.junyou.bus.leichong.configue.export;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class LeiChongConfigGroup {
	
	private Map<Integer, LeiChongConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String des2;//活动描述
	private String pic;//背景图
	private Integer bcGold;//补充1天的金额
	private Map<Integer, Map<String,Integer>> dayMap = new HashMap<>();
	private Object[] dayClient;//天数奖励给客户端
	private Integer lcday;//累充天数
	private String sevenItem;//连冲7天的大奖
	private Object[] severClient;//连冲7天的大奖
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}


	public String getDes2() {
		return des2;
	}

	public void setDes2(String des2) {
		this.des2 = des2;
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

	public Map<Integer, LeiChongConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, LeiChongConfig> configMap) {
		this.configMap = configMap;
	}

	public Integer getBcGold() {
		return bcGold;
	}

	public void setBcGold(Integer bcGold) {
		this.bcGold = bcGold;
	}

	public Map<Integer, Map<String, Integer>> getDayMap() {
		return dayMap;
	}

	public void setDayMap(Map<Integer, Map<String, Integer>> dayMap) {
		this.dayMap = dayMap;
	}
	

	public Integer getLcday() {
		return lcday;
	}

	public void setLcday(Integer lcday) {
		this.lcday = lcday;
	}

	public Object[] getDayClient() {
		return dayClient;
	}

	public void setDayClient(Object[] dayClient) {
		this.dayClient = dayClient;
	}


	public String getSevenItem() {
		return sevenItem;
	}

	public void setSevenItem(String sevenItem) {
		this.sevenItem = sevenItem;
	}

	public Object[] getSeverClient() {
		return severClient;
	}

	public void setSeverClient(Object[] severClient) {
		this.severClient = severClient;
	}

	
	
}
