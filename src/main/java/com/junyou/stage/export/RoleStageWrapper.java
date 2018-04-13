package com.junyou.stage.export;

import com.junyou.bus.rolestage.entity.RoleStage;

public class RoleStageWrapper {
	
	private RoleStage roleStage;
	
	public RoleStageWrapper() {
		// TODO Auto-generated constructor stub
	}

	public RoleStageWrapper(RoleStage roleStage) {
		this.roleStage = roleStage;
	}
	
	public RoleStage getRoleStage() {
		return roleStage;
	}

	public void setRoleStage(RoleStage roleStage) {
		this.roleStage = roleStage;
	}

	public Long getUserRoleId() {
		return roleStage.getUserRoleId();
	}
	
	public String getMapNode(){
		return roleStage.getMapNode();
	}
	
	public Integer getMapId() {
		return roleStage.getMapId();
	}

	public Integer getMapX() {
		return roleStage.getMapX();
	}

	public Integer getMapY() {
		return roleStage.getMapY();
	}

	public Long getHp() {
		return roleStage.getHp();
	}
	public String getBuff() {
		return roleStage.getBuff();
	}
	public Integer getShenqi() {
		return roleStage.getShenqi();
	}

	public int getTiLi() {
		return roleStage.getTiLi();
	}
	public int getNuqi(){
		return roleStage.getNuqi();
	}
}
