package com.junyou.configure.parser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.junyou.configure.loader.IConfigureLoader;
import com.junyou.configure.loader.RemoteConfigureLoader;
import com.junyou.configure.parser.AbsRefreshAbleConfigureParser;

/**
 * @description 远程可定时刷新配置解析器
 * @author ShiJie Chi
 * @date 2013-2-27 上午11:50:19 
 */
public abstract class AbsRemoteRefreshAbleConfigureParser extends AbsRefreshAbleConfigureParser {

	@Autowired
	@Qualifier("remoteReader")
	private RemoteConfigureLoader configureLoader;
	
	@Override
	protected IConfigureLoader getLoader() {
		return configureLoader;
	}
	
}
