package com.junyou.state.jingji.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.bus.jingji.export.PaiMingZaXiangConfigExportService;

public class JingjiAttribute2 implements Serializable{
	private static final long serialVersionUID = 2L;
	private long updateTime;
	private int buff;
	private Map<Integer,Map<String,Long>> attribute;
	private List<String> skills = new ArrayList<>();
	private long zplus;
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
	public Map<Integer,Map<String,Long>> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<Integer,Map<String,Long>> attribute) {
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
	public long getZplus() {
		return zplus;
	}
	public long getBuffZplus(){
		if(buff > 0){
			return zplus * (100 + buff * PaiMingZaXiangConfigExportService.getGuwuAdd()) / 100;
		}
		return zplus;
	}
	public void setZplus(long zplus) {
		this.zplus = zplus;
	}
	
	
	
}
