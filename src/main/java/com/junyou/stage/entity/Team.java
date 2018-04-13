package com.junyou.stage.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.stage.TeamOutputWrapper;

public class Team {
	private int teamId;
	private static int maxCount;
	private TeamMember leader; 
	private Map<Long,TeamMember> members = new HashMap<Long, TeamMember>();
	private Object[] teamVo;
	private Object[] memberVos;
	private List<Long> roleIdList = new ArrayList<>();
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public TeamMember getLeader() {
		return leader;
	}
	public Team(TeamMember leader) {
		this.leader = leader;
		members.put(leader.getRole().getId(), leader);
		roleIdList.add(leader.getRole().getId());
	}
	public List<TeamMember> getMembers() {
		return new ArrayList<>(members.values());
	}
	public TeamMember getMember(Long userRoleId) {
		return members.get(userRoleId);
	}
	public boolean changeLeader(Long userRoleId){
		TeamMember member = members.get(userRoleId);
		if(member == null){
			return false;
		}
		leader.setLeader(false);
		setLeader(member);
		return true;
	}
	/**
	 * 新增成员
	 * @param member
	 * @return
	 */
	public synchronized boolean addMembers(TeamMember member) {
		if(members.size() >= maxCount){
			return false;
		}
		member.setLeader(false);
		member.setTeam(this);
		members.put(member.getRole().getId(), member);
		roleIdList.add(member.getRole().getId());
		memberVos = null;
		return true;
	}
	/**
	 *  设置队长
	 * @param leader
	 */
	private void setLeader(TeamMember leader){
		this.leader = leader;
		this.leader.setLeader(true);
		if(teamVo != null){
			teamVo[1] = leader.getRole().getId();
			teamVo[2] = leader.getRole().getName();
			teamVo[3] = leader.getRole().getLevel();
		}
	}
	/**
	 * 移除成员
	 * @param userRoleId
	 */
	public TeamMember removeMember(Long userRoleId){
		TeamMember member = members.remove(userRoleId);
		roleIdList.remove(userRoleId);
		if(member == null){
			return null;
		}
		member.setTeam(null);
		if(member.isLeader()){
			//队长退队
			member.setLeader(false);
			if(members.size() > 0){
				//如果队伍没解散，变更新队长
				setLeader(members.values().iterator().next());
			}
		}
		memberVos = null;
		return member;
	}
	/**
	 * 队伍VO
	 * @return
	 */
	public Object[] getTeamVo() {
		if(teamVo == null){
			teamVo = TeamOutputWrapper.getTeamVo(this);
		}
		teamVo[4] = getMemberVos();
		return teamVo;
	}
	/**
	 * 成员vo列表
	 * @return
	 */
	public Object[] getMemberVos() {
		if(memberVos == null){
			memberVos = new Object[members.size()];
			int i = 0;
			for (TeamMember member : members.values()) {
				memberVos[i++] = member.getMemberVo();
			}
		}
		return memberVos;
	}
	/**
	 * 是否是队伍中成员
	 * @param userRoleId
	 * @return
	 */
	public boolean isTeamMember(Long userRoleId){
		return members.containsKey(userRoleId);
	}
	/**
	 * 设置队伍最大成员数量
	 * @param maxCount
	 */
	public static void setMaxCount(int maxCount) {
		Team.maxCount = maxCount;
	}
	/**
	 * 是否已满
	 * @return
	 */
	public boolean isFull(){
		return members.size() >= maxCount;
	}
	/**
	 * 获取成员id数组
	 * @return
	 */
	public Long[] getRoleIdList() {
		return roleIdList.toArray(new Long[roleIdList.size()]);
	}
	
}
