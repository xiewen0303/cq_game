package com.junyou.kuafu.share.tunnel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gs.tunnel.GsMsgSwap;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.MsgPrintUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.number.LongUtils;
import com.kernel.utils.SerializableHelper;

@Component
public class KuafuMsgSender {
	
	private static GsMsgSwap gsMsgSender;
	
	@Autowired
	public void setGsMsgSender(GsMsgSwap gsMsgSender) {
		KuafuMsgSender.gsMsgSender = gsMsgSender;
	}
	
	/**
	 * 发送到跨服服务器
	 * @param userRoleId
	 * @param command
	 * @param result
	 */
	public static void send2KuafuServer(Long userRoleId,Long realUserRoleId, Short command, Object result){
		if(realUserRoleId.longValue()==GameConstants.DEFAULT_ROLE_ID.longValue()){
			ChuanQiLog.error("DEFAULT_ROLE_ID command = {}",command);
			return;
		}
		Object[] msg = new Object[]{command, result, CmdGroupInfo.dest_type_kuafu_server, CmdGroupInfo.node_from_type_kuafu_source, CmdGroupInfo.broadcast_type_1,null,null,userRoleId,null,1};
		if( MsgPrintUtil.needPrint() ){
			MsgPrintUtil.printOutMsg(msg, MsgPrintUtil.KUAFU_PRINT, MsgPrintUtil.KUAFU_PREFIX);
		}
		KuafuNetTunnel tunnel = null;
		boolean returned = false;
		try{
			KuafuServerInfo serverInfo = KuafuRoleServerManager.getInstance().getBindServer(realUserRoleId);
			tunnel = KuafuConfigUtil.getConnection(serverInfo);
			if(tunnel!=null&&tunnel.isConnected()){
				tunnel.receive(new Object[]{command, userRoleId, result});
			}else{
				KuafuConfigUtil.returnBrokenConnection(tunnel);
				returned = true;
				ChuanQiLog.error("realUserRoleId ={} send2KuafuServer fail serverId={} command={}",realUserRoleId,serverInfo==null?"-":serverInfo.getServerId(),command);
			}
		}catch (Exception e) {
			KuafuConfigUtil.returnBrokenConnection(tunnel);
			returned = true;
			ChuanQiLog.error("", e);
		}finally{
			if(!returned){
				KuafuConfigUtil.returnConnection(tunnel);
			}
		}
	}
	
	/**
	 * 发送到角色所在的源服务器
	 * @param userRoleId
	 * @param command
	 * @param result
	 * @param directSwap 是否到跨服源之后直接转发
	 */
	public static void send2KuafuSource(Long userRoleId, Short command, Object result){
		String serverId = KuafuSessionManager.getServerId(userRoleId);
		if(serverId!=null){
			KuafuSessionManager.writeMsg(serverId, SerializableHelper.serialize(new Object[]{command, userRoleId, result}));
		}else{
			ChuanQiLog.error("send kuafu-source fail, serverId is null, cmd is {"+command+"} roleId:{"+userRoleId+"}");
		}
	}
	
	/**
	 * 发送给单个源服务器
	 * @param command
	 * @param result
	 */
	public static void send2KuafuSource(String serverId,Short command, Object result){
		if(serverId==null){
			ChuanQiLog.error("kuafu serverId is null, cmd is {"+command+"}");
			return;
		}
		KuafuSessionManager.writeMsg(serverId, SerializableHelper.serialize(new Object[]{command, GameConstants.DEFAULT_ROLE_ID, result}));
	}
	

