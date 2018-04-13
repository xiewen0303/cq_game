package com.junyou.kuafuio.handle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import com.kernel.protocol.JavaDecoder;
import com.kernel.protocol.JavaEncoder;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafumatch.manager.KuafuMatchNetIoHandle;

public class KuafuServerChannelPipelineFactory extends ChannelInitializer<Channel> {

	public KuafuServerChannelPipelineFactory() {
		
	}
	
	/**
	 * 心跳时间(单位：秒)
	 */
//	private int heartBeat = 120;

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
//		p.addLast("idle", new IdleStateHandler(0, 0, heartBeat));
		p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
		p.addLast("frameEncoder", new LengthFieldPrepender(4));
		p.addLast("decoder", new JavaDecoder());
		p.addLast("encoder", new JavaEncoder());
		if(!KuafuManager.isMatchServer()){
			p.addLast("handle", new KuafuNetIoHandle());
		}else{
			p.addLast("handle", new KuafuMatchNetIoHandle());
		}
	}
}