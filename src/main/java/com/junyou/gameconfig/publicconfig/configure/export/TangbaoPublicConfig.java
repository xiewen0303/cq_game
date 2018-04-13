package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.List;
import java.util.Map;

import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-5-5 下午3:29:31
 */
public class TangbaoPublicConfig extends AdapterPublicConfig{
	private String name;
	private int zzNeedLevel;
	private int zzMax;
	private int czNeedLevel;
	private int czMax;
	private int ftNeedZz;//附体功能所需资质丹
	private List<Integer> skillNeedLevel;
	private Map<String,Long> eatAttMax;
	private String skill;
	private String res;
	private int heartTime;
	private int needLevel;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getZzNeedLevel() {
		return zzNeedLevel;
	}
	public void setZzNeedLevel(int zzNeedLevel) {
		this.zzNeedLevel = zzNeedLevel;
	}
	public int getZzMax() {
		return zzMax;
	}
	public void setZzMax(int zzMax) {
		this.zzMax = zzMax;
	}
	public int getCzNeedLevel() {
		return czNeedLevel;
	}
	public void setCzNeedLevel(int czNeedLevel) {
		this.czNeedLevel = czNeedLevel;
	}
	public int getCzMax() {
		return czMax;
	}
	public void setCzMax(int czMax) {
		this.czMax = czMax;
	}
	public int getFtNeedZz() {
		return ftNeedZz;
	}
	public void setFtNeedZz(int ftNeedZz) {
		this.ftNeedZz = ftNeedZz;
	}
	public List<Integer> getSkillNeedLevel() {
		return skillNeedLevel;
	}
	public void setSkillNeedLevel(List<Integer> skillNeedLevel) {
		this.skillNeedLevel = new ReadOnlyList<>(skillNeedLevel);
	}
	public Map<String, Long> getEatAttMax() {
		return eatAttMax;
	}
	public void setEatAttMax(Map<String, Long> eatAttMax) {
		this.eatAttMax = new ReadOnlyMap<>(eatAttMax);
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	public int getHeartTime() {
		return heartTime;
	}
	public void setHeartTime(int heartTime) {
		this.heartTime = heartTime;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	
	
	
}
