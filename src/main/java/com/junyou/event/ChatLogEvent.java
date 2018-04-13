package com.junyou.event;

import com.junyou.log.LogPrintHandle;
 

/** 
 * 聊天日志
 * @author wind
 *   
 */
public class ChatLogEvent extends AbsGameLogEvent {

	 
	private static final long serialVersionUID = 1L;
	
	private String roleName;
	private String userId; //账号
	private Long roleId; //角色Id
	private String serverId;
	private String logChatMsg;
	private int childType;
	
	public ChatLogEvent( String roleName, String userId, Long roleId,
			String serverId, String logChatMsg, int childType) {
		super(LogPrintHandle.CHAT_LOG);
		this.roleName = roleName;
		this.userId = userId;
		this.roleId = roleId;
		this.serverId = serverId;
		this.logChatMsg = logChatMsg;
		this.childType = childType;
	}
	public String getRoleName() {
		return roleName;
	}
	public String getUserId() {
		return userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public String getServerId() {
		return serverId;
	}
	public String getLogChatMsg() {
		return logChatMsg;
	}
	public int getChildType() {
		return childType;
	} 
}