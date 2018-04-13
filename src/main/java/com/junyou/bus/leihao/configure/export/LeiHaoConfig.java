package com.junyou.bus.leihao.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

public class LeiHaoConfig {
	private int id;
	private int xfvalue;
	private int count = 1;//默认一次
	private Map<String, GoodsConfigureVo> itemMap = new HashMap<String, GoodsConfigureVo>();
	private String itemStr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getXfvalue() {
		return xfvalue;
	}

	public void setXfvalue(int xfvalue) {
		this.xfvalue = xfvalue;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<String, GoodsConfigureVo> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, GoodsConfigureVo> itemMap) {
		this.itemMap = itemMap;
	}

	public String getItemStr() {
		return itemStr;
	}

	public void setItemStr(String itemStr) {
		this.itemStr = itemStr;
	}

}
