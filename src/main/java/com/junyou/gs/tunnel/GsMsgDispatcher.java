package com.junyou.gs.tunnel;

import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_bus;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_bus_init;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_client;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_kuafu_server;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_stage;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.dest_type_stage_control;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.from_type_client;
import static com.hehj.easyexecutor.cmd.CmdGroupInfo.getCmdDest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.JSONArray;
import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.bus.tunnel.BusMsgDispatcher;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.messageswap.MsgUtil;
import com.junyou.messageswap.NodeSwap;
import com.junyou.stage.tunnel.StageMsgDispatcher;
import com.junyou.utils.KuafuCmdUtil;

/**
 * @description 客户端接入消息分发器
 * @author hehj
 * 2011-11-4 下午3:04:30
 */
public class GsMsgDispatcher {

	private BusMsgDispatcher busMsgDispatcher;
	
	private StageMsgDispatcher stageMsgDispatcher;
	
	private GsMsgSwap gsMsgSwap;
	
	public void setGsMsgSwap(GsMsgSwap gsMsgSwap){
		this.gsMsgSwap = gsMsgSwap;
	}
	
	public void setBusMsgDispatcher(BusMsgDispatcher busMsgDispatcher) {
		this.busMsgDispatcher = busMsgDispatcher;
	}

	public void setStageMsgDispatcher(StageMsgDispatcher stageMsgDispatcher) {
		this.stageMsgDispatcher = stageMsgDispatcher;
	}

	public void in(Object message) {
		
		Object[] msg = (Object[])message;

		int fromType = MsgUtil.getFromType(msg);
		
		switch (fromType) {
		case from_type_client: // 客户端源
			clientMsgSwap(msg);
			break;
		default : // 组件源
			componentMsgSwap(msg);
			break;
		}
	
	}

	/**
	 * 处理来自客户端消息的分发
	 * @param msg
	 */
	private void clientMsgSwap(Object[] msg){

		Short cmd = MsgUtil.getCommand(msg);
		
		int dest = getCmdDest(cmd);
		msg[2] = dest;
		switch (dest) {
		case dest_type_bus: // bus

			busMsgDispatcher.in(msg);
			
			break;
		
		case dest_type_stage: // stage
			
			_swap2stage(msg);
			
			break;
		case dest_type_stage_control: // stage-control
			
			busMsgDispatcher.in(msg);
			
			break;
			
		case dest_type_kuafu_server:
			
			busMsgDispatcher.in(msg);
			
			break;
		default:
			ChuanQiLog.errorFrame("no dest for client cmd:"+cmd);
			break;
		}
		
	}

	/**
	 * 处理来自业务组件的消息分发
	 * @param msg
	 */
	private void componentMsgSwap(Object[] msg){

		int destType = MsgUtil.getDestType(msg);
		switch (destType) {
		case dest_type_client:
			
			swap2client(msg);
			break;
			
		case dest_type_bus_init:
			
			busMsgDispatcher.in(msg);
			break;
			
		case dest_type_bus:
			
			busMsgDispatcher.in(msg);
			break;
			
		case dest_type_stage_control:
			
			busMsgDispatcher.in(msg);
			break;
			
		case dest_type_stage:
			
			_swap2stage(msg);
			
			break;
			
		case dest_type_kuafu_server:
			
			busMsgDispatcher.in(msg);
			
			break;
			
		default:
			ChuanQiLog.errorFrame("no dest for component cmd:"+MsgUtil.getCommand(msg));
		}
		
		
	}
	
	

	private ConcurrentMap<Long, String> roleStageInfos = new ConcurrentHashMap<Long, String>();
	private void _swap2stage(Object[] msg){
		
		// 填充stageid
		Short cmd = MsgUtil.getCommand(msg);
		Long roleid = MsgUtil.getRoleId(msg);
		
		// 是否为直接转发的指令
		if( KuafuCmdUtil.swapCmdNow(cmd, msg) ){
			NodeSwap.swap2Kuafu(cmd, msg);
			return;
		}
		
		synchronized (getLock(roleid)) {

			if(InnerCmdType.S_Enter_Stage_cmd == cmd){
				Object[] data = (Object[]) MsgUtil.getMsgData(msg);
				roleStageInfos.put(roleid, (String) data[0]);
			}else if(InnerCmdType.S_LEAVE_AFTER_ENTER_CMD == cmd){
				Object[] data = (Object[]) MsgUtil.getMsgData(msg);
				roleStageInfos.put(roleid, (String) data[1]);
			}
			
			String stageid = null;
			if(roleid > 0){
				stageid = roleStageInfos.get(roleid);
				if(null != stageid){
					msg[8] = stageid;
				}else{
					ChuanQiLog.error("when in,role is not in stage: "+JSONArray.toJSONString(msg));
					return;
				}
			}else{
				//给系统用户赋值场景id，此场景id不存在对应场景，业务中自行处理
				stageid = GameConstants.DEFAULT_ROLE_STAGEID;
			}
			
			if(InnerCmdType.S_Leave_Stage_cmd == cmd){
				swap2client(new Object[]{InnerCmdType.leaveStage_cmd,null,CmdGroupInfo.dest_type_client,CmdGroupInfo.node_from_type_gs,CmdGroupInfo.broadcast_type_1,null,null,roleid,stageid,1});
				roleStageInfos.remove(roleid);
			}
			
		}
		
		stageMsgDispatcher.in(msg);
	}
	
	private void swap2client(Object[] msg){
		gsMsgSwap.swap(msg);
	}
	
	private static ConcurrentMap<Long, Object> roleLocks = new ConcurrentHashMap<Long, Object>();
	private static Object getLock(Long roleid){
		
		Object lock = roleLocks.get(roleid);
		if(null == lock){
			synchronized (roleLocks) {
				lock = roleLocks.get(roleid);
				if(null == lock){
					lock = new Object();
					roleLocks.put(roleid, lock);
				}
			}
		}
		
		return lock;
	}

}
