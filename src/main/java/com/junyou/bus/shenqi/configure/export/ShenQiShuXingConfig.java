package com.junyou.bus.shenqi.configure.export;

import java.util.List;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 神器属性
 * 
 * @author dsh
 * @date 2015-6-3
 */
public class ShenQiShuXingConfig extends AbsVersion implements
		Comparable<ShenQiShuXingConfig> {
	private Integer id;

	private String name;
	
	private Map<String, Long> attrs;

	private List<Integer> conditionList;

	private List<Integer> needCountList;
	private Map<String, Integer> needItem;
	
	private String skill;
	
	private String skill2;

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

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public List<Integer> getConditionList() {
		return conditionList;
	}

	public void setConditionList(List<Integer> conditionList) {
		this.conditionList = conditionList;
	}

	public List<Integer> getNeedCountList() {
		return needCountList;
	}

	public void setNeedCountList(List<Integer> needCountList) {
		this.needCountList = needCountList;
	}

	public Map<String, Integer> getNeedItem() {
		return needItem;
	}

	public void setNeedItem(Map<String, Integer> needItem) {
		this.needItem = needItem;
	}
	
	public Long getByAttrKey(String attrName){
		return attrs.get(attrName);
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getSkill2() {
		return skill2;
	}

	public void setSkill2(String skill2) {
		this.skill2 = skill2;
	}

	@Override
	public int compareTo(ShenQiShuXingConfig o) {
		if (o.getId().intValue() < id.intValue()) {
			return 1;
		} else {
			return -1;
		}
	}
}
