package com.junyou.kuafu.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

//跨服ip管理
public class KuafuServerInfoManager {
	private static KuafuServerInfoManager instance = new KuafuServerInfoManager();

	public static KuafuServerInfoManager getInstance() {
		return instance;
	}

	private Map<String, KuafuServerInfo> infoMap = new ConcurrentHashMap<String, KuafuServerInfo>();

	public KuafuServerInfo getKuafuServerInfo(String serverId) {
		return infoMap.get(serverId);
	}
	
	public void removeServerInfo(String serverId){
		infoMap.remove(serverId);
	}

	public void addKuafuServerInfo(KuafuServerInfo info) {
		infoMap.put(info.getServerId(), info);
	}
	
	public KuafuServerInfo getKuafuServerInfo(String serverId,Redis redis) {
		KuafuServerInfo serverInfo = KuafuServerInfoManager.getInstance()
				.getKuafuServerInfo(serverId);
		if(serverInfo!=null){
			return serverInfo;
		}
		Map<String, String> serverInfoMap = redis.hgetAll(RedisKey.buildKuafuServerKey(serverId));
		if (serverInfoMap == null  || serverInfoMap.isEmpty()) {
			ChuanQiLog.error("userRoleId ={} match success can not get kuafu server info serverId={}",serverId);
			redis.zrem(RedisKey.KUAFU_SERVER_LIST_KEY, serverId);
			redis.hset(RedisKey.KUAFU_DELETE_SERVER_LIST, serverId, DatetimeUtil.formatTime(GameSystemTime.getSystemMillTime(), DatetimeUtil.FORMART3));
			return null;
		}
		String inIp = serverInfoMap.get(KuafuServerInfo.IN_IP_FIELD_KEY);
		String outIp = serverInfoMap.get(KuafuServerInfo.OUT_IP_FIELD_KEY);
		String netSegment = serverInfoMap.get(KuafuServerInfo.NET_SEGMENT_FIELD_KEY);
		String port = serverInfoMap.get(KuafuServerInfo.PORT_FIELD_KEY);

		String myNetSegment = redis.hget(RedisKey.buildKuafuServerKey(ChuanQiConfigUtil.getServerId()),KuafuServerInfo.NET_SEGMENT_FIELD_KEY);
		if(myNetSegment == null){
			ChuanQiLog.error("source server can not found in redis, serverId={}",ChuanQiConfigUtil.getServerId());
			return null;
		}
		String ip = null;
		if (myNetSegment.equals(netSegment)) {
			ip = inIp;
		} else {
			ip = outIp;
		}
		serverInfo = new KuafuServerInfo();
		serverInfo.setIp(ip);
		serverInfo.setPort(Integer.parseInt(port));
		serverInfo.setServerId(serverId);
		infoMap.put(serverId, serverInfo);
		return serverInfo;
	}

}
