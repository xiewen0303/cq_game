package com.junyou.bus.rfbflower.configue.export;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 鲜花魅力榜奖励
 * @author lxn
 * 
 */
public class FlowerCharmRankConfig {
	private int configId;
	private int minRank ; //最低排名
	private int maxRank ; //最高排名
	private Map<String, Integer> rewards;
	
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public int getMinRank() {
		return minRank;
	}
	public void setMinRank(int minRank) {
		this.minRank = minRank;
	}
	public int getMaxRank() {
		return maxRank;
	}
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}
	public Map<String, Integer> getRewards() {
		return rewards;
	}
	public void setRewards(Map<String, Integer> rewards) {
		this.rewards = rewards;
	}
	/**
	 * 转成客户端要的数据
	 * @return
	 */
	public Object[] toArray() {
		Object[] obj = new Object[4];
		obj[0] = minRank;
		obj[1] = maxRank;
		if (rewards != null && !rewards.isEmpty()) {
			Object[] item = new Object[rewards.size()];
			int loop = 0;
			 for (Entry<String, Integer> entry : rewards.entrySet()) {
				item[loop] = new Object[]{entry.getKey(),entry.getValue()};
				loop++;
			}
			 obj[2] =  item;
		}
		return obj;
	}
}