	/**
	 * 发送给单个客户端
	 * @param userRoleId
	 * @param command
	 * @param result
	 */
	public static void send2One(Long userRoleId, Short command, Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_client,CmdGroupInfo.from_type_bus,CmdGroupInfo.broadcast_type_1,null,null,userRoleId,null,1};
		if(MsgPrintUtil.needPrint()){
			MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.BUS_PRINT,MsgPrintUtil.BUS_PREFIX);
		}
		gsMsgSender.swap(msg);
	}
	
	/**
	 * 发送给单个源服务器
	 * @param command
	 * @param result
	 */
	public static void send2OneKuafuSource(String serverId,Short command, Object result){
		KuafuSessionManager.writeMsg(serverId, SerializableHelper.serialize(new Object[]{command, GameConstants.DEFAULT_ROLE_ID, result}));
	}
	/**
	 * 发送给所有源服务器
	 * @param command
	 * @param result
	 */
	public static void send2AllKuafuSource(Short command, Object result){
		byte[] msg = SerializableHelper.serialize(new Object[]{command, GameConstants.DEFAULT_ROLE_ID, result});
		KuafuSessionManager.writeMsgToAllServer(msg);
	}
	
	/**
	 * 发送给所有客户端
	 * @param userRoleId
	 * @param command
	 * @param result
	 */
	public static void send2All(Short command, Object result){
		Object[] res = new Object[]{command,result};
		send2AllKuafuSource(InnerCmdType.INNER_KF_TO_MANY_CLIENT, res);
	}
	/**
	 * 发送给多个客户端
	 * @param userRoleId
	 * @param command
	 * @param result
	 */
	public static void send2Many(Long[] userRoleIds, Short command, Object result){
		if(userRoleIds == null || userRoleIds.length < 1){
			return;
		}
		Map<String,List<Long>> serverMap = new HashMap<>();
		for (Long userRoleId : userRoleIds) {
			String serverId = KuafuSessionManager.getServerId(userRoleId);
			if(ObjectUtil.strIsEmpty(serverId)){
				ChuanQiLog.error("玩家{}没有找到所属服务器",userRoleId);
				continue;
			}
			List<Long> list = serverMap.get(serverId);
			if(list == null){
				list = new ArrayList<>();
				serverMap.put(serverId, list);
			}
			list.add(userRoleId);
		}
		for (Map.Entry<String,List<Long>> entry : serverMap.entrySet()) {
			List<Long> list = entry.getValue();
			if(list.size() > 1){
				KuafuSessionManager.writeMsg(entry.getKey(), SerializableHelper.serialize(new Object[]{InnerCmdType.INNER_KF_TO_MANY_CLIENT, GameConstants.DEFAULT_ROLE_ID, new Object[]{command,result,list.toArray()}}));
			}else{
				KuafuSessionManager.writeMsg(entry.getKey(), SerializableHelper.serialize(new Object[]{command, list.get(0), result}));
			}
		}
		
	}
	/**
	 * 发送给多个客户端
	 * @param userRoleId
	 * @param command
	 * @param result
	 */
	public static void send2Many(Object[] userRoleIds, Short command, Object result){
		if(userRoleIds == null || userRoleIds.length < 1){
			return;
		}
		Map<String,List<Long>> serverMap = new HashMap<>();
		for (Object roleId : userRoleIds) {
			Long userRoleId = LongUtils.obj2long(roleId);
			String serverId = KuafuSessionManager.getServerId(userRoleId);
			if(ObjectUtil.strIsEmpty(serverId)){
				ChuanQiLog.error("玩家{}没有找到所属服务器",userRoleId);
				continue;
			}
			List<Long> list = serverMap.get(serverId);
			if(list == null){
				list = new ArrayList<>();
				serverMap.put(serverId, list);
			}
			list.add(userRoleId);
		}
		for (Map.Entry<String,List<Long>> entry : serverMap.entrySet()) {
			List<Long> list = entry.getValue();
			if(list.size() > 1){
				KuafuSessionManager.writeMsg(entry.getKey(), SerializableHelper.serialize(new Object[]{InnerCmdType.INNER_KF_TO_MANY_CLIENT, GameConstants.DEFAULT_ROLE_ID, new Object[]{command,result,list.toArray()}}));
			}else{
				KuafuSessionManager.writeMsg(entry.getKey(), SerializableHelper.serialize(new Object[]{command, list.get(0), result}));
			}
		}
		
	}
}
