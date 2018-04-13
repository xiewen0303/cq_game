package com.junyou.public_.tunnel;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.constants.GameConstants;
import com.junyou.messageswap.NodeSwap;
import com.junyou.utils.MsgPrintUtil;

public class PublicMsgSender {
	
	private static PublicMsgDispatcher publicMsgDispatcher;
	
	private static IPublicSwap publicSwap;
	
	private static PublicMsgTunnel publicMsgTunnel;
	
	public void setPublicMsgTunnel(PublicMsgTunnel publicMsgTunnel) {
		PublicMsgSender.publicMsgTunnel = publicMsgTunnel;
	}
	
	public PublicMsgTunnel getPublicMsgTunnel(){
		return publicMsgTunnel;
	}
	
	public void setPublicMsgDispatcher(
			PublicMsgDispatcher publicMsgDispatcher) {
		PublicMsgSender.publicMsgDispatcher = publicMsgDispatcher;
	}

	public void init(){
		PublicMsgSender.publicSwap = new IPublicSwap() {
			@Override
			public void swap(Object[] msg) {
				NodeSwap.swap(msg);
			}
		};
	}

	/**
	 * 发送给单个客户端
	 * @param userid
	 * @param command
	 * @param result
	 */
	public static void send2OneBySessionId(String sessionid,String userId, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_client,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_1,sessionid,null,userId,null,0};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
		publicSwap.swap(msg);
	}
	
	/**
	 * 发送给单个客户端
	 * @param roleid
	 * @param command
	 * @param result
	 */
	public static void send2One(Long roleid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_client,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_1,null,null,roleid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
		publicSwap.swap(msg);
	}
	
	/**
	 * 发送给所有客户端
	 * @param command
	 * @param data
	 */
	public static void send2All(Short command, Object data){
		Object[] msg = new Object[]{command,data,CmdGroupInfo.dest_type_client,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_3,null,null,GameConstants.SEND_ALL_GUID,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
		publicSwap.swap(msg);
	}
	
	/**
	 * 发送给多个客户端
	 * @param userRoleIds
	 * @param command
	 * @param data
	 */
	public static void send2Many(Long[] roleIds, Short command,Object result){
		if(roleIds != null && roleIds.length > 0){
			Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_client,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_2,null,null,roleIds,null,1};
			MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
			publicSwap.swap(msg);
		}
	}

	/**
	 * 发送给场景组件的单个目标
	 * @param roleid
	 * @param command
	 * @param result
	 */
	public static void send2Stage(Long roleid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_stage,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_1,null,null,roleid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
		publicSwap.swap(msg);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
	}

	/**
	 * 发送给场景控制组件的单个目标
	 * @param roleid
	 * @param command
	 * @param result
	 */
	public static void send2GsStageControl(Long roleid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_stage_control,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_1,null,null,roleid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
		publicSwap.swap(msg);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
	}

	/**
	 * 发送给场景业务组件的单个目标
	 * @param roleid
	 * @param command
	 * @param result
	 */
	public static void send2GsBus(Long roleid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_bus,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_1,null,null,roleid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
		publicSwap.swap(msg);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
	}
	
	/**
	 * public内部指令分发
	 * @param command
	 * @param roleid
	 * @param result
	 */
	public static void send2PublicInner(Short command,Long roleid,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_public,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_1,null,null,roleid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.PUBLIC_PRINT,MsgPrintUtil.PUBLIC_PREFIX);
		publicMsgDispatcher.in(msg);
	}
}
