package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 平台统计日志事件
 * @author DaoZheng Yuan
 * 2015年6月15日 下午12:21:15
 */
public class PlatformLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private String url;//打印的平台url
	private String userId;//账号id
	private Long userRoleId;//角色id
	private String roleName;//角色名称
	private String ip;//玩家最后登录的id
	
	public PlatformLogEvent(String url,String userId,Long userRoleId,String roleName,String ip){
		super(LogPrintHandle.PLATFORM_LOG);
		
		this.url = url;
		this.userId = userId;
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.ip = ip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
