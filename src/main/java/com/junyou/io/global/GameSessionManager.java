package com.junyou.io.global;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.io.GameSession;
import com.junyou.session.SessionConstants;

/**
 * 自定义session管理器
 * @author ydz
 * @date 2015-7-13 下午3:33:04
 */
public class GameSessionManager {
	
	private static final Logger logger = LogManager.getLogger(GameSessionManager.class);
	
	private GameSessionManager(){
	}

	public static GameSessionManager getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private static class InstanceHolder {
		private static final GameSessionManager INSTANCE = new GameSessionManager();
	}
	
	private ConcurrentMap<Channel, GameSession> sessionMap = new ConcurrentHashMap<>();
	private ConcurrentMap<String, GameSession> sessionIdMap = new ConcurrentHashMap<>();
	private ConcurrentMap<String, GameSession> userMap = new ConcurrentHashMap<>();
	private ConcurrentMap<Long, GameSession> roleMap = new ConcurrentHashMap<>();
	
	private ConcurrentMap<String, Map<Long,Object>> ipMap = new ConcurrentHashMap<String, Map<Long,Object>>();
	
	private final ReentrantLock LOCK = new ReentrantLock();
	
	
	public synchronized GameSession addSession(byte type,Channel channel) {
		String sessionId = DefaultChannelId.newInstance().asLongText();
		GameSession result = new GameSession(type,channel, sessionId);
		logger.info("create session {}", sessionId);
		sessionIdMap.put(sessionId, result);
		sessionMap.put(channel, result);
		return result;
	}
	
	/**
	 * 获取不同IP数量
	 * @return
	 */
	public int getSessionIps(){
		return ipMap.size();
	}
	
	public void addIp(GameSession session){
		try {
			if(session == null){
				return;
			}
			
			String ip = session.getIp();
			if(ip == null){
				return;
			}
			
			Map<Long,Object> roleIds = ipMap.get(ip);
			if(roleIds == null){
				roleIds = new HashMap<Long, Object>();
				ipMap.put(ip, roleIds);
			}
			roleIds.put(session.getRoleId(), null);
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	
	public void delIp(GameSession session){
		try {
			if(session == null){
				return;
			}
			
			String ip = session.getIp();
			if(ip == null){
				return;
			}
			
			Map<Long,Object> roleIds = ipMap.get(ip);
			if(roleIds != null){
				
				if(roleIds.size() == 1){
					ipMap.remove(ip);
				}else{
					roleIds.remove(session.getRoleId());
				}
			}
			
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	public GameSession getSession(Channel channel) {
		return sessionMap.get(channel);
	}

	public Collection<GameSession> getOnlineRoleList() {
		return sessionMap.values();
	}
	
	public boolean isOnline(long roleId) {
		return roleMap.containsKey(roleId);
	}
	
	public synchronized void addSession4Role(GameSession session, long roleId) {
		// logger.info("加入session 管理 sessionId={}", sessionId);
		session.setRoleId(roleId);
		roleMap.put(roleId, session);
		
		//ip记录
		addIp(session);
	}
	
	public synchronized void addSession4User(GameSession session,String userId,String serverId){
		session.setUserId(userId);
		session.setServerId(serverId);
		userMap.put(getAccountKey(userId, serverId), session);
	}
	
	
	/**
	 * 保存角色名称
	 * @param name
	 * @param level
	 * @param name
	 */
	public synchronized void saveSessin4Name(Long roleId,String name){
		GameSession session = this.getSession4RoleId(roleId);
		if (session != null) {
			//修改角色名字
			session.setRoleName(name);
		}
	}
	
	/**
	 * 保存角色等级
	 * @param name
	 * @param level
	 * @param level
	 */
	public synchronized void saveSessin4Level(Long roleId,int level){
		GameSession session = this.getSession4RoleId(roleId);
		if (session != null) {
			//修改等级
			session.setLevel(level);
		}
	}
	/**
	 * 保存角色等级
	 * @param name
	 * @param level
	 * @param level
	 */
	public synchronized void saveSessin4RoleBaseInfo(Long roleId,String name,Integer configId,int level,Long createTime){
		GameSession session = this.getSession4RoleId(roleId);
		if (session != null) {
			//修改角色名字
			session.setRoleName(name);
			//修改等级
			session.setLevel(level);
			//角色配置ID（职业）
			session.setConfigId(configId);
			//角色创号时间
			session.setCreateTime(createTime);
		}
	}
	
	
	public long getOnlineRoleId(Long roleId) {
		long ret = 0;
		GameSession session = this.getSession4RoleId(roleId);
		if (session != null) {
			ret = session.getRoleId();
		}
		return ret;
	}

	/**
	 * 根据ip获取roleId列表
	 * @param ip
	 * @return
	 */
	public Map<Long,Object> getRoleIdsByIp(String ip){
		return ipMap.get(ip);
	}
	
	public GameSession getSession4Id(String sessionId){
		return sessionIdMap.get(sessionId);
	}
	
	public GameSession getSession4RoleId(long roleId) {
		return roleMap.get(roleId);
	}
	
	
	public GameSession getSession4UserId(String userId,String serverId) {
		return userMap.get(getAccountKey(userId, serverId));
	}
	
	private String getAccountKey(String userId,String serverId){
		//暂不改用StringBuffer
		return userId + SessionConstants.ACCOUNT_KEY_JOIN + serverId;
	}
	

	public synchronized Collection<Long> removeSession(Channel channel) {
		Collection<Long> ret = new ArrayList<>();
		GameSession session = sessionMap.remove(channel);
		if (session != null) {
			sessionIdMap.remove(session.getId());
			String sessionId = session.getId();
			if (session.isNomalSession()) {
				String userId = session.getUserId();
				if (userId != null) {
					
					String tmpAccId = getAccountKey(userId, session.getServerId());
					GameSession userSession = userMap.get(tmpAccId);
					if (userSession != null && userSession.equals(session)) {
						userMap.remove(tmpAccId);
					}
				}
				long roleId = session.getRoleId();
				if (roleId != 0) {
					GameSession roleSession = roleMap.get(roleId);
					// fix bug 解决新的session被误干掉的问题
					if (roleSession != null && roleSession.equals(session)) {
						roleMap.remove(roleId);
						
						//去除ip记录
						delIp(roleSession);
					}
				}
			}else {
//				// 清理掉通过这个session通讯的所有玩家
//				ret = this.removeRoleList(session);
			}
			logger.info("remove session {}", sessionId);
		}

		return ret;
	}
	
	/**
	 * 关闭全服所有连接
	 */
	public void closeAllConnections() {
		logger.info("closeAllConnections");
		LOCK.lock();
		try {
			for (GameSession session : sessionMap.values()) {
				session.getChannel().disconnect();
			}
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 在线玩家个数
	 * 
	 * @return
	 */
	public int getOnlineCount() {
		return roleMap.size();
	}

	/** 连接数 */
	public int getConnectionNum() {
		return sessionIdMap.size();
	}
	
	
	/**
	 * 获取角色session列表
	 * @return
	 */
	public Collection<GameSession> getRoleListSession(){
		return roleMap.values();
	}
	
	/**
	 * 获取角色id列表
	 * @return
	 */
	public Set<Long> getRoleIdListSession(){
		return roleMap.keySet();
	}
}
