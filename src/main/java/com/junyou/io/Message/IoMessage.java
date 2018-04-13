package com.junyou.io.Message;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.cmd.InnerCmdType;
import com.kernel.pool.executor.Message;

public class IoMessage extends Message{

	public IoMessage(Object[] msg) {
		super(msg);
	}

	@Override
	public Short getCommand() {

		Short cmd = super.getCommand();
		if(CmdGroupInfo.isChatModule(cmd) || CmdGroupInfo.isPingModule(cmd) || CmdGroupInfo.isTsServerModule(cmd)){
			return cmd;
		}
		
		return InnerCmdType.IO_MSG_DISPATHCE_CMD;
	}
}
