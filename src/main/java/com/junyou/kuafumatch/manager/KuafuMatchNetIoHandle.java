package com.junyou.kuafumatch.manager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.cmd.InnerCmdType;
import com.junyou.io.GameSession;
import com.junyou.io.swap.IoMsgSender;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.log.ChuanQiLog;

public class KuafuMatchNetIoHandle extends
		SimpleChannelInboundHandler<Object[]> {

	private static final Log IO_ERROR_LOGGER = LogFactory
			.getLog("io_error_logger");

	private static final Log DEBUG_LOGGER = LogFactory
			.getLog(KuafuMatchNetIoHandle.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		IO_ERROR_LOGGER.error(new StringBuffer("session[")
				.append(ctx.channel()).append("]"), cause);
		if (DEBUG_LOGGER.isDebugEnabled()) {
			DEBUG_LOGGER.debug("", cause);
		}
	}

	/** 断开连接 原先的channelClosed */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		KuafuSessionManager.removeServerSession(ctx.channel());
		ChuanQiLog.info("kuafu KuafuNetIoHandle sessionClosed ");
	}

	/**
	 * 建立连接 channelConnected
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
	}

	/** messageReceived */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object[] msg) {
		Object[] tmps = msg;
		short cmd = (short) tmps[0];
		Channel channel = ctx.channel();
		GameSession session = KuafuSessionManager
				.getKuafuSessionByChannel(channel);

		Long roleid = (Long) tmps[1];
		Object data = tmps[2];

		// 第一次连接
		if (InnerCmdType.DEFAULT_CMD == cmd) {
			Object[] fisrtData = (Object[]) data;

			session = KuafuSessionManager.addServerSession(
					fisrtData[1].toString(), channel);
			return;
		}
		if(InnerCmdType.KUAFU_ARENA_1V1_MATCH == cmd || InnerCmdType.KUAFU_ARENA_4V4_MATCH == cmd){
			//此指令视为 匹配服务器的登录指令 
			KuafuRoleLoginStateHelper.roleOnline(roleid);
			Object[] fisrtData = (Object[]) data;
			KuafuSessionManager.bindServerId(roleid, fisrtData[0].toString());
			KuafuSessionManager.addUserRoleId(roleid);
		}
		if(InnerCmdType.KUAFU_ARENA_4V4_TEAM_MATCH == cmd){
			Object[] fisrtData = (Object[]) data;
			String serverId = (String) fisrtData[0];
			Object[] members = (Object[]) fisrtData[1];
			for (Object e : members) {
				Object[] info = (Object[]) e;
				Long roleId = (Long) info[0];
				//此指令视为 匹配服务器的登录指令 
				KuafuRoleLoginStateHelper.roleOnline(roleId);
				KuafuSessionManager.bindServerId(roleId, serverId);
				KuafuSessionManager.addUserRoleId(roleId);
			}
		}

		handleCmd(cmd, roleid, session.getId(), data);
	}

	private void handleCmd(short command, Long roleId, String sessionId,
			Object data) {

		IoMsgSender.swap(new Object[] { command, data, 0,
				CmdGroupInfo.node_from_type_kuafu_source,
				CmdGroupInfo.broadcast_type_1, sessionId, null, roleId,// roleId
				null, null, null, null });// accountId
	}
}
