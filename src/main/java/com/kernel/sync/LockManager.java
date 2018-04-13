package com.kernel.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LockManager {

	private static final Log LOG = LogFactory.getLog("server_status_logger");
	
	private ConcurrentMap<String, ConcurrentMap<String, Lock>> components = new ConcurrentHashMap<String, ConcurrentMap<String,Lock>>();
	private static final long cleanPeriod = 30 * 60 * 1000;
	{
		Thread t = new Thread("LockManager-Cleaner"){
			
			@Override
			public void run() {
				for(;;){
					try {
						
						Thread.sleep(cleanPeriod);
						
						try{
							
							clean();
							
						}catch (Exception e) {
							LOG.error("",e);
						}
						
					} catch (Exception e) {
						LOG.error("",e);
					}
				}
			}
			
		};
		t.setDaemon(true);
		t.start();
	}
	
//	public Lock getLock(String component,String lockKey){
//		
//		ConcurrentMap<String, Lock> componentLocks = getComponentLocks(component);
//		synchronized (componentLocks) {
//			Lock lock = componentLocks.get(lockKey);
//			if(null == lock){
//				lock = new Lock(lockKey);
//				componentLocks.put(lockKey, lock);
//			}
//			lock.update();
//			return lock;
//		}
//	}
	public Lock getLock(String component,String lockKey){
		
		ConcurrentMap<String, Lock> componentLocks = getComponentLocks(component);
		
		Lock lock = componentLocks.get(lockKey);
		if(null == lock){
			synchronized (componentLocks) {
				lock = new Lock(lockKey);
				componentLocks.put(lockKey, lock);
			}
		}
		lock.update();
		return lock;
	}
	
	private ConcurrentMap<String, Lock> getComponentLocks(String component){
		ConcurrentMap<String, Lock> componentLocks = components.get(component);
		if(null == componentLocks){
			synchronized (components) {
				componentLocks = components.get(component);
				if(null == componentLocks){
					componentLocks = new ConcurrentHashMap<String, Lock>();
					components.put(component, componentLocks);
				}
			}
		}
		return componentLocks;
	}
	
//	public void clean(){
//		int cleanCount = 0;
//		long remianCount = 0;
//		for(ConcurrentMap<String, Lock> componentLocks : components.values()){
//			synchronized (componentLocks) {
//				
//				for(Lock lock : componentLocks.values()){
//					if(lock.canClean()){
//						componentLocks.remove(lock.getKey());
//						cleanCount ++;
//					}
//				}
//				
//				remianCount += componentLocks.size();
//				
//			}
//			
//		}
//		LOG.error("LockManager-Cleaner:cleaned " + cleanCount + ",remain " + remianCount);
//	}
	public void clean(){
		int cleanCount = 0;
		long remianCount = 0;
		for(ConcurrentMap<String, Lock> componentLocks : components.values()){
			List<String> tobeMoveList = new ArrayList<String>();
				for(Lock lock : componentLocks.values()){
					if(lock.canClean()){
						tobeMoveList.add(lock.getKey());
						cleanCount ++;
					}
				}
				if(tobeMoveList.size()>0){
					synchronized (componentLocks) {
						for(String e:tobeMoveList){
							componentLocks.remove(e);
						}
					}
				}
				remianCount += componentLocks.size();
			
		}
		LOG.error("LockManager-Cleaner:cleaned " + cleanCount + ",remain " + remianCount);
	}
}
