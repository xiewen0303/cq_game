package com.junyou.bus.branchtask.configure;


import java.util.HashMap;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * 支线任务 
 */
public class ZhiXianRenWuConfig extends AbsVersion {

	private Integer id;

	private String name;

	private String desc;
	
	private Integer order;
	
	private Integer type;
	
	private int money;
	
	private int exp;
	
	private int zhenQi;
	
	private Map<String,Integer> awards = new HashMap<String, Integer>();

	private IOpenCondition openCondition;
	
	private Object target;
	
	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Map<String, Integer> getAwards() {
		return awards;
	}

	public void setAwards(Map<String, Integer> awards) {
		this.awards = awards;
	}


	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getZhenQi() {
		return zhenQi;
	}

	public void setZhenQi(int zhenQi) {
		this.zhenQi = zhenQi;
	}

	public IOpenCondition getOpenCondition() {
		return openCondition;
	}

	public void setOpenCondition(IOpenCondition openCondition) {
		this.openCondition = openCondition;
	}
}
