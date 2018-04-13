package com.junyou.bus.onlinerewards.configue.export;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 在线奖励
 * @author lxn
 * 
 */
public class OnlineRewardsConfig {
	private Integer id; // excel行id
	private Integer time;
	private Map<String, Integer> rewards;
	private String desc;
	private String pic; //id==-1
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Map<String, Integer> getRewards() {
		return rewards;
	}
	public void setRewards(Map<String, Integer> rewards) {
		this.rewards = rewards;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * 转成客户端要的数据
	 * @return
	 */
	public Object[] toArray() {
		Object[] obj = new Object[4];
		obj[0] = id;
		obj[1] = time;
		if (rewards != null && !rewards.isEmpty()) {
			Object[] item = new Object[rewards.size()];
			int loop = 0;
			 for (Entry<String, Integer> entry : rewards.entrySet()) {
				item[loop] = new Object[]{entry.getKey(),entry.getValue()};
				loop++;
			}
			 obj[2] =  item;
		}
		 obj[3] = desc;
		return obj;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}
