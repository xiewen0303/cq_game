package com.junyou.kuafu.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

public class KuafuManager {

	private static Boolean matchServer = null;
	
	private static KuafuManager instance = new KuafuManager();

	private Map<Long, Long> KF_STATE = new ConcurrentHashMap<Long, Long>();
	
	public static boolean OPEN_KUAFU_DEBUG_LOG = false;

//	private static Map<Long, Object> LEAVE_MAP = new HashMap<Long, Object>();

//	/**
//	 * 开始在跨服切地图
//	 * 
//	 * @param roleId
//	 */
//	public static void startChange(Long roleId) {
//		LEAVE_MAP.put(roleId, null);
//	}
//
//	/**
//	 * 是否在跨服切地图
//	 * 
//	 * @param roleId
//	 * @return
//	 */
//	public static boolean changeKfIng(Long roleId) {
//		return LEAVE_MAP.get(roleId) != null;
//	}
//
//	/**
//	 * 移除玩家的跨服切地图状态
//	 * 
//	 * @param roleId
//	 */
//	public static void removeChange(Long roleId) {
//		LEAVE_MAP.remove(roleId);
//	}
//
	/**
	 * 开始跨服
	 */
	public static void startKuafu(Long roleId) {
		instance.KF_STATE.put(roleId, GameSystemTime.getSystemMillTime());
		if(OPEN_KUAFU_DEBUG_LOG){
			ChuanQiLog.error("startKuafu roleId={}",roleId,new RuntimeException());
		}
		StageMsgSender.send2One(roleId, ClientCmdType.BIAOJI_KUAFU_STATE, true);
	}

	/**
	 * 是否处于跨服状态
	 */
	public static boolean kuafuIng(Long roleId) {
		return instance.KF_STATE.get(roleId) != null;
	}

	/**
	 * 移除玩家的跨服状态
	 * 
	 * @param roleId
	 */
	public static void removeKuafu(Long roleId) {
		instance.KF_STATE.remove(roleId);
		if(OPEN_KUAFU_DEBUG_LOG){
			ChuanQiLog.error("removeKuafu roleId={}",roleId,new RuntimeException());
		}
		StageMsgSender.send2One(roleId, ClientCmdType.BIAOJI_KUAFU_STATE, false);
	}

	public static List<Long> getAllRoleIds() {
		return new ArrayList<Long>(instance.KF_STATE.keySet());
	}

	public static boolean isMatchServer() {
		if (matchServer == null) {
			matchServer = ChuanQiConfigUtil.getIsGlobalMatchServer();
		}
		return matchServer;
	}
	
	public static void clearRoleKuafu(Long userRoleId){
		Redis redis = GameServerContext.getRedis();
		if(redis!=null){
			long count = redis.zcount(RedisKey.KUAFU_SERVER_LIST_KEY);
			Set<String> kuafuServerIds = redis.zrange(RedisKey.KUAFU_SERVER_LIST_KEY, 0, count);
			if(kuafuServerIds!=null){
				for(String e:kuafuServerIds){
					KuafuServerInfo serverInfo=	KuafuServerInfoManager.getInstance().getKuafuServerInfo(e);
					if(serverInfo!=null){
						// 校验连接是否可用
						KuafuNetTunnel tunnel = KuafuConfigUtil.getConnection(serverInfo);
						if (tunnel != null && tunnel.isConnected()) {
							KuafuConfigUtil.returnConnection(tunnel);
							KuafuRoleServerManager.getInstance().bindServer(userRoleId, serverInfo);
							KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
									userRoleId, InnerCmdType.CLEAR_ROLE_KUAFU, userRoleId);
						}else{
							KuafuConfigUtil.returnBrokenConnection(tunnel);
						}
					}
				}
			}
			KuafuRoleServerManager.getInstance().removeBind(userRoleId);
		}
	
	}

}
