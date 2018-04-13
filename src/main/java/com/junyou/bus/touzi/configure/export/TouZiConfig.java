package com.junyou.bus.touzi.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 
 * @description 投资计划配置 
 *
 * @author LiNing
 * @date 2015-06-15 16:27:40
 */
public class TouZiConfig{

	private int id;
	private int type;    //类型
	private int gold;
	private int startDay;//开始天数（开服第几天，包括开服当天）
	private int endDay;//结束天数
	private int maxDay;//最大天数
	private String name;
	private ReadOnlyMap<Integer, Map<String, Integer>> itemMap;//Map<天，奖励物品Map>
	private Object[] clientItemMap;//所有奖励物品[]

	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}
	
	/**
	 * 
	 * @param reDay 领奖天数
	 * @param lDay  登录天数
	 * @return
	 */
	public Map<String, Integer> getItemMap(int reDay, int lDay) {
		if(itemMap == null || itemMap.size() <= 0){
			return null;
		}
		
		Map<String, Integer> dataMap = new HashMap<>();
		for (int i = reDay + 1; i <= lDay; i++) {
			Map<String, Integer> newMap = itemMap.get(i);
			dataMap = getCovItemMap(dataMap, newMap);
		}
		
		return dataMap;
	}
	
	/**
	 * 天数
	 * day
	 * @return
	 */
	public Map<String, Integer> getItemMap(int day) {
		if(itemMap == null || itemMap.size() <= 0){
			return null;
		}
		return  itemMap.get(day);
	}
	
	/**
	 * 
	 * @param reDay 领奖天数
	 * @param lDay  登录天数
	 * @return
	 */
	public Map<String, Integer> getItemMap() {
		if(itemMap == null || itemMap.size() <= 0){
			return null;
		}
		
		Map<String, Integer> dataMap = new HashMap<>();
		for (Map<String,Integer> newMap : itemMap.values()) {
			dataMap = getCovItemMap(dataMap, newMap);
		}
		return dataMap;
	}

	public void setItemMap(ReadOnlyMap<Integer, Map<String, Integer>> itemMap) {
		this.itemMap = itemMap;
	}
	
	public Object[] getClientItemMap() {
		return clientItemMap;
	}

	public void setClientItemMap(Object[] clientItemMap) {
		this.clientItemMap = clientItemMap;
	}

	/**
	 * 当前天数是否在投资期间
	 * @return true:在期间
	 */
	public boolean isRunTouzi(){
		int kfDays = ServerInfoServiceManager.getInstance().getKaifuDays();
		if(this.startDay <= kfDays && (this.endDay <= 0 || kfDays <= this.endDay)){
			return true;
		}
		return false;
	}

	public int getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(int maxDay) {
		if(maxDay <= 0){
			return;
		}
		this.maxDay = maxDay;
	}

	public TouZiConfig copy(){
		return null;
	}
	
	/**
	 * 获取最后奖励的物品
	 * @param oldMap 上一个奖励物品
	 * @param newMap 新的奖励物品
	 * @return
	 */
	public Map<String, Integer> getCovItemMap(Map<String, Integer> oldMap, Map<String, Integer> newMap){
		if(newMap != null && newMap.size() > 0){
			for (Map.Entry<String, Integer> entry : newMap.entrySet()) {
				
				Integer count = entry.getValue();
				if(oldMap.containsKey(entry.getKey())){
					count = count + oldMap.get(entry.getKey());
				}
				oldMap.put(entry.getKey(), count);
			}
		}
		return oldMap;
	}
}
