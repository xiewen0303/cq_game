package com.junyou.kuafu.manager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.junyou.io.GameSession;
import com.junyou.io.global.DefaultChannelId;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.context.GameServerContext;
import com.junyou.log.ChuanQiLog;
import com.junyou.session.SessionConstants;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

public class KuafuSessionManager {
	/**
	 * 连接数
	 */
	private static AtomicInteger channelNum = new AtomicInteger(0);
	/**
	 * userRoleId跟serverId对应的关系
	 */
	private static Map<Long, String> userRoleIdServerIdMap = new ConcurrentHashMap<Long, String>();
	/**
	 * serverId跟游标对应的关系
	 */
	private static Map<String, AtomicInteger> coursors = new ConcurrentHashMap<String, AtomicInteger>();
	/**
	 * 所有的玩家
	 */
	private static List<Long> userRoleIds = new Vector<>();
	/**
	 * serverId对应所有的session
	 */
	private static ConcurrentMap<String, List<GameSession>> serverSessionMap = new ConcurrentHashMap<>();
	/**
	 * channel对应的serverId
	 */
	private static ConcurrentMap<Channel, String> serverChannelMap = new ConcurrentHashMap<>();
	/**
	 * channel对应的session
	 */
	private static ConcurrentMap<Channel, GameSession> kuaFuSessionChannelMap = new ConcurrentHashMap<>();

	private static AtomicInteger getCoursor(String serverId) {
		return coursors.get(serverId);
	}

	private static void initCoursor(String serverId) {
		if (!coursors.containsKey(serverId)) {
			coursors.put(serverId, new AtomicInteger(0));
		}
	}

	public static void bindServerId(Long userRoleId, String serverId) {
		userRoleIdServerIdMap.put(userRoleId, serverId);
	}

	public static String getServerId(Long userRoleId) {
		return userRoleIdServerIdMap.get(userRoleId);
	}

	public static void addUserRoleId(Long userRoleId) {
		if (!userRoleIds.contains(userRoleId)) {
			userRoleIds.add(userRoleId);
			resetServerScore();
		}
	}

	public static void removeUserRoleId(Long userRoleId) {
		if (userRoleIds.remove(userRoleId)) {
			userRoleIdServerIdMap.remove(userRoleId);
			resetServerScore();
		}
	}

	public static List<Long> getAllUserRoleId() {
		return userRoleIds;
	}

	public static void removeAllUserRoleId() {
		userRoleIds.clear();
		resetServerScore();
	}

	public static String getServerId(GameSession session) {
		if (session == null) {
			return null;
		}
		return session.getServerId();
	}

	private static List<GameSession> getServerSessions(String serverId) {
		if (serverSessionMap.containsKey(serverId)) {
			return new ReadOnlyList<>(serverSessionMap.get(serverId));
		}
		return null;
	}

	public static GameSession getKuafuSessionByChannel(Channel channel) {
		return kuaFuSessionChannelMap.get(channel);
	}

	public static GameSession addServerSession(String serverId, Channel channel) {
		if (ObjectUtil.strIsEmpty(serverId)) {
			return null;
		}
		initCoursor(serverId);

		List<GameSession> list = serverSessionMap.get(serverId);
		if (list == null) {
			synchronized (serverSessionMap) {
				list = serverSessionMap.get(serverId);
				if (list == null) {
					list = new Vector<>();
					serverSessionMap.put(serverId, list);
				}
			}
		}

		String sessionId = DefaultChannelId.newInstance().asLongText();
		GameSession session = new GameSession(
				SessionConstants.KUAFU_SESSION_TYPE, channel, sessionId);
		session.setServerId(serverId);

		list.add(session);

		serverChannelMap.put(channel, serverId);
		kuaFuSessionChannelMap.put(channel, session);
		try {
			session.setIp(((InetSocketAddress) channel.remoteAddress())
					.getAddress().getHostAddress());
		} catch (Exception e) {
		}
		ChuanQiLog.info("{} connection kuafu success. ip:{}", serverId,
				session.getIp());

		return session;
	}

	public static void removeServerSession(Channel channel) {
		serverChannelMap.remove(channel);

		GameSession session = kuaFuSessionChannelMap.remove(channel);
		if (session != null) {
			removeServerSession(session);
		}
	}

	/**
	 * 移除服务器会话
	 * 
	 * @param session
	 * @return 同样需要移除的会话
	 */
	public static void removeServerSession(GameSession session) {
		String serverId = getServerId(session);
		if (!ObjectUtil.strIsEmpty(serverId)) {
			synchronized (serverSessionMap) {
				List<GameSession> list = serverSessionMap.get(serverId);
				if (list != null) {
					list.remove(session);
					if (list.size() == 0) {
						serverSessionMap.remove(serverId);
						kickAllUser(serverId);
					}
				}
			}
		} else {
			ChuanQiLog.error("session断开了，但是没有找到所属的serverId，sessionId{}",
					session.getId());
		}
	}

