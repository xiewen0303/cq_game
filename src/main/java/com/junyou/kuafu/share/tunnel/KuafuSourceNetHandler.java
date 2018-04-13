package com.junyou.kuafu.share.tunnel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.io.GameSession;
import com.junyou.io.global.GameSessionManager;
import com.junyou.io.swap.IoMsgSender;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerManager;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.messageswap.NodeSwap;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.KuafuCmdUtil;
import com.junyou.utils.codec.AmfUtil;
import com.junyou.utils.number.LongUtils;

public class KuafuSourceNetHandler extends SimpleChannelInboundHandler<Object[]> {

	private KuafuNetTunnel kuafuNetTunnel;
	
	public KuafuSourceNetHandler(KuafuNetTunnel kuafuNetTunnel) {
		super();
		ChuanQiLog.info("KuafuSourceNetHandler 已创建");
		this.kuafuNetTunnel = kuafuNetTunnel;
	}
	/**
	 * 建立连接 channelConnected 
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		KuafuServerManager.getInstance().addChannelNum(kuafuNetTunnel.getServerInfo().getServerId());
	}

	/** 断开连接 原先的channelClosed */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ChuanQiLog.info("kuauf {} channelClosed", ctx.channel());
		if(kuafuNetTunnel!=null && !kuafuNetTunnel.isConnected()){
			KuafuConfigUtil.returnBrokenConnection(kuafuNetTunnel);
		}
		String serverId = kuafuNetTunnel.getServerInfo().getServerId();
		KuafuServerManager.getInstance().removeChannelNum(serverId);
		if (KuafuServerManager.getInstance().getChannelNum(serverId)<=0) {
			List<Long> roleIds = KuafuRoleServerManager.getInstance().getKuafuRoleIds(serverId);
			if (roleIds == null || roleIds.isEmpty()) {
				return;
			}
			// 如果跨服服务器断开连接，则所有在跨服中的玩家立即执行切地图指令
			for (Long roleid : roleIds) {
				if(!KuafuManager.kuafuIng(roleid)){
					return;
				}
				ChuanQiLog.info("all kuauf connection broke kick roleId={} ", roleid);
				KuafuManager.removeKuafu(roleid);
				
				GameSession session = GameSessionManager.getInstance().getSession4RoleId(roleid);
				if(session != null){
					StageMsgSender.send2One(roleid, ClientCmdType.AOI_ELEMENT_CLEAR, null);
					IoMsgSender.swap(new Object[]{InnerCmdType.MORE_FUBEN_LEAVE_TEAM,roleid,0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
					
					IoMsgSender.swap(new Object[]{InnerCmdType.BAGUA_LEAVE_TEAM,roleid,0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
					
					IoMsgSender.swap(new Object[]{InnerCmdType.INNER_T_LEAVEL_COPY,roleid,0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
				}
			}
		}
	}

	/** messageReceived */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object[] msg) {
		// 收到跨服返回的MSG(roleId,cmd,data)
		
		try {
			Short cmd = (Short) msg[0];
			Long roleOutId = (Long) msg[1];
			Object data = msg[2];
			
			if (KuafuCmdUtil.isZhuanfaCmd(cmd)) {
				// 如果指令无需源服务器发送给单个玩家
				if (KuafuCmdUtil.isSendToOneCmd(cmd)) {
					Object[] datas = (Object[]) data;
					Short realCmd = (Short) datas[0];
					Object realData = datas[1];
					Long roleId = LongUtils.obj2long(datas[2]);
					
					GameSession outSession = GameSessionManager.getInstance().getSession4RoleId(roleId);
					if (outSession != null) {
						outSession.sendMsg(AmfUtil.convertMsg2Bytes(realCmd, realData));
					}
					return;
				}
				
				// 如果指令无需源服务器发送给多个跨服玩家
				if (KuafuCmdUtil.isSendToManyCmd(cmd)) {
					Object[] datas = (Object[]) data;
					Short realCmd = (Short) datas[0];
					Object realData = datas[1];
					if (datas.length > 2) {// 发送给多个玩家
						Object[] roleIds = (Object[]) datas[2];
						for (Object object : roleIds) {
							Long roleId = LongUtils.obj2long(object);
							GameSession outSession = GameSessionManager.getInstance().getSession4RoleId(roleId);
							if (outSession != null) {
								outSession.sendMsg(AmfUtil.convertMsg2Bytes(realCmd,realData));
							}
						}
					} else {// 发送给全部跨服玩家
						for (Long roleId : KuafuManager.getAllRoleIds()) {
							GameSession outSession = GameSessionManager.getInstance().getSession4RoleId(roleId);
							if (outSession != null) {
								outSession.sendMsg(AmfUtil.convertMsg2Bytes(realCmd,realData));
							}
						}
					}
					return;
				}
			}
			// 是否为跨服服务器分发出来的需要源服务器处理的指令
			if (!KuafuCmdUtil.isSourceHandleCmd(cmd)) {

				GameSession outSession = GameSessionManager.getInstance().getSession4RoleId(roleOutId);
				if (outSession == null) {
					return;
				}
				outSession.sendMsg(AmfUtil.convertMsg2Bytes(cmd, data));
				return;
			}

			// 如果是结束的指令，则设置为跨服离线
			// if( KuafuCmdUtil.isOverCmd(cmd) ){
			//	 KuafuManager.removeKuafu(roleid);
			// }

			int destType = CmdGroupInfo.getCmdDest(cmd);

			Object[] innerMsg = new Object[] { cmd, data, destType,
					CmdGroupInfo.node_from_type_kuafu_server,
					CmdGroupInfo.broadcast_type_1, 0, null, roleOutId,// roleId
					null, 1, null, null };

			NodeSwap.swap(innerMsg);
		} catch (Exception e) {
			ChuanQiLog.error("channelRead0 failed: " + e.getMessage(), e);
		}
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		ChuanQiLog.error(e.getMessage(), e);
	}
}