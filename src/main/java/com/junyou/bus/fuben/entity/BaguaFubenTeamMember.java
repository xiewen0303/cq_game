package com.junyou.bus.fuben.entity;


public class BaguaFubenTeamMember {
	private Long roleId;
	private String memberName;
	private BaguaFubenTeam team;
	private boolean isPrepare;
	private Long strength;
	private int configId;
	private int level;
	private String serverId;
	private Object[] memberVo;
	private Object roleData;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public BaguaFubenTeam getTeam() {
		return team;
	}
	public void setTeam(BaguaFubenTeam team) {
		this.team = team;
	}
	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public boolean isPrepare() {
		return isPrepare;
	}
	public void setPrepare(boolean isPrepare) {
		this.isPrepare = isPrepare;
	}
	
	public Long getStrength() {
		return strength;
	}
	public void setStrength(Long strength) {
		this.strength = strength;
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
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Object[] getMemberVo(){
		if(memberVo == null){
			memberVo = new Object[]{roleId,0,memberName,configId,level,isPrepare,strength};
		}else{
			memberVo[5] = isPrepare;
			memberVo[6] = strength;
		}
		return memberVo;
	}
	public Object getRoleData() {
		return roleData;
	}
	public void setRoleData(Object roleData) {
		this.roleData = roleData;
	}
	
	
}
