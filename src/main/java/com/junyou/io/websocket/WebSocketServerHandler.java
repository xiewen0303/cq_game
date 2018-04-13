package com.junyou.io.websocket;

import com.junyou.io.GameSession;
import com.junyou.io.IOExecutorsManager;
import com.junyou.io.global.GameSessionManager;
import com.junyou.io.global.GsCountChecker;
import com.junyou.session.SessionConstants;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;


/** 
 * 服务器接收客户端数据handle
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = LogManager.getLogger(WebSocketServerHandler.class);

	public WebSocketServerHandler() {
		
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
			IOExecutorsManager.removebindExecutorService(session);
			logger.info("channelInactive {}/{} closed", ctx.channel(), session.getId());
		}
		
		//容错
		GameSessionManager.getInstance().removeSession(ctx.channel());
	}




//	/** messageReceived */
//	@Override
//	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
//		try {
//			Channel channel = ctx.channel();
//			GameSession session = GameSessionManager.getInstance().getSession(channel);
//
//			short cmd = (Short) msg[0];
//			//需要预处理业务
//			if(cmd == ClientCmdType.IN){
//				String account_id = ((String)((Object[])msg[1])[0]).trim();
//				String server_id = ((String)((Object[])msg[1])[1]).trim();
//				boolean is_chenmi = (boolean) ((Object[])msg[1])[2];
//
//				GameSession inAccountSession = GameSessionManager.getInstance().getSession4UserId(account_id,server_id);
//				//挤出判断
//				if(null != inAccountSession){
//					inAccountSession.setJichu(true);
//
//					//断开连接
//					inAccountSession.getChannel().close();
//				}
//
//				session.setChenmi(is_chenmi);
//				GameSessionManager.getInstance().addSession4User(session, account_id, server_id);
//
//			}else if(cmd == ClientCmdType.CREATE_ROLE || cmd == ClientCmdType.CLIENT_APPLY_ENTER_GAME){
//				//如果是创角、登陆指令,处理必要数据
//
//				String account_id = session.getUserId();
//				String server_id = session.getServerId();
//
//				msg[1] = new Object[]{account_id,server_id,msg[1]};
//			}
//
//
//			IoMsgSender.swap(new Object[]{cmd,msg[1],0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
//		} catch (Exception e) {
//			logger.error("channelRead0 failed: " + e.getMessage(), e);
//		}
//	}
//
//	@Override
//	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
//		logger.error(e.getMessage(), e);
//		if (ctx.channel().isActive()) {
//			ctx.close();
//		}
//	}
//
//
//	private void exitNotify(GameSession session) throws Exception{
//		IoMsgSender.swap(new Object[]{InnerCmdType.NODE_INIT_OUT,null,0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
//	}
}