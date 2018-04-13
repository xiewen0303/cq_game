/**
 * 
 */
package com.junyou.bus.stagecontroll;

import com.junyou.bus.stagecontroll.position.AbsRolePosition;
import com.junyou.bus.stagecontroll.position.RoleNormalPosition;


/**
 * @author DaoZheng Yuan
 * 2014年11月26日 下午3:27:01
 */
public class RoleState {
	
	private Long roleId;
	/**
	 * 当前场景信息
	 */
	private AbsRolePosition curPosition;
	/**
	 * 历史场景信息
	 */
	private AbsRolePosition readyForPosition;
	/**
	 * 可保存的场景信息
	 */
	private RoleNormalPosition roleNormalPosition;
	
	/**
	 * 是否正在切换地图中
	 */
	private boolean isChanging;
	
	/**
	 * 是否是在副本中
	 */
	private boolean isInFuben;
	
	public RoleState(Long roleId) {
		this.roleId = roleId;
	}

	public AbsRolePosition getCurPosition() {
		return curPosition;
	}

	public void setCurPosition(AbsRolePosition curPosition) {
		this.curPosition = curPosition;
	}

	public AbsRolePosition getReadyToPosition() {
		return readyForPosition;
	}

	public void setReadyForPosition(AbsRolePosition readyForPosition) {
		this.readyForPosition = readyForPosition;
	}

	public void setOfflineSavePosition(RoleNormalPosition roleNormalPosition) {
		
		this.roleNormalPosition = roleNormalPosition;
		
	}
	
	public RoleNormalPosition getOfflineSavePosition(){
		return roleNormalPosition;
	}

	public Long getRoleId() {
		return roleId;
	}

	public boolean isChanging() {
		return isChanging;
	}

	public void setChanging(boolean isChanging) {
		this.isChanging = isChanging;
	}

	public boolean isInFuben() {
		return isInFuben || curPosition.isCopyMap();
	}

	public void setInFuben(boolean isInFuben) {
		this.isInFuben = isInFuben;
	}
	
	
}
