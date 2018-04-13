package com.junyou.configure.parser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.junyou.configure.loader.IConfigureLoader;
import com.junyou.configure.loader.RemoteConfigureLoaderByPlatformAndServer;

/**
 * 远程刷新配置解析器（等服务器通知刷新）
 * @author zhubo 
 * @email  zhubo@junyougame.com
 * @date 2015-4-23 下午2:24:44
 */
public abstract class AbsRemoteRefreshAbleConfigureParserByPlatformAndServer extends AbsRefreshAbleConfigureParserByPlatformAndServer {

	@Autowired
	private RemoteConfigureLoaderByPlatformAndServer configureLoader;
	
	@Override
	protected IConfigureLoader getLoader() {
		return configureLoader;
	}
	
}
