package com.junyou.messageswap;

import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_bus;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_bus_init;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_client;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_inout;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_kuafu_server;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_public;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_stage;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_stage_control;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.getCmdDest;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.node_from_type_client;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.node_from_type_gs;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.node_from_type_kuafu_server;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.node_from_type_kuafu_source;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.node_from_type_public;

import com.junyou.constants.GameConstants;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.KuafuCmdUtil;

/**
 * @description 负责节点间消息分发 
 * @author hehj
 * @date 2012-4-6 上午9:52:08 
 */
public class NodeSwap {

	public static final String CLIENT_TUNNEL = "client_tunnel";
	public static final String PUBLIC_TUNNEL = "public_bus_tunnel";
	public static final String GS_TUNNEL = "gs1";
	
//	private static boolean isSingleGs = true;
//	private static ConcurrentMap<String, ISwapTunnel> gsTunnelMap = new ConcurrentHashMap<String, ISwapTunnel>();
	private static ISwapTunnel clientTunnel;
	private static ISwapTunnel publicTunnel;
	private static ISwapTunnel singleGsTunnel;
	
//	private static ConcurrentMap<Long, String> nodeInfo = new ConcurrentHashMap<>();
	
//	public static void setSingleGs(boolean isSingleGs) {
//		NodeSwap.isSingleGs = isSingleGs;
//	}

	public static void registGsTunnel(ISwapTunnel tunnel){
		singleGsTunnel = tunnel;
		
//		synchronized (gsTunnelMap) {
//			if(isSingleGs){
//				if(null == singleGsTunnel){
//					singleGsTunnel = tunnel;
//				}else{
//					throw new RuntimeException("gs node can't be set more than once when is single gs.");
//				}
//			}else{
//				gsTunnelMap.put(GS_TUNNEL, tunnel);
//			}
//		}
	}

	public static void registClientTunnel(ISwapTunnel tunnel){
		clientTunnel = tunnel;
	}

	public static void registPublicTunnel(ISwapTunnel tunnel){
		publicTunnel = tunnel;
	}
	
	/**
	 * 分发消息到合适的节点
	 * @param msg
	 */
	public static void swap(Object[] msg){
		
		try{
			
			int fromType = MsgUtil.getFromType(msg);
			switch (fromType) {
			case node_from_type_client:
				clientMsgSwap(msg);
				break;
				
			case node_from_type_public:
				
				publicMsgSwap(msg);
				break;
				
			case node_from_type_gs:
				
				gsMsgSwap(msg);
				break;
				
			case node_from_type_kuafu_source:
				clientMsgSwap(msg);
				
				break;
			case node_from_type_kuafu_server:
				
				gsMsgSwap(msg);
				break;
			}

		}catch (Exception e) {
			ChuanQiLog.error("exception when swap "+MsgUtil.getCommand(msg),e);
		}
		
		
	}
	
	/**
	 * 分发来自客户端的消息
	 * @param msg
	 */
	public static void clientMsgSwap(Object[] msg){
		
		Short cmd = MsgUtil.getCommand(msg);
		
		//如果是跨服禁止的指令
		if( KuafuCmdUtil.isForbidCmds(cmd, msg) ){
			return;
		}
		
		if( KuafuCmdUtil.swapCmdNow(cmd, msg) ){
			swap2Kuafu(cmd, msg);
			return;
		}
		
		//指令未注册
		Integer tmpDest = getCmdDest(cmd);
		if( tmpDest == null ){
			ChuanQiLog.error("no dest node for client1 cmd:{"+cmd+"}"+msg[7]);
			return;
		}
		
		int dest = getCmdDest(cmd);
		msg[2] = dest;
		switch (dest) {
		case dest_type_public: // public
			
			swap2public(msg);

			break;
		case dest_type_inout: // in-out
			
			swap2public(msg);

			break;
		case dest_type_bus_init: // bus-init
			
			swap2public(msg);

			break;
		case dest_type_bus: // bus
			
			swap2gs(msg);
			
			break;
		
		case dest_type_stage: // stage
			
			swap2gs(msg);
			
			break;
		case dest_type_stage_control: // stage-control
			
			swap2gs(msg);
			
			break;
		case dest_type_kuafu_server: // kuafu server
			swap2gs(msg);
			break;
		default:
			ChuanQiLog.error("no dest node for client2 cmd:"+cmd);
			break;
		}
	}

	/**
	 * 分发来自游戏服务器节点的消息
	 */
	public static void gsMsgSwap(Object[] msg){
		componentMsgSwap(msg);
	}
	
	/**
	 * 分发来自公共服务节点的消息 
	 */
	public static void publicMsgSwap(Object[] msg){
		componentMsgSwap(msg);
	}
	
	private static void componentMsgSwap(Object[] msg){
		
		int destType = MsgUtil.getDestType(msg);
		switch (destType) {
		case dest_type_client:
			swap2client(msg);
			break;
		case dest_type_bus_init:
			swap2gs(msg);
			break;
		case dest_type_bus:
			swap2gs(msg);
			break;
		case dest_type_public: // public
			swap2public(msg);
			break;
		case dest_type_stage_control:
			swap2gs(msg);
			break;
		case dest_type_stage:
			swap2gs(msg);
			break;
		case dest_type_kuafu_server:
			swap2gs(msg);
			break;
		default:
			ChuanQiLog.error("no dest for component cmd:"+MsgUtil.getCommand(msg));
		}

	}
	
	/**
	 * 分发消息到公共服务节点
	 * @param msg
	 */
	private static void swap2public(Object[] msg){
		publicTunnel.receive(msg);
	}
	
	/**
	 * 分发消息到游戏服务器节点
	 * @param msg
	 */
	private static void swap2gs(Object[] msg){
		Short cmd = MsgUtil.getCommand(msg);
		
		// 是否为直接转发的指令
		if( KuafuCmdUtil.swapCmdNow(cmd, msg) ){
			swap2Kuafu(cmd, msg);
			return;
		}
		
		singleGsTunnel.receive(msg);
	}
	
	/**
	 * 分发消息到跨服服务器
	 * @param msg
	 */
	public static void swap2Kuafu(Short cmd, Object[] msg){
//		if(LOG.isDebugEnabled()){
//			LOG.debug("kuafu-"+JSON.toJSONString(msg));
//		}
		
		KuafuNetTunnel tunnel = null;
		boolean returned = false;
		try{
			Long userRoleId = MsgUtil.getRoleId(msg);
			if(userRoleId.longValue()==GameConstants.DEFAULT_ROLE_ID.longValue()){
				ChuanQiLog.error("DEFAULT_ROLE_ID cmd = {}",cmd);
				return;
			}
			KuafuServerInfo serverInfo = null;
			if(userRoleId.longValue()!=GameConstants.DEFAULT_ROLE_ID.longValue()){
				serverInfo = KuafuRoleServerManager.getInstance().getBindServer(userRoleId);
			}
			tunnel = KuafuConfigUtil.getConnection(serverInfo);
			if(tunnel!=null && tunnel.isConnected()){
				tunnel.receive(new Object[]{ cmd,userRoleId, MsgUtil.getMsgData(msg)});
			}else{
				KuafuConfigUtil.returnBrokenConnection(tunnel);
				returned = true;
				ChuanQiLog.error("realUserRoleId ={} send2KuafuServer fail serverId={} cmd={}",userRoleId,serverInfo==null?"-":serverInfo.getServerId(),cmd);
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
	 * 分发消息到客户端
	 * @param msg
	 */
	private static void swap2client(Object[] msg){
		clientTunnel.receive(msg);
	}

}
