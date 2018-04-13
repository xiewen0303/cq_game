package com.junyou.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.context.GameServerContext;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.exception.JunYouCustomException;

/**
 * 客户端安全端口
 * @author DaoZheng Yuan
 * 2014年11月22日 下午3:47:37
 */
//@Component
public class SafeServiceListener {

	private final Logger LOG = LogManager.getLogger("io_error_logger"); 
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	/**
	 * flash安全端口
	 */
	private int safePort = 843;
	
//	@PostConstruct
	public void init(){
		
		if(GameServerContext.getServerInfoConfig().isNeedFlashSafe()){
			
			// Configure the server.
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			
			bootstrap.childHandler(new GameServerChannelPipelineFactory());
			
			// Bind and start to accept incoming connections.
			try {
				bootstrap.bind(new InetSocketAddress(safePort)).sync();
				ChuanQiLog.info("safe TCP服务完毕{}", safePort);
			} catch (Exception e) {
				ChuanQiLog.info("safe TCP服务失败");
				throw new JunYouCustomException(e);
			}
		}else{
			LOG.error("no need to start flash safe service.");
		}
	}
	
	public void stop(){
		try {
			if(GameServerContext.getServerInfoConfig().isNeedFlashSafe()){
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		} catch (Exception e) {
			ChuanQiLog.error("SafeServiceListener stop error",e);
		}
	}
}
