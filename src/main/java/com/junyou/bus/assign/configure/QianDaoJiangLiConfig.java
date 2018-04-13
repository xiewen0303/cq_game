package com.junyou.bus.assign.configure;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;
import com.kernel.data.dao.AbsVersion;

/**
 * 签到基础配置
 * @author jy
 *
 */
public class QianDaoJiangLiConfig extends AbsVersion implements IGoodsCheckConfig {

	/**
	 * 礼包ID
	 */
	private Integer id;

	/**
	 * 累计签到次数
	 */
	private Integer count;
	
	/**
	 * 累计签到奖励
	 */
	private ReadOnlyMap<String,Integer> awards;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public ReadOnlyMap<String, Integer> getAwards() {
		return awards;
	}

	public void setAwards(ReadOnlyMap<String, Integer> awards) {
		this.awards = awards;
	}

	@Override
	public String getConfigName() {
		return "AssignConfig--"+getId();
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
		return list;
	}
}
