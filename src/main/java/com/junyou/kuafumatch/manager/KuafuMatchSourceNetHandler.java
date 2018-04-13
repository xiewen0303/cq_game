package com.junyou.kuafumatch.manager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.log.ChuanQiLog;
import com.junyou.messageswap.NodeSwap;

public class KuafuMatchSourceNetHandler extends SimpleChannelInboundHandler<Object[]> {

	private KuafuNetTunnel kuafuNetTunnel;
	
	public KuafuMatchSourceNetHandler(KuafuNetTunnel kuafuNetTunnel) {
		super();
		ChuanQiLog.info("KuafuMatchSourceNetHandler 已创建");
		this.kuafuNetTunnel = kuafuNetTunnel;
	}

	/** 断开连接 原先的channelClosed */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ChuanQiLog.info("kuafu match {} channelClosed", ctx.channel());
		if(kuafuNetTunnel!=null && !kuafuNetTunnel.isConnected()){
			if(kuafuNetTunnel.getServerInfo().isCompetitionMatchServer()){
				KuafuCompetitionMatchServerManager.returnKuafuMatchBrokenConnection(kuafuNetTunnel);
			}else{
				KuafuMatchServerManager.returnKuafuMatchBrokenConnection(kuafuNetTunnel);
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
			
			Integer destType = CmdGroupInfo.getCmdDest(cmd);
			if(destType==null){
				ChuanQiLog.error("destType is null cmd={}",cmd);
				return ;
			}

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