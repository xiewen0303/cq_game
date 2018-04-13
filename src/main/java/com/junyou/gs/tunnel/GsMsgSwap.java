package com.junyou.gs.tunnel;

import com.junyou.messageswap.NodeSwap;

public class GsMsgSwap {

	private IGsSwap gsSwap;
	private GsMsgTunnel gsMsgTunnel;
	
	public void setGsMsgTunnel(GsMsgTunnel gsMsgTunnel) {
		this.gsMsgTunnel = gsMsgTunnel;
	}

	public void init(){
		
		this.gsMsgTunnel.setGsMsgSwap(this);
		
		
		this.gsSwap = new IGsSwap() {
			
			@Override
			public void swap(Object[] msg) {
				NodeSwap.swap(msg);
			}
		};		
	}

	/**
	 * 发送给单个客户端
	 * @param userid
	 * @param command
	 * @param result
	 */
	public void swap(Object[] msg){
		gsSwap.swap(msg);
	}

}
