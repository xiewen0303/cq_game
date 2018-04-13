package com.junyou.event;

import com.junyou.log.LogPrintHandle;
 

/**
 * 在线人数日志
 * @author LiuYu
 * @date 2015-5-4 上午11:38:49
 */
public class OnlineCountLogEvent extends AbsGameLogEvent {

	 
	private static final long serialVersionUID = 1L;
	
	private int online;//在线人数
	private int ip;//独立ip数
	private int dlq;//微端人数
	
	public OnlineCountLogEvent(int online, int ip, int dlq) {
		super(LogPrintHandle.ONLINE_COUNT);
		this.online = online;
		this.ip = ip;
		this.dlq = dlq;
	}

	public int getOnline() {
		return online;
	}

	public int getIp() {
		return ip;
	}

	public int getDlq() {
		return dlq;
	}
	
}