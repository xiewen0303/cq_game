package com.junyou.bus.qisha.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-8-20 下午4:57:05
 */
public class QiShaBossConfig implements IGoodsCheckConfig{
	private int shunxu;
	private String monsterId;
	private Map<String,Integer> killItems;
	private String killItemStr;
	private Map<String,Integer> hurtItems;
	private String hurtItemStr;
	public int getShunxu() {
		return shunxu;
	}
	public void setShunxu(int shunxu) {
		this.shunxu = shunxu;
	}
	public String getMonsterId() {
		return monsterId;
	}
	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}
	public Map<String, Integer> getKillItems() {
		return killItems;
	}
	public void setKillItems(Map<String, Integer> killItems) {
		this.killItems = new ReadOnlyMap<>(killItems);
	}
	public Map<String, Integer> getHurtItems() {
		return hurtItems;
	}
	public void setHurtItems(Map<String, Integer> hurtItems) {
		this.hurtItems = new ReadOnlyMap<>(hurtItems);
	}
	@Override
	public String getConfigName() {
		return "QiShaBossConfig--"+shunxu;
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = new ArrayList<>();
		list.add(killItems);
		list.add(hurtItems);
		return list;
	}
	public String getKillItemStr() {
		return killItemStr;
	}
	public void setKillItemStr(String killItemStr) {
		this.killItemStr = killItemStr;
	}
	public String getHurtItemStr() {
		return hurtItemStr;
	}
	public void setHurtItemStr(String hurtItemStr) {
		this.hurtItemStr = hurtItemStr;
	}
	
	
}
