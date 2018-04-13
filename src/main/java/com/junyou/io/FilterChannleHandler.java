package com.junyou.io;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.alibaba.fastjson.JSONObject;
import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.io.global.GameSessionManager;
import com.junyou.io.modelfilter.MsgFilterManage;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.codec.AmfUtil;

public class FilterChannleHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		Object[] tmps = (Object[])msg;
		short cmd = (Short) tmps[0];
		
		if(cmd < 0){
			return;
		}
		
		//ChuanQiLog.debug("=============="+JSONObject.toJSONString(tmps));
		GameSession session = GameSessionManager.getInstance().getSession(ctx.channel());
		if(session != null){
			//判断指令和模块是否关闭
			boolean flag = filterModelClose(session, cmd);
			if(!flag){
				super.channelRead(ctx, msg);
			}
		}
	}


	/**
	 * 检查模块是否关闭
	 * @return true:关闭
	 */
	public boolean filterModelClose(GameSession session,short cmd){
		//主端口消息入口是否打开
		if(!MsgFilterManage.isOpenMsg()){
			return true;
		}
		
		//指令开关判断
		if(MsgFilterManage.checkIsClose(cmd)){
			return true;
		}
		
		String modName = CmdGroupInfo.getCmdModule(cmd);
		
		boolean flag = MsgFilterManage.checkIsClose(modName);
		if(flag){
			Integer moduleId = MsgFilterManage.getCloseModIdByModName(modName);
			
			Object data = AppErrorCode.createError(AppErrorCode.MODEL_CLOSE_CHECK, moduleId);
			byte[] msgBytes = AmfUtil.convertMsg2Bytes(ClientCmdType.NOTIFY_CLIENT_ALERT,data);
			if(session.getChannel().isActive()){
				session.sendMsg(msgBytes);
				flag = true;
			}
		}
		return flag;
	} 
}
