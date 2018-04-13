package com.junyou.bus.tunnel;

import com.junyou.messageswap.ISwapTunnel;

public class BusMsgTunnel implements ISwapTunnel {

	private BusMsgDispatcher busMsgDispatcher;
	
	public void setBusMsgDispatcher(BusMsgDispatcher busMsgDispatcher) {
		this.busMsgDispatcher = busMsgDispatcher;
	}

	@Override
	public void receive(Object msg) {
		busMsgDispatcher.in(msg);
	}

}
