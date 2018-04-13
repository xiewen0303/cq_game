package com.junyou.analysis;

/**
 * 游戏程序必要配置
 * @author DaoZheng Yuan
 * 2014年12月12日 下午4:25:06
 */
public class GameAppConfig {

	private boolean debug;
	
	private boolean checkBootDb;
	
	private boolean dsTiRole;
	
	private String platformId;
	
	private String serverId;
	
	private String platformServerId;
	
	private String serverName;
	
	private int gameServerPort;
	
	private int gameHttpPort;
	
	private boolean printJyLog;
	
	private String jyLogUrl;

	private boolean isUseLog2db;
	
	/**
	 * 服务器是是否使用第三方的log2db服务,对log日志的统计
	 * @return true:是
	 **/
	public boolean isUseLog2db() {
		return isUseLog2db;
	}

	public void setUseLog2db(boolean isUseLog2db) {
		this.isUseLog2db = isUseLog2db;
	}

	/**
	 * 服务器是否以调试模块运行(外网一般会关闭  isDebug=false)
	 * @return true:是调试模式
	 **/
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	
	/**
	 * 定时踢下线非活动玩家（10-15分钟分被服务器踢下线，原因是客户2~4秒会给服务器心跳包，没有收到客户端超过10到15分钟踢下线）
	 * @return
	 */
	public boolean isDsTiRole() {
		return dsTiRole;
	}

	public void setDsTiRole(boolean dsTiRole) {
		this.dsTiRole = dsTiRole;
	}
	
	/**
	 * 是否启动验证数据库和程序是一致的(一般到线上最好也开启)
	 * @return true:开启
	 */
	public boolean isCheckBootDb() {
		return checkBootDb;
	}

	public void setCheckBootDb(boolean checkBootDb) {
		this.checkBootDb = checkBootDb;
	}

	/**
	 * 平台ID标识(例:37平台  "37wan"  pps平台 "pps")
	 * @return
	 */
	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	/**
	 * 服务器ID标识只支持数字 (运维分配给这个服务器的数值,本游戏全世界唯一 serverId值区间[1,65535])
	 * @return
	 */
	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/**
	 * 服务器名称(主要是后面游戏可能要显示在业务面板上,只做显示)
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * 游戏启动主监听端口
	 * @return
	 */
	public int getGameServerPort() {
		return gameServerPort;
	}

	public void setGameServerPort(int gameServerPort) {
		this.gameServerPort = gameServerPort;
	}

	/**
	 * 游戏http服务器监听端口(可以理解为webservice端口)
	 * @return
	 */
	public int getGameHttpPort() {
		return gameHttpPort;
	}

	public void setGameHttpPort(int gameHttpPort) {
		this.gameHttpPort = gameHttpPort;
	}

	/**
	 * 本平台的服务器id
	 * @return
	 */
	public String getPlatformServerId() {
		return platformServerId;
	}

	public void setPlatformServerId(String platformServerId) {
		this.platformServerId = platformServerId;
	}

	public boolean isPrintJyLog() {
		return printJyLog;
	}

	public void setPrintJyLog(boolean printJyLog) {
		this.printJyLog = printJyLog;
	}

	public String getJyLogUrl() {
		return jyLogUrl;
	}

	public void setJyLogUrl(String jyLogUrl) {
		this.jyLogUrl = jyLogUrl;
	}
	
	
}
