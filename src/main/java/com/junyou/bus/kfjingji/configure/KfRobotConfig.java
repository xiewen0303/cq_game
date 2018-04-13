package com.junyou.bus.kfjingji.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-10-29 下午2:37:39
 */
public class KfRobotConfig {
	private int id;
	private String name;
	private int configId;
	private int zuoqi;
	private int level;
	private long zplus;
	private Map<Integer,Map<String,Long>> attribute = new HashMap<>();
	private List<String> skills = new ArrayList<>();
	private Object[] info;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<Integer,Map<String,Long>> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute.put(BaseAttributeType.LEVEL.getVal(), new ReadOnlyMap<>(attribute));
	}
	public List<String> getSkills() {
		return skills;
	}
	public void addSkill(String skill){
		skills.add(skill);
	}
	public int getZuoqi() {
		return zuoqi;
	}
	public void setZuoqi(int zuoqi) {
		this.zuoqi = zuoqi;
	}
	public long getZplus() {
		return zplus;
	}
	public void setZplus(long zplus) {
		this.zplus = zplus;
	}
	public Object[] getInfo() {
		if(info == null){
			info = new Object[]{
				-id,name,level,zplus,id,configId,zuoqi
			};
		}
		return info;
	}
	
}
