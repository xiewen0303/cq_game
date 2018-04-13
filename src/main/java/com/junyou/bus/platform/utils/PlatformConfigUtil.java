package com.junyou.bus.platform.utils;

import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.ConfigureUtil;

/**
 * 平台配置工具
 * @author LiuYu
 * 2015-6-6 下午3:59:12
 */
public class PlatformConfigUtil {
	private static final String PUBLIC_CONFIG_NAME = "config/platform/public.properties";
	private static String PLATFORM_CONFIG_NAME;
	static{
		PLATFORM_CONFIG_NAME = "config/platform/" + ChuanQiConfigUtil.getPlatfromId() + ".properties";
	}
	
	/**
	 * 获取公共平台配置信息
	 * @param key
	 * @return
	 */
	public static String getPublicConfigInfo(String key){
		return ConfigureUtil.getPropAllowNull(PUBLIC_CONFIG_NAME, key);
	}
	
	/**
	 * 获取当前平台配置信息
	 * @param key
	 * @return
	 */
	public static String getNowPlatformConfigInfo(String key){
		return ConfigureUtil.getPropAllowNull(PLATFORM_CONFIG_NAME, key);
	}
}
