package com.junyou.kuafu.share.tunnel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.internal.SystemPropertyUtil;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.kuafumatch.manager.KuafuMatchSourceNetHandler;
import com.junyou.log.ChuanQiLog;
import com.junyou.messageswap.ISwapTunnel;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.pool.executor.JunYouThreadFactory;
import com.kernel.protocol.JavaDecoder;
import com.kernel.protocol.JavaEncoder;
import com.kernel.utils.SerializableHelper;

public class KuafuNetTunnel implements ISwapTunnel {
	
	private KuafuServerInfo serverInfo;

	public KuafuServerInfo getServerInfo() {
		return serverInfo;
	}

	private Bootstrap bootstrap = new Bootstrap();
	private EventLoopGroup workerGroup = null;
	private Channel channel = null;
	
	public void initTunnel(final KuafuServerInfo serverInfo) {
		this.serverInfo = serverInfo;
		workerGroup = new NioEventLoopGroup(4,new JunYouThreadFactory("netty-kuafu-worker-"+serverInfo.getServerId()+"-"));
		bootstrap.group(workerGroup).channel(NioSocketChannel.class);
		final KuafuNetTunnel kuafuNetTunnel = this;
		bootstrap.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				pipeline.addLast("decoder", new JavaDecoder());
				pipeline.addLast("encoder", new JavaEncoder());
				if(!serverInfo.isMatchServer()){
					pipeline.addLast("handler", new KuafuSourceNetHandler(kuafuNetTunnel));
				}else{
					pipeline.addLast("handler", new KuafuMatchSourceNetHandler(kuafuNetTunnel));
				}
			}
		});
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		
		try {
			ChannelFuture future = bootstrap.connect(serverInfo.getIp(), serverInfo.getPort()).sync();
			if(future.isSuccess()){
				ChuanQiLog.info("connect success to remote tunnel 2 [{}:{}]", serverInfo.getIp(), serverInfo.getPort());
				
				channel = future.channel();
				receiveMsg(channel, new Object[] {InnerCmdType.DEFAULT_CMD,GameConstants.DEFAULT_ROLE_ID,new Object[] { ChuanQiConfigUtil.getWsOutPort(),ChuanQiConfigUtil.getServerId() } });
			}else{
				ChuanQiLog.ioError("can't connect to remote tunnel 2 [{}:{}]", serverInfo.getIp(), serverInfo.getPort());
				
				future.cancel(true);
				workerGroup.shutdownGracefully();
				workerGroup.terminationFuture();
				workerGroup = null;
			}
			
		} catch (Exception e) {
			if(channel!=null){
				if(channel.isOpen()){
					channel.close();
				}
			}
			if(workerGroup!=null){
				workerGroup.shutdownGracefully();
				if(workerGroup!=null){
					workerGroup.terminationFuture();
				}
			}
			ChuanQiLog.ioError(String.format("连接Server(IP[%s],PORT[%s])失败", serverInfo.getIp(),serverInfo.getPort()),e);
		}
	}

	public boolean isConnected() {
		if (channel != null) {
			return channel.isOpen(); 
		}
		return false;
	}

	public void destroy() {
		if (isConnected()) {
			channel.close();
		}
		if (bootstrap != null) {
			if(workerGroup!=null){
				workerGroup.shutdownGracefully();
				if(workerGroup!=null){
					workerGroup.terminationFuture();
				}
			}
		}
		channel = null;
		workerGroup = null;
		bootstrap = null;
	}

	@Override
	public void receive(Object msg) {
		receiveMsg(channel, msg);
	}

	private void receiveMsg(Channel channel, Object msg) {
		if (channel != null && channel.isActive()) {
			byte[] msgBytes = SerializableHelper.serialize(msg);
			channel.writeAndFlush(msgBytes);
		}
	}

}
