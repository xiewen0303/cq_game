package com.kernel.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.exadel.flamingo.flex.messaging.amf.io.AMF3Deserializer;
import com.junyou.log.ChuanQiLog;
import com.kernel.encrypt.SimpleEncrypt;


/** 
 * amf 解码器
 * @author DaoZheng Yuan
 * @created 2015-7-28下午12:52:28
 */
public class Amf3Decoder extends ByteToMessageDecoder {
	private static final Logger logger = LogManager.getLogger(Amf3Decoder.class);
	
	private final static String POLICY = "<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";
	private final static String POLICY_REQUEST = "<policy-file-request/>";
	private boolean amf3;
	private final SimpleEncrypt encrypt;
	private MutableBoolean common;//策略文件请求 false,正常消息true

	public Amf3Decoder( SimpleEncrypt encrypt, MutableBoolean common) {
		this.encrypt = encrypt;
		this.common = common;
	}

	@SuppressWarnings("resource")
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> objects) throws Exception {
		if (byteBuf.readableBytes() < 4) {
			return;
		}
		
		// mark读索引
		byteBuf.markReaderIndex();
		int length = byteBuf.readInt();
		
		if (length > 65536) {
			byteBuf.resetReaderIndex();
			byte[] content = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(content);
			String request = new String(content);
			
			// 怀疑是请求安全策略
			if (request.indexOf(POLICY_REQUEST) >= 0) {
				common.setFalse();
				ctx.channel().writeAndFlush(POLICY.getBytes()).sync();
				logger.warn("该用户无法访问843端口，从主端口获取安全策略 address={}", ctx.channel().remoteAddress());
				return;
			} else {
				logger.error("request msg length > 65536,address={} ,content={}", ctx.channel().remoteAddress(),request);
				byteBuf.resetReaderIndex();
				ctx.close();
			}
			return;
		}
		if (length < 0) {
			logger.error("request msg length <0,length={}", length);
			byteBuf.resetReaderIndex();
			ctx.close();
			return;
		}

		if (byteBuf.readableBytes() < length) {
			byteBuf.resetReaderIndex();
			return;
		}

		byte[] content = new byte[length];
		byteBuf.readBytes(content);
		if (amf3) {
			// logger.info("length={},msg={}", content.length, content);
			content = encrypt.decode(content);
			try {
				AMF3Deserializer deserializer = new AMF3Deserializer(new ByteArrayInputStream(content));
				short cmd = deserializer.readShort();
				Object cmdData = deserializer.readObject();
				Object[] msg = new Object[]{cmd,cmdData};
				objects.add(msg);
				
			} catch (Exception e) {
				ChuanQiLog.error("解码客户端发来的数据异常（怀疑客户端服务器端版对应不上或者被外挂访问）！");
			}
		} else {
			byte[] keyBytes = encrypt.getCurrKeyBytes();
			logger.info("handshake from {} by {}", ctx.channel().remoteAddress(), new String(content));
			ctx.channel().writeAndFlush(keyBytes);
			amf3 = true;
		}
	}
	
	
	/**
	 * 读取标识
	 * @param buffer
	 * @return int 实际数据长度  <br/>
	 */
	public int readRawVarInt32(ByteBuf byteBuf){
		int dataLen = 0;
		int offset = 0;
		int tmpBuffOffset = 0;
		
		while(byteBuf.readableBytes() >= 1){
			byte tmp = byteBuf.getByte(tmpBuffOffset++);
			
			//字节高位为0,表示结束
			if(tmp >= 0){
				dataLen |= tmp << offset;
				break;
			}else{
				dataLen |= (tmp & 0x7f) << offset;
				offset += 7;
			}
		}
		
		
		if(dataLen == 0){
			return dataLen;
		}else if(byteBuf.readableBytes() < dataLen){
			byteBuf.setIndex(byteBuf.readerIndex() - tmpBuffOffset, byteBuf.writerIndex());
			dataLen = -1;
		}
		
		return dataLen;
	}
}
