package com.junyou.bus.kaifuactivity.configure.export;

import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 (全民修仙)
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class KaiFuQuanMingXiuXianConfig {
	
	private Map<String, Integer> jiangitem;
	private Object[] itemClientMap;
	
	private Integer value1;

	private Integer id;

	private Integer shuliang;

	public Map<String, Integer> getJiangitem() {
		return jiangitem;
	}

	public void setJiangitem(Map<String, Integer> jiangitem) {
		this.jiangitem = jiangitem;
	}

	public Object[] getItemClientMap() {
		
		return itemClientMap;
	}
	public void setItemClientMap(Object[] itemClientMap) {
		this.itemClientMap = itemClientMap;
	}

	public Integer getValue1() {
		return value1;
	}

	public void setValue1(Integer value1) {
		this.value1 = value1;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShuliang() {
		return shuliang;
	}

	public void setShuliang(Integer shuliang) {
		this.shuliang = shuliang;
	}


	
}
