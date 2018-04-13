package com.junyou.kuafumatch.manager;

import java.util.Map;

import com.junyou.context.GameServerContext;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnelPool;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnelPoolConfig;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

public class KuafuCompetitionMatchServerManager {
	private static KuafuNetTunnelPool pool;
	private static KuafuNetTunnelPoolConfig poolConfig = new KuafuNetTunnelPoolConfig();

	static {
		/**
		 * 为了减少与跨服的连接数，开启回收器，59秒检测一次，若发现有空闲时间超过100s的连接，将会回收此连接
		 */
		/** 回收器检测间隔 */
		poolConfig.setTimeBetweenEvictionRunsMillis(59 * 1000L);
		/** tunnel空闲阈值，超过将被回收 */
		poolConfig.setMinEvictableIdleTimeMillis(100 * 1000L);
	}

	public static KuafuNetTunnel getKuafuMatchConnection() {
		if (pool == null) {
			initPool();
		}
		if (pool == null) {
			return null;
		}
		KuafuNetTunnel ret = pool.getResource();
		if(ret == null){
			pool.destroy();
			initPool();
			ret = pool.getResource();
		}else if (ret != null && !ret.isConnected()) {
			pool.returnBrokenResource(ret);
			pool.destroy();
			initPool();
			ret = pool.getResource();
		}
		return ret;
	}

	private static void initPool() {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis is not init");
			return;
		}
		String kuafuMatchServerId = redis.get(RedisKey.COMPETITION_KUAFU_MATCH_SERVER);
		if (kuafuMatchServerId == null) {
			ChuanQiLog.error("competition kuafuMatchServerId is null ");
			return;
		}
		Map<String, String> serverInfoMap = redis.hgetAll(RedisKey.buildKuafuServerKey(kuafuMatchServerId));
		if(serverInfoMap == null || serverInfoMap.isEmpty()){
			ChuanQiLog.error("kuafu serverInfoMap is null ");
			return;
		}
		String inIp = serverInfoMap.get(KuafuServerInfo.IN_IP_FIELD_KEY);
		String outIp = serverInfoMap.get(KuafuServerInfo.OUT_IP_FIELD_KEY);
		String netSegment = serverInfoMap
				.get(KuafuServerInfo.NET_SEGMENT_FIELD_KEY);
		String port = serverInfoMap.get(KuafuServerInfo.PORT_FIELD_KEY);
		String myNetSegment = redis.hget(
				RedisKey.buildKuafuServerKey(ChuanQiConfigUtil.getServerId()),
				KuafuServerInfo.NET_SEGMENT_FIELD_KEY);
		if(myNetSegment == null){
			ChuanQiLog.error("source server can not found in redis, serverId={}",ChuanQiConfigUtil.getServerId());
			return;
		}
		String ip = null;
		if (myNetSegment.equals(netSegment)) {
			ip = inIp;
		} else {
			ip = outIp;
		}
		KuafuServerInfo serverInfo = new KuafuServerInfo();
		serverInfo.setIp(ip);
		serverInfo.setPort(Integer.parseInt(port));
		serverInfo.setServerId(kuafuMatchServerId);
		serverInfo.setMatchServer(true);
		serverInfo.setCompetitionMatchServer(true);
		pool = new KuafuNetTunnelPool(poolConfig, serverInfo);
		ChuanQiLog
				.info("kuafu competition match server connection pool init ip={},port={},serverId={}",
						new Object[] { ip, port, kuafuMatchServerId });
	}

	public static void returnKuafuMatchConnection(KuafuNetTunnel kuafuNetTunnel) {
		if (pool != null && kuafuNetTunnel != null) {
			pool.returnResource(kuafuNetTunnel);
		}
	}

	public static void returnKuafuMatchBrokenConnection(
			KuafuNetTunnel kuafuNetTunnel) {
		if (pool != null && kuafuNetTunnel != null) {
			pool.returnBrokenResource(kuafuNetTunnel);
		}
	}
	public static void register() {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			return;
		}
		String serverId = ChuanQiConfigUtil.getServerId();
		String matchServerId = redis.get(RedisKey.COMPETITION_KUAFU_MATCH_SERVER);
		if(matchServerId!=null && !matchServerId.equals(serverId)){
			throw new RuntimeException("多次注册比赛匹配服务器错误，请确保比赛匹配服务器只有一台！如果想变更匹配服务器，请先修改redis内的"+RedisKey.COMPETITION_KUAFU_MATCH_SERVER+"的值");
		}
		
		if (redis.exists(RedisKey.buildKuafuServerKey(serverId))) {
			redis.set(RedisKey.COMPETITION_KUAFU_MATCH_SERVER, serverId);
			//容错 防止匹配服务器出现在跨服服务器的列表里 如果出现 删掉
			redis.zrem(RedisKey.KUAFU_SERVER_LIST_KEY, serverId);
		} else {
			ChuanQiLog.error("跨服服务器没有找到相关配置信息 serverId={}", serverId);
		}
	}
}
