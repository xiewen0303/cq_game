package com.junyou.io.websocket;

import com.junyou.io.websocket.handler.WebSocketServerHandler;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WebSocketServer {
	
	/**
	 * 服务器端口
	 */
	private static final int PORT = ChuanQiConfigUtil.getClientIoPort();;
	
	/**
	 * 心跳时间(单位：秒)
	 */
	private static int heartBeat = 120;
	
	public static void start() {  
	        EventLoopGroup bossGroup = new NioEventLoopGroup();  
	        EventLoopGroup workerGroup = new NioEventLoopGroup();  
	        try {  
	            ServerBootstrap b = new ServerBootstrap();  
	            b.group(bossGroup, workerGroup);
	            b.channel(NioServerSocketChannel.class);
				b.option(ChannelOption.SO_BACKLOG, 1024);
	            b.option(ChannelOption.TCP_NODELAY, true);
	            b.option(ChannelOption.SO_REUSEADDR, true);
	            b.childOption(ChannelOption.SO_RCVBUF, 65536);
                b.childOption(ChannelOption.SO_SNDBUF, 65536);
                b.childOption(ChannelOption.SO_KEEPALIVE, true);
//	            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
	            b.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false));  // heap buf 's better

	            
	            b.childHandler(new ChannelInitializer<Channel>() {  
	                @Override  
	                protected void initChannel(Channel channel) throws Exception {  
	                    ChannelPipeline pipeline = channel.pipeline();  
	                    
	                    pipeline.addLast("idle", new IdleStateHandler(0, 0, heartBeat));
	                	
	                    pipeline.addLast("http-codec", new HttpServerCodec()); // Http消息编码解码  
	                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536)); // Http消息组装  
	                    pipeline.addLast("http-chunked", new ChunkedWriteHandler()); // WebSocket通信支持
//	                    pipeline.addLast(new WebSocketServerProtocolHandler("/")); // WebSocket通信支持
	                    pipeline.addLast("handler", new WebSocketServerHandler()); // WebSocket服务端Handler
	                }  
	            });
	              
	            Channel channel = b.bind(PORT).sync().channel();  
	            ChuanQiLog.info("WebSocket 已经启动，端口：" + PORT + ".");
	            channel.closeFuture().sync();  
	        }catch (Exception e) {
				ChuanQiLog.error("websocket 启动错误",e);
				e.printStackTrace();
			} finally {  
	            bossGroup.shutdownGracefully();  
	            workerGroup.shutdownGracefully();  
	        }  
	    }  
	
}
