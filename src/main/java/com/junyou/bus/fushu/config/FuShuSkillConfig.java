package com.junyou.bus.fushu.config;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 附属功能技能表
 * @author LiuYu
 * 2015-8-25 上午10:12:39
 */
public class FuShuSkillConfig {
	private String id;
	private Map<String,Long> atts;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map<String, Long> getAtts() {
		return atts;
	}
	public void setAtts(Map<String, Long> atts) {
		this.atts = new ReadOnlyMap<>(atts);
	}
	
}
