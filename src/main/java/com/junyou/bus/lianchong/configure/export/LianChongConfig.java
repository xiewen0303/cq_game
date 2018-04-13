package com.junyou.bus.lianchong.configure.export;

import java.util.List;
import java.util.Map;

import org.springframework.cglib.core.CollectionUtils;

import com.junyou.utils.json.JsonUtils;

/**
 * 天数类型对应的奖励
 * 
 * @author lxn
 * 
 */
public class LianChongConfig {

	private Integer id; // excel行id
	private Integer type; // 类型
	private Integer gold;// 需要元宝|充值的档位
	private List<RewardVo> rewardVos;

	/**
	 * 转成客户端要的数据
	 * @return
	 */
	public Object[] toArray() {
		Object[] obj = new Object[4];
		obj[0] = id;
		obj[1] = type;
		obj[2] = gold;
		if (rewardVos != null && rewardVos.size() > 0) {
			Object[] objects = new Object[rewardVos.size()];
			for (int i = 0; i < rewardVos.size(); i++) {
				RewardVo vo = rewardVos.get(i);
				objects[i] = vo.toArray();
			}
			obj[3] = objects;
		}
		return obj;
	}

	// 内部类获取
	public RewardVo getInnerRewardVo() {
		return new RewardVo();
	}

	public List<RewardVo> getRewardVos() {
		return rewardVos;
	}

	public void setRewardVos(List<RewardVo> rewardVos) {
		this.rewardVos = rewardVos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getGold() {
		return gold;
	}

	// 奖励
	public class RewardVo {
		private Integer dayType; // 天数类型
		private Map<String, Integer> itemMap;

		public Object[] toArray() {
			if (itemMap != null && itemMap.size() > 0) {
				Object[] objects2 = new Object[itemMap.size()];
				int loop = 0;
				for (Map.Entry<String, Integer> entry : itemMap.entrySet()) {
					Object[] objects = new Object[] { entry.getKey(), entry.getValue() };
					objects2[loop] = objects;
					loop++;
				}
				return new Object[] { dayType, objects2 };
			}
			return null;
		}

		public Map<String, Integer> getItemMap() {
			return itemMap;
		}

		public void setItemMap(Map<String, Integer> itemMap) {
			this.itemMap = itemMap;
		}

		public Integer getDayType() {
			return dayType;
		}

		public void setDayType(Integer dayType) {
			this.dayType = dayType;
		}
	}
}
