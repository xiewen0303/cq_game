package com.kernel.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kernel.utils.SerializableHelper;


/** 
 * 内部 解码器
 * @author DaoZheng Yuan
 * @created 2015-7-28下午12:52:28
 */
public class JavaDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LogManager.getLogger(JavaDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf,
			List<Object> objects) throws Exception {
		
		try {
			if(byteBuf.readableBytes() <= 0){
				return;
			}
			
			byte[] bytes = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(bytes);
			
			// decode by java protocol
			Object msg = SerializableHelper.deserialize(bytes);
			if(msg != null){
				objects.add(msg);
			}
		} catch (Exception e) {
			logger.info("",e);
		}
	}
}