	public static void writeMsgToAllServer(Object msg) {
		for (String serverId : serverSessionMap.keySet()) {
			writeMsg(serverId, msg, 0);
		}
	}

	public static void writeMsg(final String serverId, final Object msg) {
		writeMsg(serverId, msg, 0);
	}

	public static void writeMsg(final String serverId, final Object msg,
			int times) {
		if (ObjectUtil.strIsEmpty(serverId)) {
			return;
		}
		final AtomicInteger coursor = getCoursor(serverId);
		if (coursor == null) {
			ChuanQiLog.error("serverId:{} dont havecoursor", serverId);
			return;
		}
		final List<GameSession> list = getServerSessions(serverId);
		if (list == null) {
			ChuanQiLog.error("no session for serverId:{}", serverId);
			return;
		}
		int size = list.size();
		if (times >= size) {
			ChuanQiLog
					.error("retry times limit,send2KuafuSource serverId:{} current session size:{}",
							serverId, size);
			return;
		}
		if (list != null && size > 0) {
			int left = (coursor.getAndIncrement() & Integer.MAX_VALUE) % size;
			GameSession session = null;
			try {
				session = list.get(left);
			} catch (Exception e) {
				times = times + 1;
				writeMsg(serverId, msg, times);
				return;
			}
			if (session != null && session.getChannel().isActive()) {
				ChannelFuture wf = session.sendDMsg(msg);

				if (wf != null) {
					wf.addListener(new ChannelFutureListener() {

						@Override
						public void operationComplete(
								ChannelFuture channelFuture) throws Exception {
							if (!channelFuture.isSuccess()) {
								ChuanQiLog.error("msg send fail retry");
								int left = (coursor.getAndIncrement() & Integer.MAX_VALUE)
										% (list.size());
								GameSession session = null;
								try {
									session = list.get(left);
								} catch (Exception e) {
									//
								}
								if (session != null
										&& session.getChannel().isActive()) {
									session.sendDMsg(msg);
								}
							}
						}
					});
				}

			} else {
				ChuanQiLog.error("channel is not active try times={}",times);
				times = times + 1;
				writeMsg(serverId, msg, times);
				return;
			}
		} else {
			ChuanQiLog
					.error("SESSION IS NULL OR CLOSED,send2KuafuSource session is null serverId:{}",
							serverId);
		}
	}

	private static void kickAllUser(String serverId) {
		if (ObjectUtil.strIsEmpty(serverId)) {
			return;
		}
		Map<Long, String> map = new HashMap<>(userRoleIdServerIdMap);
		for (Entry<Long, String> entry : map.entrySet()) {
			if (serverId.equals(entry.getValue())) {
				BusMsgSender.send2BusInner(entry.getKey(),
						InnerCmdType.INNER_KUAFU_LEAVE, null);
			}
		}
	}

	public static void channelNumIncr() {
		channelNum.incrementAndGet();
		resetServerScore();
	}

	public static void channelNumDecr() {
		channelNum.decrementAndGet();
		resetServerScore();
	}

	public static int getChannelNum() {
		return channelNum.get();
	}

	public static int getOnlineRoleNum() {
		return userRoleIds.size();
	}

	/**
	 * 跨服性能评分，连接数影响因子，越大，代表影响力越大
	 */
	private static final int CHANNEL_SIZE_FACTOR = 5;
	/**
	 * 跨服性能评分，在线玩家数影响因子，越大，代表影响力越大
	 */
	private static final int ONLINE_ROLE_SIZE_FACTOR = 4;

	public static void resetServerScore() {
		if (KuafuManager.isMatchServer()) {
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			return;
		}
		String serverId = ChuanQiConfigUtil.getServerId();
		String key = RedisKey.buildKuafuServerKey(serverId);
		if (!redis.exists(key)) {
			ChuanQiLog.error("服务服务器不存在 serverId={}", serverId);
			return;
		}
		int channelNum = getChannelNum();
		int onlineRoleNum = getOnlineRoleNum();
		ChuanQiLog.info("channelNum = {} onlineRoleNum={}", channelNum,
				onlineRoleNum);
		int score = channelNum * CHANNEL_SIZE_FACTOR + onlineRoleNum
				* ONLINE_ROLE_SIZE_FACTOR;
		redis.zadd(RedisKey.KUAFU_SERVER_LIST_KEY, score, serverId);
		redis.set(RedisKey.buildKuafuServerScoreKey(serverId), channelNum + "_"
				+ onlineRoleNum);
	}
}
