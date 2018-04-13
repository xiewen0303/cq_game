package com.junyou.bus.huiyanshijin.configue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description 老玩家回归活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class HuiYanShiJingConfigGroup {
	
	private Map<Integer, HuiYanShiJingConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private Integer maxGold;//最高可获得多少元宝
	
	private Map<Integer, Integer> goldMap = new HashMap<>();//MAP<挖矿级别，挖矿所需元宝>
	
	private List<Object[]> goldList = new ArrayList<>();//每个级别消耗的元宝，给客服端的
	private List<Object[]> iconList = new ArrayList<>();//每个矿石对应的icon，给客服端的
	
	private Integer djCount;//全服大奖次数
	
	public Integer getDjCount() {
		return djCount;
	}

	public void setDjCount(Integer djCount) {
		this.djCount = djCount;
	}

	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Map<Integer, HuiYanShiJingConfig> getConfigMap() {
		return configMap;
	}
	public void setConfigMap(Map<Integer, HuiYanShiJingConfig> configMap) {
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
	public Map<Integer, Integer> getGoldMap() {
		return goldMap;
	}
	public void setGoldMap(Map<Integer, Integer> goldMap) {
		this.goldMap = goldMap;
	}
	public List<Object[]> getGoldList() {
		return goldList;
	}
	public void setGoldList(List<Object[]> goldList) {
		this.goldList = goldList;
	}
	public List<Object[]> getIconList() {
		return iconList;
	}
	public void setIconList(List<Object[]> iconList) {
		this.iconList = iconList;
	}

	public Integer getMaxGold() {
		return maxGold;
	}

	public void setMaxGold(Integer maxGold) {
		this.maxGold = maxGold;
	}
	
	
	
	
}
