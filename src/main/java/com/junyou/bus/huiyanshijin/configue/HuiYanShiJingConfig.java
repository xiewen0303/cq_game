package com.junyou.bus.huiyanshijin.configue;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @description 慧眼识金
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class HuiYanShiJingConfig {
	
	
	
	private Integer id;
	private Integer rank;//挖矿等级
	private Integer minCount;//全服次数
	private Integer maxCount;//全服次数
	
	private Map<Object[], Integer> jlMap = new HashMap<Object[], Integer>();//组包MAP

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getMinCount() {
		return minCount;
	}

	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public Map<Object[], Integer> getJlMap() {
		return jlMap;
	}

	public void setJlMap(Map<Object[], Integer> jlMap) {
		this.jlMap = jlMap;
	}
	
	
}
