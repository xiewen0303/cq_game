package com.junyou.stage.tunnel;

import static com.junyou.stage.share.StageidThreadShare.getStageId;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.bus.tunnel.BusMsgDispatcher;
import com.junyou.constants.GameConstants;
import com.junyou.gs.tunnel.GsMsgSwap;
import com.junyou.utils.MsgPrintUtil;

public class StageMsgSender {

	private static GsMsgSwap gsMsgSender;

	private static StageMsgDispatcher stageMsgDispatcher;
	private static BusMsgDispatcher busMsgDispatcher;
	
	public void setGsMsgSender(GsMsgSwap gsMsgSender) {
		StageMsgSender.gsMsgSender = gsMsgSender;
	}

	public void setStageMsgDispatcher(StageMsgDispatcher stageMsgDispatcher) {
		StageMsgSender.stageMsgDispatcher = stageMsgDispatcher;
	}

	public void setBusMsgDispatcher(BusMsgDispatcher busMsgDispatcher) {
		StageMsgSender.busMsgDispatcher = busMsgDispatcher;
	}

	/**
	 * 发送给单个客户端
	 * @param userid
	 * @param command
	 * @param result
	 */
	public static void send2One(Long userid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_client,CmdGroupInfo.from_type_stage,CmdGroupInfo.broadcast_type_1,null,null,userid,getStageId(),1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX);
		gsMsgSender.swap(msg);
	}
	
	/**
	 * 发送给所有客户端
	 * @param command
	 * @param data
	 */
	public static void send2All(Short command, Object data){
		Object[] msg = new Object[]{command,data,CmdGroupInfo.dest_type_client,CmdGroupInfo.from_type_stage,CmdGroupInfo.broadcast_type_3,null,null,GameConstants.SEND_ALL_GUID,getStageId(),1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX);
		gsMsgSender.swap(msg);
	}
	
	/**
	 * 发送给多个客户端
	 * @param userRoleIds
	 * @param command
	 * @param data
	 */
	public static void send2Many(Object[] userRoleIds, Short command,Object data){
		if(command == null){
			throw new RuntimeException("stage cmd == null");
		}
		Object[] msg = new Object[]{command,data,CmdGroupInfo.dest_type_client,CmdGroupInfo.from_type_stage,CmdGroupInfo.broadcast_type_2,null,null,userRoleIds,getStageId(),1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX);
		gsMsgSender.swap(msg);
	}
	
	/**
	 * 发送给业务处理组件的单个目标
	 * @param userid
	 * @param command
	 * @param result
	 */
	public static void send2Bus(Long userid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_bus,CmdGroupInfo.from_type_stage,CmdGroupInfo.broadcast_type_1,null,null,userid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX);
		busMsgDispatcher.in(msg);
	}

	/**
	 * 发送给公共组件的单个目标
	 * @param userid
	 * @param command
	 * @param result
	 */
	public static void send2Public(Long userid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_public,CmdGroupInfo.node_from_type_gs,CmdGroupInfo.broadcast_type_1,null,null,userid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX);
		gsMsgSender.swap(msg);
	}
	
	/**
	 * 发送给场景组件的单个目标
	 * @param userid
	 * @param command
	 * @param result
	 */
	public static void send2Stage(Long userid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_stage,CmdGroupInfo.from_type_stage,CmdGroupInfo.broadcast_type_1,null,null,userid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX);
		stageMsgDispatcher.in(msg);
	}

	/**
	 * 发送给场景控制组件的单个目标
	 * @param userid
	 * @param command
	 * @param result
	 */
	public static void send2StageControl(Long userid, Short command,Object result){
		Object[] msg = new Object[]{command,result,CmdGroupInfo.dest_type_stage_control,CmdGroupInfo.from_type_stage,CmdGroupInfo.broadcast_type_1,null,null,userid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.STAGE_PRINT,MsgPrintUtil.STAGE_PREFIX);
		busMsgDispatcher.in(msg);
	}

	/**
	 * bus组件内部消息发送
	 * @param userid
	 * @param stageId
	 * @param command
	 * @param result
	 */
	public static void send2StageInner(Long userid,String stageId, Short command,Object result){
		stageMsgDispatcher.in(new Object[]{command,result,CmdGroupInfo.dest_type_stage,CmdGroupInfo.from_type_stage,null,null,null,userid,stageId,3});
	}
	
	/**
	 * bus组件内部消息发送(带token值)
	 * @param userid
	 * @param command
	 * @param result
	 * @param token [identity,token]
	 */
	public static void send2StageInnerToken(Long userid,String stageId, Short command,Object result,Object[] token){
		stageMsgDispatcher.in(new Object[]{command,result,CmdGroupInfo.dest_type_stage,CmdGroupInfo.from_type_stage,null,null,null,userid,stageId,3,token});
	}

}
