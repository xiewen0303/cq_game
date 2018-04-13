package com.junyou.state.jingji.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.junyou.bus.jingji.export.PaiMingZaXiangConfigExportService;
 

public class JingjiAttribute implements Serializable{
	private static final long serialVersionUID = 1L;
	private long updateTime;
	private int buff;
	private Map<String,Long> attribute;
	private List<String> skills = new ArrayList<>();
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getBuff() {
		return buff;
	}
	public void setBuff(int buff) {
		this.buff = buff;
	}
	public Map<String, Long> getAttribute() {
		return attribute;
	}
	public long getAttribute(String type) {
		Long value = attribute.get(type);
		if(value == null){
			return 0;
		}else if(buff > 0){
			value = value * (100 + buff * PaiMingZaXiangConfigExportService.getGuwuAdd()) / 100;
		}
		return value;
	}
	public long getAttributeNoBuff(String type) {
		return attribute.get(type);
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = attribute;
	}
	public List<String> getSkills() {
		return skills;
	}
	public void addSkill(String skillId) {
		this.skills.add(skillId);
	}
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	
	
}
