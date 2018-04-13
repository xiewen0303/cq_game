package com.junyou.configure.schedule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.configure.parser.impl.AbsRefreshAbleConfigureParserByPlatformAndServer;
/**
 * 公告配置读取
 */
public class RefreshableNoticeConfigureManager {
	private static ConcurrentMap<String,AbsRefreshAbleConfigureParserByPlatformAndServer> configures = new ConcurrentHashMap<String,AbsRefreshAbleConfigureParserByPlatformAndServer>();
	
	public static void addConfig(AbsRefreshAbleConfigureParserByPlatformAndServer config){
		configures.put(config.getSortName(), config);
	}
	
	public static int refresh(String sortName){
		AbsRefreshAbleConfigureParserByPlatformAndServer config = configures.get(sortName);
		if(config != null){
			return config.versionRefresh();
		}
		return -3;
	}
	
}
