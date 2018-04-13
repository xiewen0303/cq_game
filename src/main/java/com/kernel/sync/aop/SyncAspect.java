package com.kernel.sync.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.kernel.sync.Lock;
import com.kernel.sync.LockManager;
import com.kernel.sync.annotation.Sync;

/**
 * @description 同步切面
 * @author hehj
 * 2011-11-29 上午10:28:18
 */
public class SyncAspect {

	private LockManager lockManager = null;
	
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	/**
	 * 同步处理通知业务实现
	 */
	public Object sync(ProceedingJoinPoint pjp,Sync sync){
		Lock lock = lockManager.getLock(sync.component(),getLockKey(sync, pjp.getArgs()));
//		System.out.println("====="+sync.component()+","+getLockKey(sync, pjp.getArgs()));
		synchronized (lock) {
			try {
				Object ret = pjp.proceed();
				return ret;
			} catch (Throwable e) {
				throw new RuntimeException("error in sync invoke",e);
			}
		}
	}
	
	private String getLockKey(Sync sync,Object[] args){
		String component = sync.component();
		StringBuilder keyBuilder = new StringBuilder(component);
		int[] indexes = sync.indexes();
		if(null != indexes && indexes.length > 0){
			for(int index : indexes){
				Object arg = args[index];
				keyBuilder.append(arg);
			}
		}
		
		return keyBuilder.toString();
	}
}
