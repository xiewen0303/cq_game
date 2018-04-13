package com.junyou.io.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.bus.kuafu_dianfeng.utils.KuafuDianFengUtils;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1SourceService;
import com.junyou.bus.shenmo.service.KuafuArena4v4SourceService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.io.GameSession;
import com.junyou.io.IOExecutorsManager;
import com.junyou.io.global.GameSessionManager;
import com.junyou.io.global.GsCountChecker;
import com.junyou.io.swap.IoMsgSender;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.kuafumatch.manager.KuafuMatchMsgSender;
import com.junyou.session.SessionConstants;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;


/** 
 * 服务器接收客户端数据handle
 */
public class NetIoServerHandler extends SimpleChannelInboundHandler<Object[]> {
	private static final Logger logger = LogManager.getLogger(NetIoServerHandler.class);

	public NetIoServerHandler() {
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object e) throws Exception {
		if (e instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) e;
			logger.error("close the channel: heartbeat {},type={}", ctx.channel(), event.state());
			ctx.close();
		}
	}

	/**
	 * 建立连接 channelConnected 
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		logger.info("{} connect", channel);
		String remoteIp = ((InetSocketAddress)channel.remoteAddress()).getAddress().getHostAddress();
		
		if(GsCountChecker.isFull() && GsCountChecker.notWhiteIp(remoteIp)){
			//在线人数已满，同时不在IP白名单内的直接断开连接
			channel.close();
		}else{
			GameSession session = GameSessionManager.getInstance().addSession(SessionConstants.NOMAL_SESSION_TYPE,channel);
			IOExecutorsManager.bindExecutorService(session);
			//设置IP值
			session.setIp(remoteIp);
		}
	}

	/** 断开连接 原先的channelClosed */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.error("{} channelClosed", ctx.channel());
		GameSession session = GameSessionManager.getInstance().getSession(ctx.channel());
		if (session == null) {
			logger.error("{} channel closed: no session id founded", ctx.channel());
		} else {
			Long roleId = session.getRoleId();
			if(roleId > 0){
				// 处理是否需要下线通知
				//回收老session:
				exitNotify(session);
				
				//跨服信息回收
				if(KuafuManager.kuafuIng(roleId) || KuafuArena1v1SourceService.isInKuafuArena(roleId)){
					KuafuMsgSender.send2KuafuServer(roleId,roleId, InnerCmdType.INNER_KUAFU_LEAVE, true);
					if(KuafuArena1v1SourceService.isInMatch(roleId)){
						KuafuMatchMsgSender.send2KuafuMatchServer(roleId, InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH, true);
						KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(roleId, InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH, true);
					}
				}
				if(KuafuManager.kuafuIng(roleId) || KuafuArena4v4SourceService.isInKuafuArena(roleId)){
					KuafuMsgSender.send2KuafuServer(roleId,roleId, InnerCmdType.INNER_KUAFU_LEAVE, false);
					if(KuafuArena4v4SourceService.isInMatch(roleId)){
						KuafuMatchMsgSender.send2KuafuMatchServer(roleId, InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH, true);
						KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(roleId, InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH, true);
					}
				}
				Redis redis = GameServerContext.getRedis();
				if(redis!=null){
					if(redis.get(RedisKey.getKuafuBossBindServerIdKey(roleId))!=null){
						KuafuServerInfo serverInfo =	KuafuRoleServerManager.getInstance().getBindServer(roleId);
						if(serverInfo!=null){
							KuafuMsgSender.send2KuafuServer(roleId, roleId,
									InnerCmdType.KUAFUBOSS_EXIT, null);
						}
					}
					if(redis.get(RedisKey.getKuafuQunXianYanBindServerIdKey(roleId))!=null){
						KuafuServerInfo serverInfo =	KuafuRoleServerManager.getInstance().getBindServer(roleId);
						if(serverInfo!=null){
							KuafuMsgSender.send2KuafuServer(roleId, roleId,InnerCmdType.KUAFUQUNXIANYAN_EXIT, null);
						}
					}
					//巅峰之战跨服战玩家在比赛中途下线处理
                    if (KuafuManager.kuafuIng(roleId) && redis.exists(RedisKey.KUAFU_DIANFENG_SERVER_ID)) {
                        String kfDianFengServerId = KuafuDianFengUtils.getDianFengKuaFuServerId();
                        KuafuServerInfo kfServerInfo = KuafuRoleServerManager.getInstance().getBindServer(roleId);
                        if (null != kfServerInfo && kfServerInfo.getServerId().equals(kfDianFengServerId)) {
                            //ChuanQiLog.info("巅峰之战跨服战期间,玩家{0}在比赛中途下线处理", roleId);
                            KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, roleId, InnerCmdType.KUAFU_DIANFENG_OFFINE, new Object[] { roleId });
                        }
                    }
                    
				}
				KuafuManager.removeKuafu(roleId);
			}
			IOExecutorsManager.removebindExecutorService(session);
			logger.info("channelInactive {}/{} closed", ctx.channel(), session.getId());
		}
		
		//容错
		GameSessionManager.getInstance().removeSession(ctx.channel());
	}

	/** messageReceived */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object[] msg) {
		try {
			Channel channel = ctx.channel();
			GameSession session = GameSessionManager.getInstance().getSession(channel);
			
			short cmd = (Short) msg[0];
			//需要预处理业务
			if(cmd == ClientCmdType.IN){
				String account_id = ((String)((Object[])msg[1])[0]).trim();
				String server_id = ((String)((Object[])msg[1])[1]).trim();
				boolean is_chenmi = (boolean) ((Object[])msg[1])[2];
				
				GameSession inAccountSession = GameSessionManager.getInstance().getSession4UserId(account_id,server_id);
				//挤出判断
				if(null != inAccountSession){
					inAccountSession.setJichu(true);
					
					//断开连接
					inAccountSession.getChannel().close();
				}
				
				session.setChenmi(is_chenmi);
				GameSessionManager.getInstance().addSession4User(session, account_id, server_id);
				
			}else if(cmd == ClientCmdType.CREATE_ROLE || cmd == ClientCmdType.CLIENT_APPLY_ENTER_GAME){
				//如果是创角、登陆指令,处理必要数据
				
				String account_id = session.getUserId();
				String server_id = session.getServerId();
				
				msg[1] = new Object[]{account_id,server_id,msg[1]};
			}
			
			
			IoMsgSender.swap(new Object[]{cmd,msg[1],0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
		} catch (Exception e) {
			logger.error("channelRead0 failed: " + e.getMessage(), e);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		logger.error(e.getMessage(), e);
		if (ctx.channel().isActive()) {
			ctx.close();
		}
	}
	
	
	private void exitNotify(GameSession session) throws Exception{
		IoMsgSender.swap(new Object[]{InnerCmdType.NODE_INIT_OUT,null,0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
	}
}