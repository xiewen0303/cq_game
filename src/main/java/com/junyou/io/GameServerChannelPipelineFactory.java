package com.junyou.io;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.junyou.context.GameServerContext;
import com.junyou.io.handler.NetIoServerHandler;
import com.junyou.session.SessionConstants;
import com.kernel.encrypt.SimpleEncrypt;
import com.kernel.protocol.Amf3Decoder;
import com.kernel.protocol.Amf3Encoder;

public class GameServerChannelPipelineFactory extends ChannelInitializer<Channel> {

	public GameServerChannelPipelineFactory() {
		
	}
	
	/**
	 * 心跳时间(单位：秒)
	 */
	private int heartBeat = 120;
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		byte[] keyBytes = SimpleEncrypt.getKeyBytes();
		MutableBoolean common = new MutableBoolean(true);
		SimpleEncrypt encrypt = new SimpleEncrypt(keyBytes, getSecdictKey(keyBytes));
		ChannelPipeline p = ch.pipeline();
		p.addLast("idle", new IdleStateHandler(0, 0, heartBeat));
		
		p.addLast("decoder", new Amf3Decoder(encrypt,common));
		p.addLast("encoder", new Amf3Encoder(common));
		
		p.addLast("modele-open-filter",new FilterChannleHandler());
		//执行顺序为注册顺序的逆序;
		p.addLast("handle", new NetIoServerHandler());
		
	}
	
	private byte[] getSecdictKey(byte[] pubKey){
		int b = pubKey[pubKey.length - 1] & 0xff;
		int offset = b >> 4 & 15;
		int length = b % 10 + 10;

		byte[] secKey = new byte[length];
		// 计算私钥
		for (int i = 0; i < length; i++) {
			b = pubKey[(offset + i) % SessionConstants.PUBLIC_KEY_STATIC_LENGTH] & 0xff;
			int secKeyOffset = (i & 0) == 1 ? b : GameServerContext.getSecdictBytes().length - 1 - b;
			secKey[i] = GameServerContext.getSecdictBytes()[secKeyOffset];
		}
		
		return secKey;
	}
}