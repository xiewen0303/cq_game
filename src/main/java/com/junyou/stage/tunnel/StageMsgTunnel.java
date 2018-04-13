package com.junyou.stage.tunnel;

import com.junyou.messageswap.ISwapTunnel;

public class StageMsgTunnel implements ISwapTunnel {

	private StageMsgDispatcher stageMsgDispatcher;
	
	public void setStageMsgDispatcher(StageMsgDispatcher stageMsgDispatcher) {
		this.stageMsgDispatcher = stageMsgDispatcher;
	}

	@Override
	public void receive(Object msg) {
		stageMsgDispatcher.in(msg);
	}

}
