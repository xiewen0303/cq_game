package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 门派被动技能
 * @author LiuYu
 * @date 2015-7-20 下午10:17:34
 */
public class GuildBeiDongSkillConfig {
	
	private String id;
	
	private ReadOnlyMap<Integer,String> pros;
	
	private Map<Integer, GuildBeiDongSkillXueXiConfig> configs = new HashMap<>();

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

	public GuildBeiDongSkillXueXiConfig getConfig(Integer level) {
		return configs.get(level);
	}

	public void addConfig(GuildBeiDongSkillXueXiConfig config) {
		this.configs.put(config.getLevel(), config);
	}
	
	
	
}
