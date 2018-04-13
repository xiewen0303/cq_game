package com.junyou.bus.giftcard.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;
import com.kernel.data.dao.AbsVersion;

/**
 * 兑换礼包奖励配置
 * @author jy
 *
 */
public class LiBaoConfig extends AbsVersion implements IGoodsCheckConfig {

	private String id;
	private String name;
	private ReadOnlyMap<String,Integer> items;
	private Integer money;
	private Integer bindgold;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReadOnlyMap<String, Integer> getItems() {
		return items;
	}

	public void setItems(ReadOnlyMap<String, Integer> items) {
		this.items = items;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getBindgold() {
		return bindgold;
	}

	public void setBindgold(Integer bindgold) {
		this.bindgold = bindgold;
	}

	@Override
	public String getConfigName() {
		return "LiBaoConfig--"+getId();
	}

	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = null;
		if(items != null && items.size() > 0){
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(items);
		}
		return list;
	}
}
