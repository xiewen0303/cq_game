/**
 * 
 */
package com.kernel.data.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.kernel.data.accessor.exception.DataExceptionFactory;
import com.kernel.data.write.async_1.AsyncWriteManager;


/**
 * @description 角色缓存管理器
 * @author ShiJie Chi
 * @created 2011-10-31下午3:39:44
 */
public class CacheManager {
	
	private ConcurrentMap<Long,IEntityCache> roleCaches = new ConcurrentHashMap<Long, IEntityCache>();

	private String name;
	
	private boolean needCleaner = false;

	private Long cleanGap = 5 * 60 * 1000l;
	
	private IEntityCacheLoader entityCacheLoader;
	
	private AsyncWriteManager asyncWriteManager;
	
	
	
	public void init(){
//		if(false){
//			
//			Thread cleanThread = new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//
//					for(;;){
//						try {
//
//							Thread.sleep(cleanGap);
//
//							for(IEntityCache entityCache : roleCaches.values()){
//								// can't be synchronized
//								cleanRoleCache(entityCache.getRoleIdentity(),entityCache);
//							}
//							
//							LOG.error("CacheManager["+getName()+"]'s cache count " + roleCaches.size());
//							
//						} catch (Exception e) {
//							LOG.error("",e);
//						}
//					}
//				}
//				
//			},"CacheManager-Cleaner-"+getName());
//			cleanThread.setDaemon(true);
//			cleanThread.start();
//			
//		}
	}
	
	public void setNeedCleaner(boolean needCleaner) {
		this.needCleaner = needCleaner;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setCleanGap(long cleanGap) {
		this.cleanGap = cleanGap * 60 * 1000;
	}

	/**
	 * 获取管理器名字
	 * @return 
	 */
	public String getName() {
		return name;
	}

	public void setEntityCacheLoader(IEntityCacheLoader entityCacheLoader) {
		this.entityCacheLoader = entityCacheLoader;
	}

	public void setAsyncWriteManager(AsyncWriteManager asyncWriteManager) {
		this.asyncWriteManager = asyncWriteManager;
	}

	/**
	 * 激活指定id的全局缓存对象
	 * @param roleIdentity
	 */
	public void activateRoleCache(Long roleIdentity){
		if(null != asyncWriteManager){
			asyncWriteManager.syncAllDataChange(roleIdentity);
		}
		IEntityCache roleCache = null;
		if(null == roleCache){
			roleCache = entityCacheLoader.loadEntityCache(roleIdentity);
			roleCaches.put(roleIdentity, roleCache);
		}
		roleCache.activate();
	}
	
	/**
	 * 冻结指定id的全局缓存对象
	 * @param roleIdentity
	 */
	public void freezeRoleCache(Long roleIdentity){
		IEntityCache roleCache = roleCaches.remove(roleIdentity);
		if(null != asyncWriteManager){
			asyncWriteManager.flushDataContainer(roleIdentity);
		}
	}

	/**
	 * 清除指定id的全局缓存对象
	 * @param roleIdentity
	 */
	private void cleanRoleCache(Long roleIdentity,IEntityCache entityCache){
		if(entityCache.canClean()){
			roleCaches.remove(roleIdentity);
		}
	}
	
	/**
	 * 获取角色缓存
	 * @param
	 */
	public IEntityCache getRoleCache(Long roleIdentity){
		
		IEntityCache roleCache = roleCaches.get(roleIdentity);
		
		if(null == roleCache){
			throw DataExceptionFactory.getException("can't  matched roleCache to roleIdentity:" + roleIdentity);
		}
		
		return roleCache;
	}
	
	
}
