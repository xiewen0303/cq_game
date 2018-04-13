/**
 * 
 */
package com.junyou.configure.export.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.kernel.data.cache.CacheManager;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-9下午3:06:27
 */
@Component
public class ConfigureContext{
	
	@Autowired
	@Qualifier("configureCacheManager")
	private CacheManager cacheManager;
	
	public final static Long CONFIGURE_IDENTITY = -888808888L;
	
	/**
	 * 初始化
	 * @param
	 */
	public void init(){
		
		cacheManager.activateRoleCache(CONFIGURE_IDENTITY);
		
	}
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}

}
