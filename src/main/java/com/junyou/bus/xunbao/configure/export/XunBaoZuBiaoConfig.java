package com.junyou.bus.xunbao.configure.export;

import java.util.HashMap;
import java.util.Map;
 
/**
 * 寻宝组包表
 * @author DaoZheng Yuan
 * 2015年6月2日 下午8:10:39
 */
public class XunBaoZuBiaoConfig  {

	private String id;
	private int allOdds;
	
	
	private Map<Object[],Integer> datas =new HashMap<Object[], Integer>();

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public void addGoodsOdds(Object[] key, int value) {
		datas.put(key, value);
	}

	public Map<Object[], Integer> getDatas() {
		return datas;
	}

	public int getAllOdds() {
		return allOdds;
	}

	public void setAllOdds(int allOdds) {
		this.allOdds = allOdds;
	}
}
