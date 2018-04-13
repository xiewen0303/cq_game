package com.junyou.http.codec;

/**
 * 封装客户端请求报文
 * @author DaoZheng Yuan
 * 2014年11月18日 下午3:04:52
 */
public class TokenMsg {
	
	private String busiCode;    //业务码
	private String reqType;		//请求类型 GET/PST	
	private String busiType;    //业务类型:TCP or HTTP
	private String busiMessage; //业务报文:TCP请求时为TCP完整报文,HTTP_POST请求时为报文体部分,HTTP_GET时为报文头第一行参数部分
	private String busiCharset; //报文字符集
	private String fullMessage; //原始完整报文(用于在日志中打印最初接收到的原始完整报文)
	
	private String remoteIp;//请求发送者的IP
	
	public String getBusiCode() {
		return busiCode;
	}
	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}
	
	
	/**
	 * 请求发送者的IP
	 * @return
	 */
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	
	/**
	 * 请求类型 GET/PST	
	 * @return
	 */
	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getBusiType() {
		return busiType;
	}
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}
	public String getBusiMessage() {
		return busiMessage;
	}
	public void setBusiMessage(String busiMessage) {
		this.busiMessage = busiMessage;
	}
	public String getBusiCharset() {
		return busiCharset;
	}
	public void setBusiCharset(String busiCharset) {
		this.busiCharset = busiCharset;
	}
	public String getFullMessage() {
		return fullMessage;
	}
	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}
	
	
}
