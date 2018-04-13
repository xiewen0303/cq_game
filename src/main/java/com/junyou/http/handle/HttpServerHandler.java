package com.junyou.http.handle;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.junyou.http.codec.TokenConstant;
import com.junyou.http.codec.TokenMsg;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;

/**
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
	private static final Logger logger = LogManager.getLogger(HttpServerHandler.class);
	
	private byte[] bytes = null;
	private int curIndex = 0;
	private boolean keepAlive = false;
	private HttpMethod requestMethod;
	private String fullMsg = null;
	
	public HttpServerHandler() {
		logger.info("创建HttpServerHandler");
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("{} channelActive", ctx.channel());
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("{} channelInactive  ", ctx.channel());
	}

	private TokenMsg getTokenMsg(TokenMsg tokenMsg){
		if(tokenMsg == null){
			tokenMsg = new TokenMsg();
		}
		
		return tokenMsg;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
		Channel ch = ctx.channel();
		String remoteIp = ((InetSocketAddress)ch.remoteAddress()).getAddress().getHostAddress();
		
		TokenMsg token = null;
		if (msg instanceof HttpRequest) {
			
			HttpRequest req = (HttpRequest) msg;
			if (req.getUri().endsWith(TokenConstant.BUSI_CODE_FAVICON_ICO_NAME)) {
				return;
			}
			
			String gameUrl = req.getUri();
			if(gameUrl.indexOf(TokenConstant.HTTP_PARAM_SPLIT_CHAR) > 0){
				gameUrl = gameUrl.substring(0, gameUrl.indexOf(TokenConstant.HTTP_PARAM_SPLIT_CHAR));
			}
			
			if (!gameUrl.endsWith(ChuanQiConfigUtil.getGmRootName())) {
				send500Continue(ctx);
				return;
			}
			
			if (HttpHeaders.is100ContinueExpected(req)) {
				send100Continue(ctx);
			}
			keepAlive = isKeepAlive(req);
			
			requestMethod = req.getMethod(); 
			token = getTokenMsg(token);
			token.setBusiType(requestMethod.name());
			
			//如果是get,解析请求参数
			if( requestMethod.equals(HttpMethod.GET) ){
				fullMsg = parseGetParams(req);
			}else{
				//如果是post,构造一个字节数组存参数,待结束之后解析成字符串
				int length = (int)HttpHeaders.getContentLength(req);
				bytes = new byte[length];
			}
		}
		if( msg instanceof HttpContent ){
			HttpContent httpContent = (HttpContent) msg;
			ByteBuf buffer = httpContent.content();
			
			//把所有字节都copy到一个字节数组，然后解析
			if( buffer.isReadable() ){
				int tmplength = buffer.readableBytes();
				buffer.readBytes(bytes, curIndex, tmplength);
				curIndex += tmplength;
			}
			
			
			String callBack = null;
			if( msg instanceof LastHttpContent ){
				FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, OK);
				resp.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
				int length = 0;
				
				try {
					if(requestMethod==null){
						ChuanQiLog.error("requestMethod is null remoteIp={} fullMsg={}",remoteIp,fullMsg);
						return;
					}
					if(requestMethod.equals(HttpMethod.GET)) {
						token = getTokenMsg(token);
						token.setRemoteIp(remoteIp);
						token.setBusiMessage(fullMsg);
						
						callBack = RpcBrokerServlet.getInstance().doGet(token);
					} else {
						fullMsg = new String(bytes, CharsetUtil.UTF_8);
						token = getTokenMsg(token);
						token.setRemoteIp(remoteIp);
						token.setBusiMessage(fullMsg);
						
						callBack = RpcBrokerServlet.getInstance().doPost(token);
					}
					length = callBack.getBytes().length;
					if(callBack != null){
						resp.content().writeBytes(callBack.getBytes());
					}
				} catch (Exception e) {
					resp.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
					ChuanQiLog.error("",e);
				}

				if (length == 0) {
					resp.setStatus(HttpResponseStatus.NO_CONTENT);
				}
				resp.headers().set(CONTENT_LENGTH, length);
				if (keepAlive) {
					resp.headers().set(CONNECTION, Values.CLOSE);
				}
				// Write the response.
				ChannelFuture future = ch.writeAndFlush(resp);
	
				if (!keepAlive || resp.getStatus() != HttpResponseStatus.OK) {
					future.addListener(ChannelFutureListener.CLOSE);
				}
			}
		}
	}
	
	
	/** 获取get请求的参数 */
	private String parseGetParams(HttpRequest req){
		String[] urlSplits = req.getUri().split("\\?");
		
		if( urlSplits.length > 1 ){
			return urlSplits[1];
		}
		return null;
	}
	
	
	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.write(response);
	}
	
	private static void send500Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		ctx.write(response);
	}
	

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException) {
			logger.warn(cause.getMessage());
		} else {
			logger.error("server failed: " + cause.getMessage(), cause);
		}
		if (ctx.channel().isActive()) {
			ctx.channel().close();
		}
	}


}
