package com.junyou.io.websocket.ssl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.springframework.stereotype.Service;

/**
 * wind
 * websocket的ssl服务
 */
@Service
public class SSLService{

    private SslContext sslCtx;

    public void startup() throws Exception {
//        GameServerConfigService gameServerConfigService = LocalMananger.getInstance().getLocalSpringServiceManager().getGameServerConfigService();
//        GameServerConfig gameServerConfig = gameServerConfigService.getGameServerConfig();
//        boolean webSocketSSLFlag = gameServerConfig.isWebSockectSSLFlag();
//        if(webSocketSSLFlag){
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
//        }
    }

    public void shutdown() throws Exception {

    }

    public SslContext getSslCtx() {
        return sslCtx;
    }

    public void setSslCtx(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
}
