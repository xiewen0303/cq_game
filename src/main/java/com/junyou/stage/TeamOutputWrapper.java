package com.junyou.stage;

import com.junyou.stage.entity.Team;
import com.junyou.stage.entity.TeamMember;

public class TeamOutputWrapper {
	public static Object[] getMemberVo(TeamMember member){
		/**
		 *	0	int	角色配置ID
			1	Number	角色guid
			2	String	角色name
			3	int	角色level
			4	int	所在队伍ID
			5	String	所属帮派
			6	String	所在地图ID
			7	int	所在地图线路ID
			8	Boolean(true:队长)	是否队长
			9	int	御剑ID
		 */
		return new Object[]{
			member.getRole().getBusinessData().getRoleConfigId()
			,member.getRole().getId()
			,member.getRole().getName()
			,member.getRole().getLevel()
			,member.getTeamId()
			,member.getRole().getBusinessData().getGuildName()
			,member.getMapId()
			,member.getLine()
			,member.isLeader()
			,member.getRole().getZuoQiLevel()
		};
	}
	
	public static Object[] getTeamVo(Team team){
		/**
		 *	0	int	队伍id
			1	Number	队长guid
			2	String	队长名
			3	int	队长等级
			4	(Array[TeamMemberVO])	队员信息
		 */
		return new Object[]{
			team.getTeamId()
			,team.getLeader().getRole().getId()
			,team.getLeader().getRole().getName()
			,team.getLeader().getRole().getLevel()
			,null
		};
	}
}
