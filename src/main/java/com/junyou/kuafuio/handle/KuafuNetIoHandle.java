package com.junyou.kuafuio.handle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.io.GameSession;
import com.junyou.io.swap.IoMsgSender;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.number.LongUtils;
import com.kernel.utils.SerializableHelper;

public class KuafuNetIoHandle extends SimpleChannelInboundHandler<Object[]> {

	private static final Log IO_ERROR_LOGGER = LogFactory.getLog("io_error_logger");

	private static final Log DEBUG_LOGGER = LogFactory.getLog(KuafuNetIoHandle.class);

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		IO_ERROR_LOGGER.error(new StringBuffer("session[").append(ctx.channel()).append("]"),cause);
		if(DEBUG_LOGGER.isDebugEnabled()){
			DEBUG_LOGGER.debug("",cause);
		}
	}
	
	/** 断开连接 原先的channelClosed */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		KuafuSessionManager.channelNumDecr();
		KuafuSessionManager.removeServerSession(ctx.channel());
		ChuanQiLog.info("kuafu KuafuNetIoHandle sessionClosed ");
	}
	
	/**
	 * 建立连接 channelConnected 
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		KuafuSessionManager.channelNumIncr();
	}


	/** messageReceived */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object[] msg) {
		Object[] tmps = msg;
		short cmd = (short)tmps[0];
		
		Channel channel = ctx.channel();
		GameSession session = KuafuSessionManager.getKuafuSessionByChannel(channel);
		
		Long roleid = (Long)tmps[1];
		Object data = tmps[2];
		
		if(InnerCmdType.KUAFU_QUIT == cmd){
			//跨服配置变动，主动关闭当前连接，T了所有在跨服中的玩家 
			if(GameConstants.DEFAULT_ROLE_ID.equals(roleid)){
				List<Long> allUserRoleId = KuafuSessionManager.getAllUserRoleId();
				for(Long e:allUserRoleId){
					//移除跨服场景中的玩家
					BusMsgSender.send2BusInner(e, InnerCmdType.KUAFU_OFFLINE1, null);
				}
				KuafuSessionManager.removeAllUserRoleId();
				return;
			}else{
				BusMsgSender.send2BusInner(roleid, InnerCmdType.KUAFU_OFFLINE1, null);
				KuafuSessionManager.removeUserRoleId(roleid);
				return;
			}
		}
		
		//第一次连接
		if(InnerCmdType.DEFAULT_CMD == cmd){
			Object []fisrtData = (Object[])data;
			
			session = KuafuSessionManager.addServerSession(fisrtData[1].toString(), channel);
			//通知原服跨服当前进行的活动
			int activeId = KuafuConfigUtil.getKfActiveId();
			if(activeId > 0){
				session.sendMsg(SerializableHelper.serialize(new Object[]{InnerCmdType.KUAFU_ACTIVE_START, GameConstants.DEFAULT_ROLE_ID, activeId}));
			}
			return;
		}
		if(InnerCmdType.BIND_ROLE_ID_SERVERID == cmd){
			Object []bindData = (Object[])data;
			Long userRoleId=LongUtils.obj2long(bindData[1]);
			String serverId = (String)bindData[0];
			KuafuSessionManager.bindServerId(userRoleId, serverId);
			return;
		}
		if(InnerCmdType.CLEAR_ROLE_KUAFU == cmd){
			Long userRoleId=LongUtils.obj2long(data);
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
					null);
			return;
		}
		
		if(!GameConstants.DEFAULT_ROLE_ID.equals(roleid)){
			KuafuSessionManager.addUserRoleId(roleid);
		}
		
		handleCmd(cmd, roleid, session.getId(), data);
	}

	
	
	private void handleCmd(short command, Long roleId, String sessionId, Object data){
		
		IoMsgSender.swap(new Object[]{command,
									data,
									0,
									CmdGroupInfo.node_from_type_kuafu_source,
									CmdGroupInfo.broadcast_type_1,
									sessionId,
									null,
									roleId,//roleId
									null,
									null,
									null,
									null});//accountId
	}
}
