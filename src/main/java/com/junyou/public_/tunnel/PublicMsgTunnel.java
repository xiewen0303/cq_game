package com.junyou.public_.tunnel;

import com.junyou.messageswap.ISwapTunnel;

public class PublicMsgTunnel implements ISwapTunnel {

	private PublicMsgDispatcher publicMsgDispatcher;
	
	public void setPublicMsgDispatcher(PublicMsgDispatcher publicMsgDispatcher) {
		this.publicMsgDispatcher = publicMsgDispatcher;
	}

	@Override
	public void receive(Object msg) {
		publicMsgDispatcher.in(msg);
	}

}
