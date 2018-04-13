package com.junyou.bus.lunpan.configure.export;

import java.util.Map;


/**
 * 
 * @description 热发布活动公共配置表 
 *
 * @author ZHONGDIAN
 * @date 2014-04-23 15:22:05
 */
public class LunPanConfig {

	private Integer id;
	
	private Integer type;
	
	private Integer quan;//权重
	
	private Map<String, Integer> itemMap;
	
	private Integer jifen;

	private Map<String, Integer> duiHuanMap;
	
	private String goodId;
	private Integer goodCount;
	
	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public Integer getGoodCount() {
		return goodCount;
	}

	public void setGoodCount(Integer goodCount) {
		this.goodCount = goodCount;
	}

	public Map<String, Integer> getDuiHuanMap() {
		return duiHuanMap;
	}

	public void setDuiHuanMap(Map<String, Integer> duiHuanMap) {
		this.duiHuanMap = duiHuanMap;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getQuan() {
		return quan;
	}

	public void setQuan(Integer quan) {
		this.quan = quan;
	}

	public Map<String, Integer> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Integer> itemMap) {
		this.itemMap = itemMap;
	}

	public Integer getJifen() {
		return jifen;
	}

	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}

	
	

}
