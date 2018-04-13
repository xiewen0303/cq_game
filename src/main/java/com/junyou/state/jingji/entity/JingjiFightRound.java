package com.junyou.state.jingji.entity;

import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;

/**
 * 竞技场战斗回合
 * @author LiuYu
 * @date 2015-3-20 上午11:37:44
 */
public class JingjiFightRound {
	private long time;
	private Long userRoleId;
	private String skillId;
	private Integer harmType;
	private long harm;
	public JingjiFightRound(IHarm harm,IJingjiFighter fighter,Long time,ISkill skill){
		this.time = time;
		userRoleId = fighter.getId();
		skillId = skill.getCategory();
		harmType = harm.getHarmTypeValue();
		this.harm = harm.getVal();
	}
	public long getTime() {
		return time;
	}
	public Long getUserRoleId() {
		return userRoleId;
	}
	public String getSkillId() {
		return skillId;
	}
	public Integer getHarmType() {
		return harmType;
	}
	public long getHarm() {
		return harm;
	}
	
}
