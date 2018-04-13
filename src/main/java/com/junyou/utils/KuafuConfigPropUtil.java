package com.junyou.utils;

import com.junyou.analysis.ServerInfoConfigManager;

/**
 * 跨服相关属性文件解析
 * @author DaoZheng Yuan
 * 2014-8-9 下午3:10:48
 */
public class KuafuConfigPropUtil {
	
	/**
	 * 是否为跨服服务器
	 * @return true:是跨服务器,false:不是 是正常的源服务器;
	 */
	public static boolean isKuafuServer(){
		return ServerInfoConfigManager.getInstance().getKuafuAppConfig().isKfServer();
	}
	
}
