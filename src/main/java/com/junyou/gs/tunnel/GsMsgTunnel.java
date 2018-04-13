package com.junyou.gs.tunnel;

import com.junyou.messageswap.ISwapTunnel;

public class GsMsgTunnel implements ISwapTunnel {

	private GsMsgDispatcher gsMsgDispatcher;

	public void setGsMsgSwap(GsMsgSwap gsMsgSwap){
		this.gsMsgDispatcher.setGsMsgSwap(gsMsgSwap);
	}
	
	public void setGsMsgDispatcher(GsMsgDispatcher gsMsgDispatcher) {
		this.gsMsgDispatcher = gsMsgDispatcher;
	}

	@Override
	public void receive(Object msg) {
		gsMsgDispatcher.in(msg);
	}

}
