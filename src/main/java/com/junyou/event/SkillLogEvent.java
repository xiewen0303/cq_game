package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 技能日志
 * @author LiuYu
 * @date 2015-3-30 上午10:33:38
 */
public class SkillLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	
	public SkillLogEvent(long userRoleId, String name,String skillId, int money, int zhenqi,int skillType) {
		super(LogPrintHandle.SKILL);
		this.userRoleId = userRoleId;
		this.name = name;
		this.skillId = skillId;
		this.money = money;
		this.zhenqi = zhenqi;
		this.skillType = skillType;
	}

	private long userRoleId;
	private String name;
	private String skillId;
	private int money;//消耗银两
	private int zhenqi;//消耗真气
	private int skillType;//技能类型

	public long getUserRoleId() {
		return userRoleId;
	}
	public String getName() {
		return name;
	}
	public String getSkillId() {
		return skillId;
	}
	public int getMoney() {
		return money;
	}
	public int getZhenqi() {
		return zhenqi;
	}
	public int getSkillType() {
		return skillType;
	}
	
	
}