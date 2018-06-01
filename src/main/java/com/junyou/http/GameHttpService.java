package com.junyou.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.junyou.constants.GameConstants;
import com.junyou.http.codec.HttpServerPipelineFactory;
import com.junyou.http.handle.RpcBrokerService;
import com.junyou.http.processor.GameHttpProcessor;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.pool.executor.ThreadNameFactory;
import com.kernel.spring.SpringApplicationContext;

/**
 * http服务启动类
 * @see 这里并没有配置backlog,那么它会采用操作系统默认的连接请求队列长度50
 * @see 详见{@link org.apache.mina.core.polling.AbstractPollingIoAcceptor}类源码的96行
 * 2014年11月18日 下午5:18:55
 */
public class GameHttpService {

	private static int port = ChuanQiConfigUtil.getWsOutPort();
	
	private ServerBootstrap bootstrap;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;
	
	private static final GameHttpService INSTANCE = new GameHttpService();
	
	private GameHttpService(){
		
	}
	
	public static GameHttpService getInstence(){
		return INSTANCE;
	}
	/**
	 * 启动
	 * @throws IOException
	 */
	public void start() throws IOException{
		GameHttpProcessor gameHttpProcessor = SpringApplicationContext.getApplicationContext().getBean(GameHttpProcessor.class);
		RpcBrokerService.getInstance().initialize(gameHttpProcessor);
		
		
		// Configure the server.
		bootstrap = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(GameConstants.THREAD_NUM,new ThreadNameFactory("netty-http-boss-"));
		workerGroup = new NioEventLoopGroup(2,new ThreadNameFactory("netty-http-worker-"));
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		// Set up the event pipeline factory.
		bootstrap.childHandler(new HttpServerPipelineFactory());
		// Enable TCP_NODELAY to handle pipelined requests without latency.
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, false);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		// Bind and start to accept incoming connections.
		try {
			bootstrap.bind(new InetSocketAddress(port)).sync();
			ChuanQiLog.info("初始化HTTP服务完毕[{}]",port);
		} catch (Exception e) {
			ChuanQiLog.error("初始化HTTP服务失败");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 停止
	 * @throws IOException
	 */
	public void stop(){
		ChuanQiLog.stopLog("http端口开始关闭。");
		try {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			// Wait until all threads are terminated.
			bossGroup.terminationFuture();

			// Shut down all event loops to terminate all threads.
			workerGroup.shutdownGracefully();
			// Wait until all threads are terminated.
			workerGroup.terminationFuture();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			ChuanQiLog.stopLog("HTTP服务已关闭");	
		}
	}
	
}
