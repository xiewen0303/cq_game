package com.junyou.bus.xianjian.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.element.role.business.Equip;

/**
 * 仙剑
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-3-31 下午4:38:24
 */
public class XianJian implements Serializable{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 140022571580467149L;

	private Map<String,ISkill> skills = new HashMap<String, ISkill>(); //所学技能
	
	private int qndCount; //潜能丹数量
	
	private int czdCount; //成长丹数量
	
	private List<Equip> equips =new ArrayList<>();//装备
	
	private int xianjianLevel; //仙剑阶段等级
	
	private int showId; //展示的外显Id
	
	private long zplus;//仙剑战斗力
	
	private boolean isGetOn;//是否使用
	private List<Integer> huanhuaList = new ArrayList<Integer>();
	
	/**
	 * 添加技能
	 */
	public void addSkills(ISkill skill){
		skills.put(skill.getId(), skill);
	}
	
	public Map<String, ISkill> getSkills() {
		return skills;
	}

	public void setSkills(Map<String, ISkill> skills) {
		this.skills = skills;
	}


	public int getQndCount() {
		return qndCount;
	}

	public void setQndCount(int qndCount) {
		this.qndCount = qndCount;
	}

	public int getCzdCount() {
		return czdCount;
	}

	public void setCzdCount(int czdCount) {
		this.czdCount = czdCount;
	}

	public List<Equip> getEquips() {
		return equips;
	}

	public void setEquips(List<Equip> equips) {
		this.equips = equips;
	}

	public int getXianjianLevel() {
		return xianjianLevel;
	}

	public void setXianjianLevel(int xianjianLevel) {
		this.xianjianLevel = xianjianLevel;
	}

	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	public boolean isGetOn() {
		return isGetOn;
	}

	public void setGetOn(boolean isGetOn) {
		this.isGetOn = isGetOn;
	}

	public long getZplus() {
		return zplus;
	}

	public void setZplus(long zplus) {
		this.zplus = zplus;
	}
	public List<Integer> getHuanhuaList() {
		return huanhuaList;
	}

	public void setHuanhuaList(List<Integer> huanhuaList) {
		this.huanhuaList = huanhuaList;
	}
	public void addHuanhua(Integer configId) {
		if(!huanhuaList.contains(configId)){
			huanhuaList.add(configId);
		}
	}
}