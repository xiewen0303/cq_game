package com.junyou.bus.platform.utils;

import com.junyou.utils.common.ConfigureUtil;

/**
 * 聚享游  通过pps服登陆
 * @author lxn
 *
 */
public class JuxiangYouUtil {
	private static final String PPSJXYParam = "config/platform/pps.juxiangyou.properties";
	/**
	 * 聚享游会员  通过pps服登陆回调他们地址
	 */
	public static String getCallbackUrl() {
		return ConfigureUtil.getProp(PPSJXYParam, "callback_url");
	}
	 
 
	
	
	
}
