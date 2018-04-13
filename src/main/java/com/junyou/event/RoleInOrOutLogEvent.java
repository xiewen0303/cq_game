package com.junyou.event;

import com.junyou.log.LogPrintHandle;


/**
 * 角色上下线日志事件
 * @author DaoZheng Yuan
 * 2014年11月28日 下午6:30:03
 */
public class RoleInOrOutLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	
	private String userRoleId;
	private String roleName;
	private String ip;
	private int sort;
	private String roleId;
	private String via;
	private String pf;
	
	/**
	 * 角色上下线登陆日志
	 * @param type 日志大类类型
	 * @param sort 日志小类类型
	 * @param roleId 账号Id
	 * @param userRoleId 角色Id（账号上线没有角色Id）
	 * @param roleName 角色名称（账号上线没有角色名称）
	 * @param ip
	 */
	public RoleInOrOutLogEvent(int type, int sort, String roleId, String userRoleId,String roleName,String ip,String via,String pf){
		super(type);
		
		if(roleName == null) roleName = LogPrintHandle.LINE_CHAR;
		if(userRoleId == null) userRoleId = LogPrintHandle.LINE_CHAR;
		
		this.roleId = roleId;
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.ip = ip;
		this.sort = sort;
		this.pf = pf;
		this.via = via;
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

	public String getVia() {
		return via;
	}

	public String getPf() {
		return pf;
	}
	
}