package com.junyou.context;

import com.junyou.analysis.GameAppConfig;
import com.junyou.analysis.KuafuAppConfig;
import com.junyou.analysis.ServerInfoConfig;
import com.junyou.bus.client.io.service.IoServiceImpl;
import com.junyou.bus.share.service.RoleStateService;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.public_.nodecontrol.service.NodeControlService;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.kernel.cache.redis.Redis;
import com.kernel.data.write.async_1.AsyncWriteManager;

/**
 * 游戏主要服务上下文
 * @author DaoZheng Yuan
 * 2014年12月16日 下午2:00:38
 */
/**
 * @author DaoZheng Yuan
 * 2015年6月10日 上午9:50:12
 */
public class GameServerContext {

	private static AsyncWriteManager asyncWriteManager;
	
	private static PublicRoleStateService publicRoleStateService;
	
	private static NodeControlService nodeControlService;
	
	private static RoleStateService roleStateService;
	
	private static SessionManagerExportService sessionManagerExportService;
	
	private static IoServiceImpl ioService;
	
	private static GameAppConfig gameAppConfig;
	
	private static ServerInfoConfig serverInfoConfig;
	
	private static KuafuAppConfig kuafuAppConfig;
	
	private static byte[] secdictBytes;
	
	private static Redis redis;


	public static AsyncWriteManager getAsyncWriteManager() {
		return asyncWriteManager;
	}

	public static void setAsyncWriteManager(AsyncWriteManager asyncWriteManager) {
		GameServerContext.asyncWriteManager = asyncWriteManager;
	}

	public static PublicRoleStateService getPublicRoleStateService() {
		return publicRoleStateService;
	}

	public static void setPublicRoleStateService(
			PublicRoleStateService publicRoleStateService) {
		GameServerContext.publicRoleStateService = publicRoleStateService;
	}

	public static NodeControlService getNodeControlService() {
		return nodeControlService;
	}

	public static void setNodeControlService(NodeControlService nodeControlService) {
		GameServerContext.nodeControlService = nodeControlService;
	}

	public static RoleStateService getRoleStateService() {
		return roleStateService;
	}

	public static void setRoleStateService(RoleStateService roleStateService) {
		GameServerContext.roleStateService = roleStateService;
	}

	public static IoServiceImpl getIoService() {
		return ioService;
	}

	public static void setIoService(IoServiceImpl ioService) {
		GameServerContext.ioService = ioService;
	}

	public static GameAppConfig getGameAppConfig() {
		return gameAppConfig;
	}

	public static void setGameAppConfig(GameAppConfig gameAppConfig) {
		GameServerContext.gameAppConfig = gameAppConfig;
	}

	public static ServerInfoConfig getServerInfoConfig() {
		return serverInfoConfig;
	}

	public static void setServerInfoConfig(ServerInfoConfig serverInfoConfig) {
		GameServerContext.serverInfoConfig = serverInfoConfig;
	}

	public static SessionManagerExportService getSessionManagerExportService() {
		return sessionManagerExportService;
	}

	public static void setSessionManagerExportService(
			SessionManagerExportService sessionManagerExportService) {
		GameServerContext.sessionManagerExportService = sessionManagerExportService;
	}

	public static KuafuAppConfig getKuafuAppConfig() {
		return kuafuAppConfig;
	}

	public static void setKuafuAppConfig(KuafuAppConfig kuafuAppConfig) {
		GameServerContext.kuafuAppConfig = kuafuAppConfig;
	}

	public static Redis getRedis() {
		return redis;
	}

	public static void setRedis(Redis redis) {
		GameServerContext.redis = redis;
	}

	public static byte[] getSecdictBytes() {
		return secdictBytes;
	}

	public static void setSecdictBytes(byte[] secdictBytes) {
		GameServerContext.secdictBytes = secdictBytes;
	}
}
