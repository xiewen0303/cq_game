package com.junyou.kuafu.share.tunnel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;

public class KuafuNetTunnelPoolManager {
	private static KuafuNetTunnelPoolManager instance = new KuafuNetTunnelPoolManager();

	public static KuafuNetTunnelPoolManager getInstance() {
		return instance;
	}

	private Map<KuafuServerInfo, KuafuNetTunnelPool> poolMap = new ConcurrentHashMap<KuafuServerInfo, KuafuNetTunnelPool>();

	public KuafuNetTunnelPool getPool(KuafuServerInfo config) {
		return poolMap.get(config);
	}

	private static KuafuNetTunnelPoolConfig poolConfig = new KuafuNetTunnelPoolConfig();

	static {
		/**
		 * 为了减少与跨服的连接数，开启回收器，59秒检测一次，若发现有空闲时间超过1分钟的连接，将会回收此连接
		 */
		/** 回收器检测间隔 */
		poolConfig.setTimeBetweenEvictionRunsMillis(59 * 1000L);
		/** tunnel空闲阈值，超过将被回收 */
		poolConfig.setMinEvictableIdleTimeMillis(1800 * 1000L);
	}

	public KuafuNetTunnelPool createPool(KuafuServerInfo serverInfo) {
		KuafuNetTunnelPool pool = new KuafuNetTunnelPool(poolConfig, serverInfo);
		ChuanQiLog.info("create pool for server serverId={} ip={} port={}",
				new Object[] { serverInfo.getServerId(), serverInfo.getIp(),
						serverInfo.getPort() });
		return pool;
	}

	public KuafuNetTunnel getTunnel(KuafuServerInfo serverInfo) {
		KuafuNetTunnelPool pool = getPool(serverInfo);
		if (pool == null) {
			pool = createPool(serverInfo);
			poolMap.put(serverInfo, pool);
		}
		KuafuNetTunnel ret = pool.getResource();
		if(ret == null){
			pool.destroy();
			pool = createPool(serverInfo);
			poolMap.put(serverInfo, pool);
			ret = pool.getResource();
		}else if (ret != null && !ret.isConnected()) {
			pool.returnBrokenResource(ret);
			pool.destroy();
			pool = createPool(serverInfo);
			poolMap.put(serverInfo, pool);
			ret = pool.getResource();
		}
		return ret;
	}

	public void destroyPool(KuafuServerInfo serverInfo) {
		KuafuNetTunnelPool pool = poolMap.remove(serverInfo);
		if (pool != null) {
			pool.destroy();
		}
	}

	public int getKuafuNetPoolSize() {
		return poolMap.size();
	}

	public int getTotalTunnelSize() {
		int count = 0;
		for (KuafuNetTunnelPool e : poolMap.values()) {
			count = count + e.getNumActive();
		}
		return count;
	}
}
