package com.junyou.bus.daomoshouzha.configure.export;

import java.util.Map;


/**
 * 
 * @description 热发布活动公共配置表 
 *
 * @author ZHONGDIAN
 * @date 2014-04-23 15:22:05
 */
public class DaoMoShouZhaConfig {

	private Integer id;
	
	private Integer quan;//权重
	
	private Map<String, Integer> itemMap;
	
	private String show;
	
	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	

}
