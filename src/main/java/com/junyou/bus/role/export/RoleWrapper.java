package com.junyou.bus.role.export;

import java.io.Serializable;
import java.sql.Timestamp;

import com.junyou.bus.role.entity.UserRole;

/**
 * 玩家RoleWrapper
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-9 下午3:01:06
 */
public class RoleWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	private UserRole userRole;
	
	public RoleWrapper(UserRole userRole) {
		this.userRole = userRole;
	}
	
	public String getUserId(){
		return userRole.getUserId();
	}
	
	public String getServerId(){
		return userRole.getServerId();
	}
	
	public Integer getConfigId(){
		return userRole.getConfigId();
	}
	public void setConfigId(Integer configId){
		userRole.setConfigId(configId);
	}
	
	public Long getId(){
		return userRole.getId();
	}
	
	public String getName(){
		return userRole.getName();
	}
	
	public Integer getLevel(){
		return userRole.getLevel();
	}
	
	public Long getExp(){
		return userRole.getExp();
	}
	
	public Timestamp getCreateTime(){
		return userRole.getCreateTime();
	}
	
	public Integer getChenmiAddOnline(){
		return userRole.getChenmiAddOnline();
	}
	
	public String getLastLoginIp(){
		return userRole.getLastLoginIp();
	}
	
	public Long getOnlineTime(){
		return userRole.getOnlineTime();
	}
	public Long getOfflineTime(){
		return userRole.getOfflineTime();
	}
	
	public boolean isFirstLogin(){
		return userRole.getOfflineTime() == getCreateTime().getTime();
	}
	public Long getZhenqi(){
		return userRole.getZhenqi();
	}
	
	/**
	 * 是否是GM
	 * @return true:是GM,false:不是GM
	 */
	public boolean isGm() {
		return userRole.isGm();
	}
	/**
	 * GM类型，-1新手指导员，0玩家，1GM
	 * @return
	 */
	public Integer getIsGm(){
		return userRole.getIsGm();
	}
	
	/**
	 * 是否不需要检测聊天等级
	 * @return
	 */
	public boolean isNoCheckChatLevel(){
		return userRole.getIsGm() != 0;
	}
}