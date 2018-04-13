package com.junyou.io.swap;

import com.junyou.messageswap.ISwapTunnel;

public class IoMsgTunnel implements ISwapTunnel {

	private IoMsgDispatcher ioMsgDispatcher;
	
	public void setIoMsgDispatcher(IoMsgDispatcher ioMsgDispatcher) {
		this.ioMsgDispatcher = ioMsgDispatcher;
	}

	@Override
	public void receive(Object msg) {
		ioMsgDispatcher.in(msg);
	}

}
