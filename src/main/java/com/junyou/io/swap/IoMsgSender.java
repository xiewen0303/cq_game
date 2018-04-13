package com.junyou.io.swap;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.messageswap.NodeSwap;
import com.junyou.utils.MsgPrintUtil;

public class IoMsgSender {

	private static IoMsgTunnel ioMsgTunnel;
	
	public void setIoMsgTunnel(IoMsgTunnel ioMsgTunnel) {
		IoMsgSender.ioMsgTunnel = ioMsgTunnel;
	}

	public void init(){
	}
	
	public static void swap(Object[] msg){
		Short cmd = (Short) msg[0];
		if(CmdGroupInfo.isChatModule(cmd) || CmdGroupInfo.isPingModule(cmd) || CmdGroupInfo.isTsServerModule(cmd)){
			ioMsgTunnel.receive(msg);
		}else{
			NodeSwap.swap(msg);
		}
	}

	public static void send2IoInner(Long roleid, Short command,Object result) {
		Object[] msg = new Object[]{command,result,0,CmdGroupInfo.node_from_type_public,CmdGroupInfo.broadcast_type_1,null,null,roleid,null,1};
		MsgPrintUtil.printOutMsg(msg,MsgPrintUtil.IO_PRINT,MsgPrintUtil.IO_PREFIX);
		swap(msg);
	}
	
}
