package com.junyou.bus.skill.utils;

import com.junyou.bus.skill.entity.RoleSkill;

public class RoleSkillWarpper {
	/**
	 * 获取技能前端格式
	 * @param skill
	 * @return
	 */
	public static Object[] getSkillInfo(RoleSkill skill){
		/**
		 *	0	int	技能id
			1	int	技能状态 0：未学习 1：已学习
			2	int	当前技能等级
			3	int	当前熟练度
		 */
		return new Object[]{
				skill.getSkillId()
				,1
				,skill.getLevel()
				,skill.getShulian()
		};
	}
}
