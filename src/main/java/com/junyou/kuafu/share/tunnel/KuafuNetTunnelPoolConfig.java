package com.junyou.kuafu.share.tunnel;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

/**
-------默认配置------- </br>
*允许最大空闲对象数 </br>
*maxIdle = 8; </br>
*允许最小空闲对象数  </br>
*minIdle = 0; </br>
*允许最大活动对象数  </br>
*maxActive = 8; </br>
*允许最大等待时间毫秒数  </br>
*maxWait = -1L; </br>
*当池中对象用完时，请求新的对象所要执行的动作  </br>
*whenExhaustedAction = 1; </br>
*指明是否在从池中取出对象前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个. </br>
* testOnBorrow = false; </br>
*指明是否在归还到池中前进行检验 </br>
*testOnReturn = false; </br>
*指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除. </br>
*testWhileIdle = false; </br>
*在空闲对象回收器线程运行期间休眠的时间毫秒数. 如果设置为非正数,则不运行空闲连接回收器线程  </br>
*timeBetweenEvictionRunsMillis = -1L; </br>
*设定在进行后台对象清理时，每次检查对象数  </br>
*numTestsPerEvictionRun = 3; </br>
*被空闲对象回收器回收前在池中保持空闲状态的最小时间毫秒数  </br>
*minEvictableIdleTimeMillis = 1800000L; </br>
*/
public class KuafuNetTunnelPoolConfig extends Config {

	public static KuafuNetTunnelPoolConfig getDefaultInstance() {
		KuafuNetTunnelPoolConfig config = new KuafuNetTunnelPoolConfig();
		return config;
	}


	public KuafuNetTunnelPoolConfig() {

	}
	
	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public byte getWhenExhaustedAction() {
		return whenExhaustedAction;
	}

	public void setWhenExhaustedAction(byte whenExhaustedAction) {
		this.whenExhaustedAction = whenExhaustedAction;
	}
	/**
	 * 连接池
	 * @return
	 */
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(
			long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

}
