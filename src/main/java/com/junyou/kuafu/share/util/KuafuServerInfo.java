package com.junyou.kuafu.share.util;

public class KuafuServerInfo {
	

	public KuafuServerInfo() {
		super();
	}

	public static final String IN_IP_FIELD_KEY = "1";
	public static final String OUT_IP_FIELD_KEY = "2";
	public static final String NET_SEGMENT_FIELD_KEY = "3";
	private String ip;

	public static final String PORT_FIELD_KEY = "4";
	private int port;

	public static final String SERVER_FIELD_KEY = "5";
	private String serverId;
	
	private boolean matchServer=false;
	private boolean competitionMatchServer=false;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public boolean isMatchServer() {
		return matchServer;
	}

	public void setMatchServer(boolean matchServer) {
		this.matchServer = matchServer;
	}

	public boolean isCompetitionMatchServer() {
		return competitionMatchServer;
	}

	public void setCompetitionMatchServer(boolean competitionMatchServer) {
		this.competitionMatchServer = competitionMatchServer;
	}

}
