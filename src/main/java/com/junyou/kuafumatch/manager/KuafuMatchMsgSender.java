package com.junyou.kuafumatch.manager;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.MsgPrintUtil;
import com.kernel.utils.SerializableHelper;

public class KuafuMatchMsgSender {

	/**
	 * 发送到跨服服务器
	 * 
	 * @param userRoleId
	 * @param command
	 * @param result
	 */
	public static void send2KuafuMatchServer(Long userRoleId, Short command,
			Object result) {
		Object[] msg = new Object[] { command, result,
				CmdGroupInfo.dest_type_kuafu_server,
				CmdGroupInfo.node_from_type_kuafu_source,
				CmdGroupInfo.broadcast_type_1, null, null, userRoleId, null, 1 };
		if (MsgPrintUtil.needPrint()) {
			MsgPrintUtil.printOutMsg(msg, MsgPrintUtil.KUAFU_PRINT,
					MsgPrintUtil.KUAFU_PREFIX);
		}
		KuafuNetTunnel tunnel = null;
		boolean returned = false;
		try {
			tunnel = KuafuMatchServerManager.getKuafuMatchConnection();
			if (tunnel != null) {
				if(tunnel.isConnected()){
					tunnel.receive(new Object[] { command, userRoleId, result });
				}else{
					ChuanQiLog.error("send 2 kuafu match server fail tunnel not connect userRoleId={},command={}",userRoleId, command);
				}
			} else {
				ChuanQiLog
						.error("send 2 kuafu match server fail tunnel is null userRoleId={},command={}",
								userRoleId, command);
			}
		} catch (Exception e) {
			KuafuMatchServerManager.returnKuafuMatchBrokenConnection(tunnel);
			returned = true;
			ChuanQiLog.error(
					"send 2 kuafu match server error userRoleId={},command={}",
					userRoleId, command);
			ChuanQiLog.error("", e);
		} finally {
			if (!returned) {
				KuafuMatchServerManager.returnKuafuMatchConnection(tunnel);
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @param userRoleId
	 * @param command
	 * @param result
	 */
	public static void send2KuafuCompetitionMatchServer(Long userRoleId, Short command,
			Object result) {
		Object[] msg = new Object[] { command, result,
				CmdGroupInfo.dest_type_kuafu_server,
				CmdGroupInfo.node_from_type_kuafu_source,
				CmdGroupInfo.broadcast_type_1, null, null, userRoleId, null, 1 };
		if (MsgPrintUtil.needPrint()) {
			MsgPrintUtil.printOutMsg(msg, MsgPrintUtil.KUAFU_PRINT,
					MsgPrintUtil.KUAFU_PREFIX);
		}
		KuafuNetTunnel tunnel = null;
		boolean returned = false;
		try {
			tunnel = KuafuCompetitionMatchServerManager.getKuafuMatchConnection();
			if (tunnel != null) {
				if(tunnel.isConnected()){
					tunnel.receive(new Object[] { command, userRoleId, result });
				}else{
					ChuanQiLog
					.error("send 2 kuafu competition match server fail tunnel not connect userRoleId={},command={}",
							userRoleId, command);
				}
			} else {
				ChuanQiLog
						.error("send 2 kuafu competition match server fail tunnel is null userRoleId={},command={}",
								userRoleId, command);
			}
		} catch (Exception e) {
			KuafuCompetitionMatchServerManager.returnKuafuMatchBrokenConnection(tunnel);
			returned = true;
			ChuanQiLog.error(
					"send 2 kuafu competition match server error userRoleId={},command={}",
					userRoleId, command);
			ChuanQiLog.error("", e);
		} finally {
			if (!returned) {
				KuafuCompetitionMatchServerManager.returnKuafuMatchConnection(tunnel);
			}
		}
	}

	/**
	 * 发送到角色所在的源服务器
	 * 
	 * @param userRoleId
	 * @param command
	 * @param result
	 * @param directSwap
	 *            是否到跨服源之后直接转发
	 */
	public static void send2SourceServer(Long userRoleId, Short command,
			Object result) {
		String serverId = KuafuSessionManager.getServerId(userRoleId);
		if (serverId != null) {
			KuafuSessionManager.writeMsg(
					serverId,
					SerializableHelper.serialize(new Object[] { command,
							userRoleId, result }));
		} else {
			ChuanQiLog.error("kuafu serverId is null, cmd is {" + command
					+ "} roleId:{" + userRoleId + "}");
		}
	}

}
