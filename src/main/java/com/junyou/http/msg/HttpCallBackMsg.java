package com.junyou.http.msg;

/**
 * http:回调消息基类
 * @author DaoZheng Yuan
 * 2014年11月25日 下午3:09:30
 */
public class HttpCallBackMsg {
	private String serverId;
	private String serverName;
	private String ptServerId;
	private String ptName;//平台名称
	
	//状态值 默认是1:表示成功,其它表示不成功并带有其含意
	private int code = 1;
	
	
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getPtServerId() {
		return ptServerId;
	}
	public void setPtServerId(String ptServerId) {
		this.ptServerId = ptServerId;
	}
	public String getPtName() {
		return ptName;
	}
	public void setPtName(String ptName) {
		this.ptName = ptName;
	}
}
