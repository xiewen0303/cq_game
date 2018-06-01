package com.junyou.io.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 * 来自客户端的协议解码
 */
public class Decoder extends ByteToMessageDecoder {
	
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int HEAD_LENGTH = 4;

        //这个HEAD_LENGTH是我们用于表示头长度的字节数。  由于上面我们传的是一个into类型的值，所以这里HEAD_LENGTH的值为4.
        int readableBytes = in.readableBytes();
        if (readableBytes < HEAD_LENGTH) {
            return;
        }

        in.markReaderIndex();                 // 我们标记一下当前的readIndex的位置
        int dataLength = in.readInt();        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        if (dataLength < 0) { 				  // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
            ctx.close();
        }
        
        if (readableBytes < dataLength) {// 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
            in.resetReaderIndex();
            return;
        }
        
        //读取协议号
//        int tepm = 
        in.readInt();// traceId
        int msgCode = in.readInt();

        //把传送过来的数据读取出来
        byte[] body = new byte[dataLength-8];
        in.readBytes(body);
        
//      Object msg = convertToObject(msgCode,body);  	  // 将byte数据转化为我们需要的对象。伪代码，用什么序列化，自行选择
        
        //此处可以使用对象池
//        out.add(new CSMessage((short) msgCode,body));   //TODO
	}
}
