package com.junyou.configure.parser.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.configure.loader.IConfigureLoader;
import com.junyou.configure.loader.RemoteConfigureLoaderByPlatformAndServer;
import com.junyou.configure.parser.AbsPlatformAndServerConfigure;

/**
 * 远程可刷新配置解析器(等待后台通知刷新)
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-4-24 下午4:42:05
 */
public abstract class AbsRemoteRefreshAbleConfigureParserWait extends AbsPlatformAndServerConfigure {

	@Autowired
	private RemoteConfigureLoaderByPlatformAndServer remoteConfigureLoaderByPlatformAndServer;
	
	@Override
	protected IConfigureLoader getLoader() {
		return remoteConfigureLoaderByPlatformAndServer;
	}
	
}
