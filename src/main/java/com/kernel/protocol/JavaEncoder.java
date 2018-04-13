package com.kernel.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/** 
 * 内部 编码器
 * @author DaoZheng Yuan
 * @created 2015-7-28下午12:52:28
 */
public class JavaEncoder extends MessageToByteEncoder<byte[]> {

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf byteBuf)
			throws Exception {
		
		if(msg != null){
			//写入业务数据
			byteBuf.writeBytes(msg);
		}
	}
}
