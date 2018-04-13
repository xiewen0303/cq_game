package com.junyou.analysis;

/**
 * 跨服游戏基础配置
 * @author DaoZheng Yuan
 * 2014年12月12日 下午5:42:17
 */
public class KuafuAppConfig {
	
	private boolean isKfServer;
	
	private int initSessionSize = 1;//初始化值是1
	
	private String protocol;
	
	private String kuafuConfigRemoteDir;//跨服远程配置目录

	/**
	 * 本服务器是否是跨服服务器 
	 * @return true:是跨服务器,false:不是 是正常的源服务器;
	 */
	public boolean isKfServer() {
		return isKfServer;
	}

	public void setKfServer(boolean isKfServer) {
		this.isKfServer = isKfServer;
	}

	/**
	 * 跨服务器和源服务器初始的session个数 
	 * @return
	 */
	public int getInitSessionSize() {
		return initSessionSize;
	}

	public void setInitSessionSize(int initSessionSize) {
		this.initSessionSize = initSessionSize;
	}

	/**
	 * 后端内部服务器之前通信的序列化和反序列协议目前只支持两种(fst 和   java)
	 * @return
	 */
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * 跨服远程配置目录 (相对目录)
	 * @return
	 */
	public String getKuafuConfigRemoteDir() {
		return kuafuConfigRemoteDir;
	}

	public void setKuafuConfigRemoteDir(String kuafuConfigRemoteDir) {
		this.kuafuConfigRemoteDir = kuafuConfigRemoteDir;
	}
	
}
