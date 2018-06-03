package com.junyou.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.exception.GameCustomException;
import com.kernel.pool.executor.ThreadNameFactory;

public class NetListener {

	public static final String flashSafePolicy = "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";
	private static EventLoopGroup bossGroup = new NioEventLoopGroup(GameConstants.THREAD_NUM,new ThreadNameFactory("netty-game-boss-"));
	private static EventLoopGroup workerGroup = new NioEventLoopGroup(GameConstants.THREAD_NUM,new ThreadNameFactory("netty-game-worker-"));

	
	public static void start(){
		
		int port = ChuanQiConfigUtil.getClientIoPort();
//		int ioConcurrent = ChuanQiConfigUtil.getClientIoConcurrent();
		
		try {
			// Configure the server.
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			if(!PlatformConstants.isYueNan()){
				bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
				bootstrap.option(ChannelOption.SO_REUSEADDR, true);
				bootstrap.option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
				bootstrap.childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
			}else{
				bootstrap.option(ChannelOption.TCP_NODELAY, true);
				bootstrap.option(ChannelOption.SO_REUSEADDR, true);
//				bootstrap.option(ChannelOption.SO_RCVBUF, 43690); // 43690 为默认值
//				bootstrap.option(ChannelOption.SO_SNDBUF, 32768);// 32k
				bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
				
				bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
//				bootstrap.childOption(ChannelOption.SO_RCVBUF, 43690); // 43690 为默认值
//				bootstrap.childOption(ChannelOption.SO_SNDBUF, 32768);// 32k
				bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			}
			
			bootstrap.childHandler(new GameServerChannelPipelineFactory());
			
			
			// Bind and start to accept incoming connections.
			try {
				bootstrap.bind(new InetSocketAddress(port)).sync();
				ChuanQiLog.info("初始化TCP服务完毕 {}", port);
			} catch (Exception e) {
				ChuanQiLog.info("初始化TCP服务失败");
				throw new GameCustomException(e);
			}
			
		} catch (Exception e) {
			
			String errorMsg = "client's io failed to listen to port[" + port + "]";
			ChuanQiLog.error(errorMsg,e);
			
			throw new RuntimeException(errorMsg, e);
			
		}

		ChuanQiLog.error("client's io is listening to port[" + port + "]");
	}
	
	public static void stop(){
		try {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		} catch (Exception e) {
			ChuanQiLog.error("NetListener stop error",e);
		}
	}
}
