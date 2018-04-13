package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 玩家手动删除带附件的邮件日志事件
 * @author DaoZheng Yuan
 * 2015年6月18日 上午10:15:11
 */
public class EmailDelLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	
	//玩家角色id
	private Long userRoleId;
	//玩家名称
	private String roleName;
	//邮件附件字符串
	private String emailAttament;
	
	public EmailDelLogEvent(Long userRoleId,String roleName,String emailAttament) {
		super(LogPrintHandle.EMAIL_DEL_LOG_BIG);
		
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.emailAttament = emailAttament;
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

	public String getEmailAttament() {
		return emailAttament;
	}

	public void setEmailAttament(String emailAttament) {
		this.emailAttament = emailAttament;
	}

}
