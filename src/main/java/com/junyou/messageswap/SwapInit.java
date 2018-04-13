package com.junyou.messageswap;

import com.junyou.gs.tunnel.GsMsgTunnel;
import com.junyou.io.swap.IoMsgTunnel;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.public_.tunnel.PublicMsgTunnel;
import com.junyou.utils.ChuanQiConfigUtil;

public class SwapInit {

	private static IoMsgTunnel ioMsgTunnel;
	private static PublicMsgTunnel publicMsgTunnel;
	private static GsMsgTunnel gsMsgTunnel;
	
	public void setGsMsgTunnel(GsMsgTunnel gsMsgTunnel) {
		SwapInit.gsMsgTunnel = gsMsgTunnel;
	}
	
	public void setPublicMsgTunnel(PublicMsgTunnel publicMsgTunnel) {
		SwapInit.publicMsgTunnel = publicMsgTunnel;
	}
	
	public void setIoMsgTunnel(IoMsgTunnel ioMsgTunnel) {
		SwapInit.ioMsgTunnel = ioMsgTunnel;
	}
	
	public void init(){
		
		if(ChuanQiConfigUtil.isSwapNode()){
			
			// 建立与io-manage的通道
			NodeSwap.registClientTunnel(ioMsgTunnel);
			
			// 建立与public的通道
			NodeSwap.registPublicTunnel(publicMsgTunnel);
			
			// 核实是否单一节点
//			NodeSwap.setSingleGs(true);
				
			// 建立与gs的通道
			NodeSwap.registGsTunnel(gsMsgTunnel);

//			//建立与跨服server的通道
//			NodeSwap.registKuafuTunnel();
		}
	}
	
}
