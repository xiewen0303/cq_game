package com.kernel.sync.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.kernel.sync.Lock;
import com.kernel.sync.LockManager;
import com.kernel.sync.annotation.PublicSyncClass;

/**
 * @description 同步切面
 * @author hehj
 */
public class SyncAspect_1_Component {
	
	private LockManager lockManager;
	
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	/**
	 * 同步处理通知业务实现
	 */
	public Object sync(ProceedingJoinPoint pjp){
		
		PublicSyncClass publicSyncClass = pjp.getTarget().getClass().getAnnotation(PublicSyncClass.class);
		
		if(null != publicSyncClass){
			
//			System.out.println("===========++++"+publicSyncClass.component());
			
			Lock lock = lockManager.getLock(publicSyncClass.component(),publicSyncClass.component());
			synchronized (lock) {
				return execute(pjp);
			}
		
		}else{
			return execute(pjp);
		}
		
	}
	
	private Object execute(ProceedingJoinPoint pjp){
		try {
			Object ret = pjp.proceed();
			return ret;
		} catch (Throwable e) {
			throw new RuntimeException("error in sync invoke",e);
		}
	}
}
