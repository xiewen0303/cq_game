package com.junyou.stage.schedule;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.kernel.token.ITokenRunnable;
import com.kernel.token.TokenManager;

public class StageScheduleExecutor {
	
	private String identity;
	
	private ConcurrentMap<String,FutureWrapper> futureMap;
	
	public StageScheduleExecutor(String identity) {
		this.identity = identity;
	}
	
	public void schedule(Runnable runnable,Integer delay, TimeUnit unit){
		
		StageGlobalScheduleExecutor.getScheduleExecutor().schedule(runnable, delay, unit);
		
	}

	public void schedule(String identity, String component,
			ITokenRunnable task, Integer delay, TimeUnit unit) {
		
		TokenManager tokenManager = StageGlobalScheduleExecutor.getTokenManager();
		Long token = tokenManager.createToken(component, identity);
		task.setToken(new Object[]{identity,token});
		
		//future处理
		if(null == futureMap){
			futureMap = new ConcurrentHashMap<String, FutureWrapper>();
		}
		
		String futureKey = getFutureKey(identity, component);
		
		ScheduledFuture newFutre = StageGlobalScheduleExecutor.getScheduleExecutor().schedule(task, delay, unit);
		FutureWrapper newFutureWrapper = new FutureWrapper(newFutre, identity, component);
		
		//删
		FutureWrapper hisFutureWrapper = futureMap.get(futureKey);
		if(null != hisFutureWrapper){
			hisFutureWrapper.getFuture().cancel(true);
		}

		//加
		futureMap.put(futureKey, newFutureWrapper);
		
	}
	public void schedule(String identity, String component,
			ITokenRunnable task, Long delay, TimeUnit unit) {
		
		TokenManager tokenManager = StageGlobalScheduleExecutor.getTokenManager();
		Long token = tokenManager.createToken(component, identity);
		task.setToken(new Object[]{identity,token});
		
		//future处理
		if(null == futureMap){
			futureMap = new ConcurrentHashMap<String, FutureWrapper>();
		}
		
		String futureKey = getFutureKey(identity, component);
		
		ScheduledFuture newFutre = StageGlobalScheduleExecutor.getScheduleExecutor().schedule(task, delay, unit);
		FutureWrapper newFutureWrapper = new FutureWrapper(newFutre, identity, component);
		
		//删
		FutureWrapper hisFutureWrapper = futureMap.get(futureKey);
		if(null != hisFutureWrapper){
			hisFutureWrapper.getFuture().cancel(true);
		}

		//加
		futureMap.put(futureKey, newFutureWrapper);
		
	}
	
	public void cancelSchedule(String identity, String component) {
		
		TokenManager tokenManager = StageGlobalScheduleExecutor.getTokenManager();
		tokenManager.removeToken(component, identity);
		
		//future处理
		//TODO
		if(null != futureMap){
			FutureWrapper hisFutreWrapper = futureMap.get(getFutureKey(identity, component));
			if(null != hisFutreWrapper){
				hisFutreWrapper.getFuture().cancel(true);
			}
		}
	}
	
	/**
	 * 
	 */
	public void clear(){
		if(null != futureMap){
			for(FutureWrapper tmp : futureMap.values()){
				cancelSchedule(tmp.getIdentity(), tmp.getComponent());
			}
		}
	}
	
	
	/**
	 * 
	 */
	private String getFutureKey(String identity, String component){
		return new StringBuffer().append(identity).append("_").append(component).toString();
	}
	
	/**
	 * @description 
	 * @author ShiJie Chi
	 * @date 2012-9-5 下午2:50:58 
	 */
	private static class FutureWrapper{
		
		private ScheduledFuture future;
		
		private String identity;
		
		private String component;
		
		public FutureWrapper(ScheduledFuture future,String identity,String component) {
			
			this.future = future;
			this.identity = identity;
			this.component = component;
			
		}

		public ScheduledFuture getFuture() {
			return future;
		}

		public void setFuture(ScheduledFuture future) {
			this.future = future;
		}

		public String getIdentity() {
			return identity;
		}

		public void setIdentity(String identity) {
			this.identity = identity;
		}

		public String getComponent() {
			return component;
		}

		public void setComponent(String component) {
			this.component = component;
		}
		
		
		
	}

}
