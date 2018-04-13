package com.junyou.kuafu.share.util;

import java.util.Set;

import com.junyou.context.GameServerContext;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnelPool;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnelPoolManager;
import com.junyou.log.ChuanQiLog;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

public class KuafuConfigUtil {
	/**
	 * 
	 */
	public static int CAN_NOT_CONNECT_CLEAN_TIMES = 3;

	/** 跨服活动id **/
	private static int kfActiveId = 0;

	public static KuafuNetTunnel getConnection(KuafuServerInfo serverInfo) {
		if (serverInfo != null) {
			return KuafuNetTunnelPoolManager.getInstance()
					.getTunnel(serverInfo);
		}
		return null;
	}

	public static void returnConnection(KuafuNetTunnel kuafuNetTunnel) {
		if (kuafuNetTunnel != null) {
			KuafuServerInfo serverInfo = kuafuNetTunnel.getServerInfo();
			KuafuNetTunnelPool pool = KuafuNetTunnelPoolManager.getInstance()
					.getPool(serverInfo);
			if (pool != null) {
				pool.returnResource(kuafuNetTunnel);
			}
		}
	}

	public static void returnBrokenConnection(KuafuNetTunnel kuafuNetTunnel) {
		if (kuafuNetTunnel != null) {
			KuafuServerInfo serverInfo = kuafuNetTunnel.getServerInfo();
			KuafuNetTunnelPool pool = KuafuNetTunnelPoolManager.getInstance()
					.getPool(serverInfo);
			if (pool != null) {
				pool.returnBrokenResource(kuafuNetTunnel);
			}
		}
	}

	/**
	 * 返回跨服是否处于联通状态
	 * 
	 * @return
	 */
	public static boolean isKuafuAvailable() {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis 没有开启");
			return false;
		}
		
		Set<String> serverIdSets = redis.zrange(RedisKey.KUAFU_SERVER_LIST_KEY,0, 0);
		if (serverIdSets == null || serverIdSets.size() == 0) {
			ChuanQiLog.error("没有任何跨服配置信息 check");
			return false;
		}
		return true;
	}

	/**
	 * 查询服务器连接状态
	 * 
	 * @return -1:没连接上跨服服务器,0:未读取到跨服配置，其他:已成功连接上跨服的连接数
	 */
	public static String getConnectionState() {
		if (!isKuafuAvailable()) {
			return "-1";
		}
		return "1";
	}

	public static void kfActiveStart(int activeId) {
		kfActiveId = activeId;
	}

	public static void kfActiveEnd() {
		kfActiveId = 0;
	}

	/**
	 * 跨服活动是否开启（此方法用于原服务器）
	 * 
	 * @param activeId
	 *            验证的活动id
	 * @return
	 */
	public static boolean isActiveKfing(Integer activeId) {
		return activeId.equals(kfActiveId);
	}

	/**
	 * 获取当前正在进行的跨服活动（此方法用于跨服服务器）
	 * 
	 * @return
	 */
	public static int getKfActiveId() {
		return kfActiveId;
	}
}
