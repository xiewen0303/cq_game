package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 被动技能
 * @author LiuYu
 * @date 2015-3-11 上午10:19:51
 */
public class BeiDongSkillConfig {
	
	private String id;
	
	private ReadOnlyMap<Integer,String> pros;
	
	private Map<Integer, BeiDongSkillXueXiConfig> configs = new HashMap<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ReadOnlyMap<Integer,String> getPros() {
		return pros;
	}

	public void setPros(Map<Integer,String> pros) {
		this.pros = new ReadOnlyMap<>(pros);
	}

	public BeiDongSkillXueXiConfig getConfig(Integer level) {
		return configs.get(level);
	}

	public void addConfig(BeiDongSkillXueXiConfig config) {
		this.configs.put(config.getLevel(), config);
	}
	
	
	
}
