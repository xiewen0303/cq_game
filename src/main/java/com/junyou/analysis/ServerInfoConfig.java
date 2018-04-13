package com.junyou.analysis;

import com.alibaba.fastjson.JSON;



/**
 * 游戏基础启动配置信息
 * @author DaoZheng Yuan
 * 2014年11月15日 上午8:20:39
 */
public class ServerInfoConfig {
	
	/**
	 * 客户端执行线程池size(默认是cpu的N核 + 1)
	 */
	private int clientIoConcurrent;
	
	/**
	 * 是否开启角色名称加前缀;	
	 */
	private boolean openRoleNamePrefix;
	
	/**
	 * 角色名称前缀字符串
	 */
	private String roleNamePrefix;
	
	/**
	 * Gm指令是否开启
	 */
	private boolean gmOpen;
	
	/**
	 * 是否需要打开falsh安全
	 */
	private boolean needFlashSafe;
	
	/**
	 * 是否是腾讯版本
	 */
	private boolean isTencent;
	
	/**
	 * 是否下载内网配置到游戏本地(一般内网debug用,外网不开启)
	 */
	private boolean isDownload;
	
	/**
	 * 服务器系统相关配置目录地址
	 */
	private String remoteSysConfigBaseUrl;
	
	/**
	 * 服务器地图相关配置目录地址
	 */
	private String remoteMapConfigBaseUrl;
	
	/**
	 * mapList文件下载路径
	 */
	private String remoteMapListConfigBaseUrl;
	
	/**
	 * 服务器屏蔽字文件目录地址
	 */
	private String remotePbzBaseUrl;
	
	/**
	 * 语言标识(zh=简体中文)
	 */
	private String language;
	
	/**
	 * country:国家标识(CN=中国)
	 */
	private String country;
	
	/**
	 * 1RMB兑换多少元宝  (ybodds = 10 表示 1人民币 = 10元宝) 
	 */
	private int ybodds;
	
	/**
	 * 是否是只开启中文字符区间(主要是角色名字和公会名字用到字符集)
	 */
	private boolean isZwChar;


	public int getClientIoConcurrent() {
		return clientIoConcurrent;
	}

	public void setClientIoConcurrent(int clientIoConcurrent) {
		this.clientIoConcurrent = clientIoConcurrent;
	}

	public boolean isOpenRoleNamePrefix() {
		return openRoleNamePrefix;
	}

	public void setOpenRoleNamePrefix(boolean openRoleNamePrefix) {
		this.openRoleNamePrefix = openRoleNamePrefix;
	}

	public String getRoleNamePrefix() {
		return roleNamePrefix;
	}

	public void setRoleNamePrefix(String roleNamePrefix) {
		this.roleNamePrefix = roleNamePrefix;
	}

	public boolean isGmOpen() {
		return gmOpen;
	}

	public void setGmOpen(boolean gmOpen) {
		this.gmOpen = gmOpen;
	}

	public boolean isNeedFlashSafe() {
		return needFlashSafe;
	}

	public void setNeedFlashSafe(boolean needFlashSafe) {
		this.needFlashSafe = needFlashSafe;
	}

	
	/**
	 * 是否是腾讯版本
	 * @return true:是
	 */
	public boolean isTencent() {
		return isTencent;
	}

	public void setTencent(boolean isTencent) {
		this.isTencent = isTencent;
	}
	
	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	
	/**
	 * 服务器地图相关配置目录地址
	 * @return
	 */
	public String getRemoteSysConfigBaseUrl() {
		return remoteSysConfigBaseUrl;
	}

	public void setRemoteSysConfigBaseUrl(String remoteSysConfigBaseUrl) {
		this.remoteSysConfigBaseUrl = remoteSysConfigBaseUrl;
	}

	/**
	 * 服务器地图相关配置目录地址
	 * @return
	 */
	public String getRemoteMapConfigBaseUrl() {
		return remoteMapConfigBaseUrl;
	}

	public void setRemoteMapConfigBaseUrl(String remoteMapConfigBaseUrl) {
		this.remoteMapConfigBaseUrl = remoteMapConfigBaseUrl;
	}
	
	/**
	 * mapList文件下载路径
	 * @return
	 */
	public String getRemoteMapListConfigBaseUrl() {
		return remoteMapListConfigBaseUrl;
	}

	public void setRemoteMapListConfigBaseUrl(String remoteMapListConfigBaseUrl) {
		this.remoteMapListConfigBaseUrl = remoteMapListConfigBaseUrl;
	}

	/**
	 * 服务器屏蔽字文件目录地址
	 * @return
	 */
	public String getRemotePbzBaseUrl() {
		return remotePbzBaseUrl;
	}

	public void setRemotePbzBaseUrl(String remotePbzBaseUrl) {
		this.remotePbzBaseUrl = remotePbzBaseUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * 1RMB兑换多少元宝  (ybodds = 10 表示 1人民币 = 10元宝) 
	 * @return
	 */
	public int getYbodds() {
		return ybodds;
	}

	public void setYbodds(int ybodds) {
		this.ybodds = ybodds;
	}
	
	
	/**
	 * 是否是只开启中文字符区间(主要是角色名字和公会名字用到字符集)
	 * @return
	 */
	public boolean isZwChar() {
		return isZwChar;
	}

	public void setZwChar(boolean isZwChar) {
		this.isZwChar = isZwChar;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
}
