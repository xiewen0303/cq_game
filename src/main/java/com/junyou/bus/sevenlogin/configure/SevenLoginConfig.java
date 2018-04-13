package com.junyou.bus.sevenlogin.configure;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;
import com.kernel.data.dao.AbsVersion;

/**
 * 七天登陆
 * @author jy
 *
 */
public class SevenLoginConfig extends AbsVersion implements IGoodsCheckConfig {

	/**
	 * 七天登陆编号
	 */
	private Integer id;
	
	/**
	 * 累计签到奖励(公共)
	 */
	private ReadOnlyMap<String,Integer> awards;
	/**
	 * 累计签到奖励(分职业)
	 */
	private Map<Byte,Map<String,Integer>> jobAwards = new HashMap<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ReadOnlyMap<String, Integer> getAwards() {
		return awards;
	}

	public void setAwards(Map<String, Integer> awards) {
		if(awards != null){
			this.awards = new ReadOnlyMap<>(awards);
		}
	}

	public Map<String, Integer> getJobAwards(Byte job) {
		Map<String,Integer> map = new HashMap<String, Integer>();
		
		Map<String,Integer> tmp = getAwards();
		if(tmp != null && tmp.size() > 0){
			map.putAll(tmp);
		}
		//职业物品
		Map<String,Integer> jobMap = jobAwards.get(job);
		if(jobMap != null && jobMap.size() > 0){
			map.putAll(jobMap);
		}
		
		return map;
	}

	public void addJobAwards(byte job,Map<String, Integer> awards) {
		if(awards != null){
			this.jobAwards.put(job, new ReadOnlyMap<>(awards));
		}
	}

	@Override
	public String getConfigName() {
		return "SevenLoginConfig--"+getId();
	}

	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = null;
		if(awards != null && awards.size() > 0){
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(awards);
		}
		for (Map<String, Integer> map : jobAwards.values()) {
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(map);
		}
		return list;
	}
}
