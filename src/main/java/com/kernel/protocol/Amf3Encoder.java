package com.kernel.protocol;

import org.apache.commons.lang3.mutable.MutableBoolean;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/** 
 * amf 编码器
 * @author DaoZheng Yuan
 * @created 2015-7-28下午12:52:28
 */
public class Amf3Encoder extends MessageToByteEncoder<byte[]> {
	private MutableBoolean common;//是否正常通信

	public Amf3Encoder(MutableBoolean common) {
		this.common = common;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf byteBuf) throws Exception {
		if (msg != null) {
			if (common.isTrue()) {
//				writeRawVarInt32(msg, byteBuf);
				byteBuf.writeInt(msg.length);
			} else {
				common.setFalse();
			}
			byteBuf.writeBytes(msg);
		}
	}
	
	
	/**
	 * 可变长度写入
	 * @param len
	 * @param buff
	 */
	public void writeRawVarInt32(byte[] msg,ByteBuf buff){
		int len = msg.length;
		while(true){
			if ((len & ~0x7F) == 0){
				buff.writeByte((byte)len);
				return;
			}else{
				int tmp = (len & 0x7F) | 0x80;
				buff.writeByte((byte)tmp);
				
				//右移7位赋值，左边空出的位以0填充
				len>>>=7;
			}
		}
	}
}
