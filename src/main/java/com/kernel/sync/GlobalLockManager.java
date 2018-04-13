package com.kernel.sync;


/**
 * 全局锁管理
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2016-1-17 上午1:09:23
 */
public class GlobalLockManager {
	
	private LockManager lockManager = new LockManager();
	
	private static final GlobalLockManager INSTANCE = new GlobalLockManager();
	
	private GlobalLockManager(){
		
	}
	
	public static GlobalLockManager getInstance() {
		return INSTANCE;
	}
	
	public Lock getLock(String component,String lockKey){
		return lockManager.getLock(component, lockKey);
	}
}
