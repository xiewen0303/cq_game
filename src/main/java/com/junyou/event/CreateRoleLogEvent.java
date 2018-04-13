package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 创建角色日志事件
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-24 下午5:32:07
 */
public class CreateRoleLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	
	private String roleId;
	private String userRoleId;
	private String roleName;
	private String ip;
	private int sort;
	private String configId;
	private String via;
	private String pf;
	
	/**
	 * 创建角色日志
	 * @param sort 日志小类类型
	 * @param roleId 账号Id
	 * @param userRoleId 角色Id（创建账号没有角色Id）
	 * @param roleName 角色名称（创建账号没有角色名称）
	 * @param configId 职业性别信息（创建账号没有角色名称）
	 * @param ip
	 */
	public CreateRoleLogEvent(int sort, String roleId, String userRoleId,String roleName,String ip, String configId,String via,String pf){
		super(LogPrintHandle.CREATE_ROLE);
		
		if(roleName == null) roleName = LogPrintHandle.LINE_CHAR;
		if(userRoleId == null) userRoleId = LogPrintHandle.LINE_CHAR;
		if(configId == null) configId = LogPrintHandle.LINE_CHAR;
		
		
		this.roleId = roleId;
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.ip = ip;
		this.sort = sort;
		this.configId = configId;
		this.via = via;
		this.pf = pf;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getIp() {
		return ip;
	}

	public int getSort() {
		return sort;
	}

	public String getUserRoleId() {
		return userRoleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getConfigId() {
		return configId;
	}

	public String getVia() {
		return via;
	}

	public String getPf() {
		return pf;
	}
	
}