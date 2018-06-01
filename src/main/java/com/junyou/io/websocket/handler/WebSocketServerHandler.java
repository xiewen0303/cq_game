package com.junyou.io.websocket.handler;

import com.junyou.bus.kuafu_dianfeng.utils.KuafuDianFengUtils;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1SourceService;
import com.junyou.bus.shenmo.service.KuafuArena4v4SourceService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.io.GameSession;
import com.junyou.io.IOExecutorsManager;
import com.junyou.io.global.GameSessionManager;
import com.junyou.io.global.GsCountChecker;
import com.junyou.io.swap.IoMsgSender;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.kuafumatch.manager.KuafuMatchMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.session.SessionConstants;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * wind
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

//    private static final String WEBSOCKET_PATH = "/websocket";
    private static final String WEBSOCKET_PATH = "";
    private WebSocketServerHandshaker handshaker;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object e) throws Exception {
        if (e instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) e;
            ChuanQiLog.error("close the channel: heartbeat {},type={}", ctx.channel(), event.state());
            ctx.close();
        }
    }

    /**
     * 建立连接 channelConnected
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ChuanQiLog.info("{} connect", channel);
        String remoteIp = ((InetSocketAddress)channel.remoteAddress()).getAddress().getHostAddress();

        if(GsCountChecker.isFull() && GsCountChecker.notWhiteIp(remoteIp)){
            //在线人数已满，同时不在IP白名单内的直接断开连接
            channel.close();
        }else{
            GameSession session = GameSessionManager.getInstance().addSession(SessionConstants.NOMAL_SESSION_TYPE,channel);
            IOExecutorsManager.bindExecutorService(session);
            //设置IP值
            session.setIp(remoteIp);
        }
    }


    /** 断开连接 原先的channelClosed */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChuanQiLog.error("{} channelClosed", ctx.channel());
        GameSession session = GameSessionManager.getInstance().getSession(ctx.channel());
        if (session == null) {
            ChuanQiLog.error("{} channel closed: no session id founded", ctx.channel());
        } else {
            Long roleId = session.getRoleId();
            if(roleId > 0){
                // 处理是否需要下线通知
                //回收老session:
                exitNotify(session);

//                //跨服信息回收
//                if(KuafuManager.kuafuIng(roleId) || KuafuArena1v1SourceService.isInKuafuArena(roleId)){
//                    KuafuMsgSender.send2KuafuServer(roleId,roleId, InnerCmdType.INNER_KUAFU_LEAVE, true);
//                    if(KuafuArena1v1SourceService.isInMatch(roleId)){
//                        KuafuMatchMsgSender.send2KuafuMatchServer(roleId, InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH, true);
//                        KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(roleId, InnerCmdType.KUAFU_ARENA_1V1_CANCEL_MATCH, true);
//                    }
//                }
//                if(KuafuManager.kuafuIng(roleId) || KuafuArena4v4SourceService.isInKuafuArena(roleId)){
//                    KuafuMsgSender.send2KuafuServer(roleId,roleId, InnerCmdType.INNER_KUAFU_LEAVE, false);
//                    if(KuafuArena4v4SourceService.isInMatch(roleId)){
//                        KuafuMatchMsgSender.send2KuafuMatchServer(roleId, InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH, true);
//                        KuafuMatchMsgSender.send2KuafuCompetitionMatchServer(roleId, InnerCmdType.KUAFU_ARENA_4V4_CANCEL_MATCH, true);
//                    }
//                }
//                Redis redis = GameServerContext.getRedis();
//                if(redis!=null){
//                    if(redis.get(RedisKey.getKuafuBossBindServerIdKey(roleId))!=null){
//                        KuafuServerInfo serverInfo =	KuafuRoleServerManager.getInstance().getBindServer(roleId);
//                        if(serverInfo!=null){
//                            KuafuMsgSender.send2KuafuServer(roleId, roleId,
//                                    InnerCmdType.KUAFUBOSS_EXIT, null);
//                        }
//                    }
//                    if(redis.get(RedisKey.getKuafuQunXianYanBindServerIdKey(roleId))!=null){
//                        KuafuServerInfo serverInfo =	KuafuRoleServerManager.getInstance().getBindServer(roleId);
//                        if(serverInfo!=null){
//                            KuafuMsgSender.send2KuafuServer(roleId, roleId,InnerCmdType.KUAFUQUNXIANYAN_EXIT, null);
//                        }
//                    }
//                    //巅峰之战跨服战玩家在比赛中途下线处理
//                    if (KuafuManager.kuafuIng(roleId) && redis.exists(RedisKey.KUAFU_DIANFENG_SERVER_ID)) {
//                        String kfDianFengServerId = KuafuDianFengUtils.getDianFengKuaFuServerId();
//                        KuafuServerInfo kfServerInfo = KuafuRoleServerManager.getInstance().getBindServer(roleId);
//                        if (null != kfServerInfo && kfServerInfo.getServerId().equals(kfDianFengServerId)) {
//                            //ChuanQiLog.info("巅峰之战跨服战期间,玩家{0}在比赛中途下线处理", roleId);
//                            KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, roleId, InnerCmdType.KUAFU_DIANFENG_OFFINE, new Object[] { roleId });
//                        }
//                    }
//                }
//                KuafuManager.removeKuafu(roleId);
            }
            IOExecutorsManager.removebindExecutorService(session);
            ChuanQiLog.info("channelInactive {}/{} closed", ctx.channel(), session.getId());
        }

        //容错
        GameSessionManager.getInstance().removeSession(ctx.channel());
    }

    private void exitNotify(GameSession session) throws Exception {
        IoMsgSender.swap(new Object[]{InnerCmdType.NODE_INIT_OUT,null,0,1,1,session.getId(),session.getIp(),session.getRoleId(),null,null});
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception  {
        if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
        // 传统的HTTP接入
        else if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if(msg instanceof TextWebSocketFrame){
            String request = ((TextWebSocketFrame) msg).text();
            System.out.println(ctx.channel()+" received" + request);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }


        //二进制
        if(frame instanceof BinaryWebSocketFrame){

            BinaryWebSocketFrame b = (BinaryWebSocketFrame)frame;
            ByteBuf byteBuf = b.content();
            int len = byteBuf.readableBytes();
            byte[] req = new byte[len];
            byteBuf.readBytes(req);


            //TODO 转发到业务数据中去


//            IMessageReadable messageReadable = null;
//            short cmd = 0;
//            try {
//                MsgSendHead.MsgBase msgBase = MsgSendHead.MsgBase.parseFrom(req);
//                int msgCode = msgBase.getMsgId();
//                ByteString contentByteStr = msgBase.getData();
//                messageReadable = new CSMessage((short) msgCode,contentByteStr.toByteArray());
//                cmd = messageReadable.getMessageCode();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            IPlayer player = null;
//            if(MessageCode.CS_Login == cmd){
//                IPlayer newPlayer = new PlayerForWS();
//                newPlayer.setCtx(ctx);
//                ctx.attr(CHANNEL_SESSION_KEY).set(newPlayer);
//                ctx.fireChannelRegistered();
//                player = newPlayer;
//            }else{ //如果是登录时可以创建player
//                player = ctx.attr(CHANNEL_SESSION_KEY).get();
//                if(player == null) {
//                    LogUtil.error("请先登录");
//                    return;
//                }
//
//                if(player.getAccountId() == null){
//                    LogUtil.error("登录流程还未处理完！");
//                    return;
//                }
//            }
//
//            messageReadable.dispatch(player);
//
//            return;
        }

        ChuanQiLog.error("{} frame types not supported", frame.getClass().getName());

    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // 如果HTTP解码失败，返回HHTP异常
        if (!req.getDecoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade").toLowerCase()))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8801"+WEBSOCKET_PATH, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private static void sendHttpResponse(
            ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

//    private static String getWebSocketLocation(HttpRequest req) {
//        String location =  req.headers().get(HttpHeaderNames.HOST) + WEBSOCKET_PATH;
//        return "ws://" + location;
////        if (WebSocketServer.SSL) {
////            return "wss://" + location;
////        } else {
////            return "ws://" + location;
////        }
//    }

    public WebSocketServerHandshaker getHandshaker() {
        return handshaker;
    }

    public void setHandshaker(WebSocketServerHandshaker handshaker) {
        this.handshaker = handshaker;
    }
}

