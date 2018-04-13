package com.junyou.bus.rolebusiness.configure.export;

import com.junyou.bus.rolebusiness.entity.RoleBusinessInfo;

/**
 * 角色业务数据Wrapper
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-13 下午2:27:28 
 */
public class RoleBusinessInfoWrapper {
	private RoleBusinessInfo roleBusinessInfo;
	
	public RoleBusinessInfoWrapper(RoleBusinessInfo roleBusinessInfo){
		this.roleBusinessInfo = roleBusinessInfo;
	}
	
	public int getPkVal(){
		return roleBusinessInfo.getPkVal();
	}
	
	public long getHuiTime(){
		return roleBusinessInfo.getHuiTime().longValue();
	}
	
	public Long getCurFighter() {
		return roleBusinessInfo.getCurFighter();
	}
	
	public Long getZhenqi(){
		return roleBusinessInfo.getZhenqi();
	}
	
	public Integer getRoleyu(){
		return roleBusinessInfo.getRongyu();
	}
	public Integer getRongLianVal(){
		return roleBusinessInfo.getRongluValue();
	}
	public Integer getXuanTieValue(){
		return roleBusinessInfo.getXuanTieValue();
	}
	public Long getXiuwei(){
		return roleBusinessInfo.getXiuwei();
	}
	/**
	 * 跳闪值
	 * @return
	 */
	public Integer getJumpVal(){
		return roleBusinessInfo.getJumpVal();
	}
	/**
	 * 最后吆喝时间
	 * @return
	 */
	public Long getLastYhTime(){
		return roleBusinessInfo.getLastYhTime();
	}
}
