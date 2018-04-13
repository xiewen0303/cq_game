package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 登陆gate日志
 * @author LiuYu
 * @date 2015-4-23 下午8:32:31
 */
public class LoginGateLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  
	
	private String userId;
	private String ip;
	private String serverId;
	private String coopName;
	private String refererUrl;

	public LoginGateLogEvent(String userId, String ip, String serverId,String coopName, String refererUrl) {
		super(LogPrintHandle.LOGIN_GATE_INFO);
		this.userId = userId;
		this.ip = ip;
		this.serverId = serverId;
		this.coopName = coopName;
		this.refererUrl = refererUrl;
	}


	public String getUserId() {
		return userId;
	}

	public String getIp() {
		return ip;
	}

	public String getServerId() {
		return serverId;
	}

	public String getCoopName() {
		return coopName;
	}

	public String getRefererUrl() {
		return refererUrl;
	}

	
}