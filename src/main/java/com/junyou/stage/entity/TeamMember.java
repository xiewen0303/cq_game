package com.junyou.stage.entity;

import com.junyou.stage.TeamOutputWrapper;
import com.junyou.stage.model.element.role.IRole;

public class TeamMember {
	private IRole role;
	private Team team;
	private boolean leader;
	private Object[] memberVo;
	private int line;
	private int mapId;
	private String stageId;
	private int sameStageMember;
	private boolean autoAgreeYaoqing = true;
	private boolean autoAgreeApply = true;
	
	public void setLine(int line) {
		this.line = line;
		if(memberVo != null){
			memberVo[7] = line;
		}
	}
	public int getLine() {
		return line;
	}
	public IRole getRole() {
		return role;
	}
	/**
	 * 请勿使用此方法
	 * @param role
	 */
	public void setRole(IRole role) {
		this.role = role;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
		if(memberVo != null){
			memberVo[4] = getTeamId();
		}
	}
	public boolean isLeader() {
		return leader;
	}
	public void setLeader(boolean leader) {
		this.leader = leader;
		if(memberVo != null){
			memberVo[8] = leader;
		}
	}
	public Object[] getMemberVo() {
		if(memberVo == null){
			memberVo = TeamOutputWrapper.getMemberVo(this);
		}else{
			memberVo[3] = role.getLevel();
			memberVo[5] = role.getBusinessData().getGuildName();
			memberVo[9] = role.getZuoQiLevel();
		}
		return memberVo;
	}
	/**
	 * 获取同场景队友数量
	 * @return
	 */
	public int getSameStageMember() {
		return sameStageMember;
	}
	public void setSameStageMember(int sameStageMember) {
		this.sameStageMember = sameStageMember;
	}
	
	public Integer getTeamId(){
		if(team != null){
			return team.getTeamId();
		}
		return null;
	}
	public boolean isAutoAgreeYaoqing() {
		return autoAgreeYaoqing;
	}
	public void setAutoAgreeYaoqing(boolean autoAgreeYaoqing) {
		this.autoAgreeYaoqing = autoAgreeYaoqing;
	}
	public boolean isAutoAgreeApply() {
		return autoAgreeApply;
	}
	public void setAutoAgreeApply(boolean autoAgreeApply) {
		this.autoAgreeApply = autoAgreeApply;
	}
	public String getStageId() {
		return stageId;
	}
	public void setStageId(String stageId) {
		this.stageId = stageId;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
		if(memberVo != null){
			memberVo[6] = mapId;
		}
	}
	
}
