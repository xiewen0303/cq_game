package com.kernel.data.cache;

import com.kernel.sync.annotation.Sync;

public class SyncCacheManager extends CacheManager {

	private static final String COMPONENT_NAME = "!@#$cache";

	/**
	 * 激活指定id的全局缓存对象
	 * @param roleIdentity
	 */
	@Sync(component=COMPONENT_NAME,indexes={0})
	public void activateRoleCache(Long roleIdentity){
		super.activateRoleCache(roleIdentity);
	}
	
	/**
	 * 冻结指定id的全局缓存对象
	 * @param roleIdentity
	 */
	@Sync(component=COMPONENT_NAME,indexes={0})
	public void freezeRoleCache(Long roleIdentity){
		super.freezeRoleCache(roleIdentity);
	}
	
}
