package com.junyou.bus.lingjing.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.math.BitOperationUtil;

/**
 * @author LiuYu
 * 2015-6-28 下午6:50:04
 */
public class LingJingShuXingCengConfig implements IGoodsCheckConfig{
	private int rank;
	private Map<Integer,LingJingShuXingConfig> configs = new HashMap<>();
	private Map<String,Long> attribute;
	private Map<String,Integer> items;
	private Object[] successMsg;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public void setConfig(LingJingShuXingConfig config) {
		this.configs.put(config.getType(), config);
	}
	/**
	 * 获取所有类型
	 * @return
	 */
	public List<Integer> getTypes(){
		return new ArrayList<>(configs.keySet());
	}
	
	public Map<String, Long> getAttribute(Integer state) {
		Map<String,Long> attribute = new HashMap<>();
		if(this.attribute != null){
			ObjectUtil.longMapAdd(attribute, this.attribute);
		}
		
		for (Entry<Integer,LingJingShuXingConfig> entry : configs.entrySet()) {
			int i = entry.getKey();
			LingJingShuXingConfig config = entry.getValue();
			if(config != null && !BitOperationUtil.calState(state, i-1)){
				ObjectUtil.longMapAdd(attribute,config.getAttribute());
			}
		}
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	public Map<String, Integer> getItems() {
		return items;
	}
	public void setItems(Map<String, Integer> items) {
		this.items = new ReadOnlyMap<>(items);
	}
	@Override
	public String getConfigName() {
		return "LingJingShuXingCengConfig--" + rank;
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		if(items == null || items.size() < 1){
			return null;
		}
		List<Map<String, Integer>> list = new ArrayList<>();
		list.add(items);
		return list;
	}
	public LingJingShuXingConfig getConfig(Integer type){
		if(type == null || configs == null || configs.size() < 1){
			return null;
		}
		return configs.get(type);
	}
	public Object[] getSuccessMsg() {
		if(successMsg == null){
			successMsg = new Object[]{1,rank + 1};
		}
		return successMsg;
	}
	
	
}
