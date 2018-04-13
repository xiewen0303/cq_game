package com.junyou.bus.jingji.entity;

import java.util.Map;

public class JingjiRobotConfig {
	
	private int rankId;
	private int roleConfigId;
	private int level;
	private int swingLevel;
	private int wuqiLevel;
	private String[] skillDatas;
	private String robotName;
	private Map<String,Long> robotAttrs;

	public int getRankId() {
		return rankId;
	}
	public void setRankId(int rankId) {
		this.rankId = rankId;
	}
	public int getRoleConfigId() {
		return roleConfigId;
	}
	public void setRoleConfigId(int roleConfigId) {
		this.roleConfigId = roleConfigId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSwingLevel() {
		return swingLevel;
	}
	public void setSwingLevel(int swingLevel) {
		this.swingLevel = swingLevel;
	}

	public int getWuqiLevel() {
		return wuqiLevel;
	}

	public void setWuqiLevel(int wuqiLevel) {
		this.wuqiLevel = wuqiLevel;
	}

	public String[] getSkillDatas() {
		return skillDatas;
	}
	public void setSkillDatas(String[] skillDatas) {
		this.skillDatas = skillDatas;
	}
	public String getRobotName() {
		return robotName;
	}
	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public Map<String,Long> getRobotAttrs() {
		return robotAttrs;
	}

	public void setRobotAttrs(Map<String, Long> robotAttrs) {
		this.robotAttrs = robotAttrs;
	}
}
