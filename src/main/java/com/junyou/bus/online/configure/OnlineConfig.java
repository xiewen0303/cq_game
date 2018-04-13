package com.junyou.bus.online.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.kernel.data.dao.AbsVersion;

/**
 * 在线奖励基础配置
 * @author jy
 *
 */
public class OnlineConfig extends AbsVersion implements IGoodsCheckConfig {

	private Integer id;
	private Integer time;
	private Map<Byte,Float> weights;
	private Map<Byte,Map<String,Integer>> rewardItems;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Map<Byte, Float> getWeights() {
		return weights;
	}

	public void setWeights(Map<Byte, Float> weights) {
		this.weights = weights;
	}

	public Map<Byte, Map<String, Integer>> getRewardItems() {
		return rewardItems;
	}

	public void setRewardItems(Map<Byte, Map<String, Integer>> rewardItems) {
		this.rewardItems = rewardItems;
	}

	@Override
	public String getConfigName() {
		return "OnlineConfig--"+getId();
	}

	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = null;
		if(rewardItems!=null && rewardItems.size()>0){
			for(Entry<Byte,Map<String,Integer>> entry:rewardItems.entrySet()){
				if(list == null){
					list = new ArrayList<>();
				}
				list.add(entry.getValue());
			}
		}
		return list;
	}
}
