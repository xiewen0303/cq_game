package com.junyou.configure.schedule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.configure.parser.AbsPlatformAndServerConfigure;


/**
 * 热发布配置读取
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-3-27 下午2:57:27
 */
public class RefreshableConfigureManager {
	
	private static ConcurrentMap<String,AbsPlatformAndServerConfigure> configures = new ConcurrentHashMap<String,AbsPlatformAndServerConfigure>();
	
	public static void addConfig(AbsPlatformAndServerConfigure config){
		configures.put(config.getSortName(), config);
	}
	
	public static int refresh(String sortName){
		AbsPlatformAndServerConfigure config = configures.get(sortName);
		if(config != null){
			return config.versionRefresh();
		}
		return -3;
	}
	
}
