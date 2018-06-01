package com.junyou.io.websocket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 在下行到客户端之前，将传输数据对象进行编码
 */
public class Encoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		
//		super.encode();
//		byte[] body = 	convertToBytes(msg);
//		int dataLength = body.length;  //读取消息的长度
//      out.writeInt(dataLength);  //先将消息长度写入，也就是消息头
//      out.writeBytes(body);  //消息体中包含我们要发送的数据
		
//		ByteBuf ByteBuf = (ByteBuf)msg;　
//		byteBuffer.position(0);
		//System.err.println("发送容量为"+byteBuffer.capacity());
// 		ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(byteBuffer);
		
		ByteBuf byteBuf = (ByteBuf)msg; 
        out.writeBytes(byteBuf);  		   //消息体中包含我们要发送的数据
	}
} 