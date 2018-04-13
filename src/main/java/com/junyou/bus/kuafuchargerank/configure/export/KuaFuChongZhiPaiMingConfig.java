package com.junyou.bus.kuafuchargerank.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

public class KuaFuChongZhiPaiMingConfig {
	private int id;
	private int min;
	private int max;
	private List<GoodsConfigureVo> item = new ArrayList<GoodsConfigureVo>();

	private Map<String, GoodsConfigureVo> itemMap = new HashMap<String, GoodsConfigureVo>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public List<GoodsConfigureVo> getItem() {
		return item;
	}

	public void setItem(List<GoodsConfigureVo> item) {
		this.item = item;
	}

	public void addItem(GoodsConfigureVo vo) {
		item.add(vo);
	}

	public Map<String, GoodsConfigureVo> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, GoodsConfigureVo> itemMap) {
		this.itemMap = itemMap;
	}

}
