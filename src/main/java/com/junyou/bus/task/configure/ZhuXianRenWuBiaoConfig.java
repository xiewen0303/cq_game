package com.junyou.bus.task.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 
 * @description 主线任务表配置 
 *
 * @author LiNing
 * @date 2013-10-24 10:52:30
 */
public class ZhuXianRenWuBiaoConfig implements IGoodsCheckConfig{

	private String data1;

	private Integer data2;

	private Integer type;

	private Integer id;

	private Long jiangexp;

	private Integer minlevel;

	private Long jiangmoney;

	private Long zhenqi;
	
	private String name;
	
	private boolean rizhi;
	
	//奖励物品
	private Map<String,Integer> rewardItems;
	
	//职业专属奖励物品
	private ReadOnlyMap<Byte,Map<String,Integer>> jobAward;

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}
	public Integer getData2() {
		return data2;
	}

	public void setData2(Integer data2) {
		this.data2 = data2;
	}
	/**
	 * 0.对话无条件
	 * 1.杀怪 data1:怪物ID data2:杀怪数量
	 * 2.采集 data1:采集物ID data2:采集数量
	 * @return
	 */
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Long getJiangexp() {
		return jiangexp;
	}

	public void setJiangexp(Long jiangexp) {
		this.jiangexp = jiangexp;
	}
	public Integer getMinlevel() {
		return minlevel;
	}

	public void setMinlevel(Integer minlevel) {
		this.minlevel = minlevel;
	}
	public Long getJiangmoney() {
		return jiangmoney;
	}

	public void setJiangmoney(Long jiangmoney) {
		this.jiangmoney = jiangmoney;
	}
	
	public Map<String, Integer> getRewardItems() {
		return rewardItems;
	}

	public void setRewardItems(Map<String, Integer> rewardItems) {
		this.rewardItems = rewardItems;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getZhenqi() {
		return zhenqi;
	}

	public void setZhenqi(Long zhenqi) {
		this.zhenqi = zhenqi;
	}

	/**
	 * 职业专属奖励物品  key:job  value map物品
	 * @return
	 */
	public Map<Byte, Map<String, Integer>> getJobAward() {
		return jobAward;
	}

	public void setJobAward(Map<Byte, Map<String, Integer>> jobAward) {
		this.jobAward = new ReadOnlyMap<>(jobAward);
	}
	
	public ZhuXianRenWuBiaoConfig copy(){
		return null;
	}

	/**
	 * 获取所有任务奖励
	 * @param job
	 * @return
	 */
	public Map<String,Integer> getAllAwardItemByJob(Integer job){
		Map<String,Integer> map = new HashMap<String, Integer>();
		
		Map<String,Integer> tmp = getRewardItems();
		if(tmp != null && tmp.size() > 0){
			map.putAll(tmp);
		}
		//职业物品
		if(getJobAward() != null){
			Map<String,Integer> jobMap = getJobAward().get(job.byteValue());
			if(jobMap != null && jobMap.size() > 0){
				map.putAll(jobMap);
			}
		}
		
		return map;
	}

	public boolean isRizhi() {
		return rizhi;
	}

	public void setRizhi(boolean rizhi) {
		this.rizhi = rizhi;
	}

	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = null;
		if(jobAward != null && jobAward.size() > 0){
			list = new ArrayList<>(jobAward.values());
		}
		if(rewardItems != null && rewardItems.size() > 0){
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(rewardItems);
		}
		return list;
	}

	@Override
	public String getConfigName() {
		return "ZhuXianRenWuBiaoConfig--" + id;
	}
	
	
	
}
