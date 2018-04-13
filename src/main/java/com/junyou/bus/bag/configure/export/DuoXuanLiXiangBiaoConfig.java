package com.junyou.bus.bag.configure.export;

import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;

/**
 * 多选宝箱配置表 
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-4-23 下午6:40:02
 */
public class DuoXuanLiXiangBiaoConfig implements IGoodsCheckConfig{

	private Integer paixu1;
	private Integer paixu2;
	private Integer paixu3;
	private Integer paixu4;
	
	private Map<Integer, Map<String, Integer>> configMap;
	
	private Map<Integer, Integer> moneytypeMap;
	
	private Map<Integer, Integer> goldMap;

	private Integer id;


	public DuoXuanLiXiangBiaoConfig copy(){
		return null;
	}
	
	public Integer getPaixu1() {
		return paixu1;
	}

	public void setPaixu1(Integer paixu1) {
		this.paixu1 = paixu1;
	}

	public Integer getPaixu2() {
		return paixu2;
	}

	public void setPaixu2(Integer paixu2) {
		this.paixu2 = paixu2;
	}

	public Integer getPaixu3() {
		return paixu3;
	}

	public void setPaixu3(Integer paixu3) {
		this.paixu3 = paixu3;
	}

	public Integer getPaixu4() {
		return paixu4;
	}

	public void setPaixu4(Integer paixu4) {
		this.paixu4 = paixu4;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Map<Integer, Map<String, Integer>> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, Map<String, Integer>> configMap) {
		this.configMap = configMap;
	}

	public Map<Integer, Integer> getMoneytypeMap() {
		return moneytypeMap;
	}

	public void setMoneytypeMap(Map<Integer, Integer> moneytypeMap) {
		this.moneytypeMap = moneytypeMap;
	}

	public Map<Integer, Integer> getGoldMap() {
		return goldMap;
	}

	public void setGoldMap(Map<Integer, Integer> goldMap) {
		this.goldMap = goldMap;
	}

	@Override
	public String getConfigName() {
		return "DuoXuanLiXiangBiaoConfig--"+id;
	}

	@Override
	public List<Map<String, Integer>> getCheckMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
