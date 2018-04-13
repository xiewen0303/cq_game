/**
 * 
 */
package com.kernel.data.accessor;

import com.junyou.utils.ChuanQiConfigUtil;


/**
 * @description 访问入口类型
 * @author ShiJie Chi
 * @created 2011-11-1下午4:07:06
 */
public class AccessType {
	
	/**
	 * 直接数据库
	 */
	private static final String DIRECT = "direct";
	
	/**
	 * 角色缓存库
	 */
	private static String ROLECACHE = "roleCache";
	static{
		int usecacheinfo = ChuanQiConfigUtil.getUseCacheInfo();
		if(usecacheinfo <= 0){
			ROLECACHE = DIRECT;
		}
	}
	
	private static String PUBLICCACHE = "publicCache";
	static{
		int usecacheinfo = ChuanQiConfigUtil.getUseCacheInfo();
		if(usecacheinfo <= 0){
			PUBLICCACHE = DIRECT;
		}
	}
	
	/**
	 * 配置缓存库
	 */
	private static final String CONFIGURECACHE = "configure";
	
	/**
	 * 获取直接访问类型
	 * @param
	 */
	public static String getDirectDbType(){
		return DIRECT;
	}
	
	/**
	 * 获取role缓存访问类型
	 */
	public static String getRoleCacheDbType(){
		return ROLECACHE;
	}

	/**
	 * 获取public缓存访问类型
	 */
	public static String getPublicCacheDbType(){
		return PUBLICCACHE;
	}

	/**
	 * 获取配置缓存访问类型
	 */
	public static String getConfigureCacheDbType() {
		return CONFIGURECACHE;
	}
	
	
}
