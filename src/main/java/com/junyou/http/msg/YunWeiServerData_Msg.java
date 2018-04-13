package com.junyou.http.msg;


/**
 * 给运维的数据-请求服务器数据回调信息
 * @author DaoZheng Yuan
 * 2014年12月22日 上午11:05:31
 */
public class YunWeiServerData_Msg extends HttpCallBackMsg {
	
	//在线人数
	private int onlineCount;
	//查询服务器版本号
	private String version;
	//查询服务器是否是跨服服务器(true:是跨服务器,false:不是 是正常的源服务器;)
	private boolean isKf;
	//查询服务器当前时间
	private String curTime;
	//查询服务器停机次数
	private int stopTimes;
	//查询服务器启动时的时间
	private String bootTime;
	//查询服务器开服时间
	private String kfTime;
	//查询服务器合服时间
	private String hfTime = "null";
	//查询跨服状态(-1:未读取到跨服配置，其他:已成功连接上跨服的连接数)
	private String kfCount;
	//查询游戏内存中的允许的最高在线人数
	private int maxOnlineCount;
	//是否打开了redis
	private boolean openRedis;
	
	
	
	public boolean isOpenRedis() {
		return openRedis;
	}
	public void setOpenRedis(boolean openRedis) {
		this.openRedis = openRedis;
	}
	public int getOnlineCount() {
		return onlineCount;
	}
	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isKf() {
		return isKf;
	}
	public void setKf(boolean isKf) {
		this.isKf = isKf;
	}
	public String getCurTime() {
		return curTime;
	}
	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}
	public String getKfTime() {
		return kfTime;
	}
	public void setKfTime(String kfTime) {
		this.kfTime = kfTime;
	}
	public String getHfTime() {
		return hfTime;
	}
	public void setHfTime(String hfTime) {
		this.hfTime = hfTime;
	}
	public int getStopTimes() {
		return stopTimes;
	}
	public void setStopTimes(int stopTimes) {
		this.stopTimes = stopTimes;
	}
	public String getBootTime() {
		return bootTime;
	}
	public void setBootTime(String bootTime) {
		this.bootTime = bootTime;
	}
	public String getKfCount() {
		return kfCount;
	}
	public void setKfCount(String kfCount) {
		this.kfCount = kfCount;
	}
	public int getMaxOnlineCount() {
		return maxOnlineCount;
	}
	public void setMaxOnlineCount(int maxOnlineCount) {
		this.maxOnlineCount = maxOnlineCount;
	}
}
