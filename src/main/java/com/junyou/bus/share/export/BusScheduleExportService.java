package com.junyou.bus.share.export;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.share.schedule.BusScheduleExecutor;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.kernel.token.TokenManager;

@Component
public class BusScheduleExportService{
	
	@Autowired
	private TokenManager tokenManager;
	
	/**
	 * key:component [key:identity value: scheduledFuture]
	 */
	ConcurrentMap<String,ConcurrentMap<String, ScheduledFuture>> taskFutureMap = new ConcurrentHashMap<String, ConcurrentMap<String, ScheduledFuture>>(); 
	
	public void schedule(String roleId,String component, BusTokenRunable task, int delay, TimeUnit unit) {
		
		Long token = tokenManager.createToken(component, roleId);
		task.setToken(new Object[]{roleId,token});
		
		ScheduledFuture future  = BusScheduleExecutor.schedule(task, delay, unit);
		
		 ConcurrentMap<String, ScheduledFuture> map = taskFutureMap.get(component);
		 if(null == map){
			 map = new ConcurrentHashMap<String, ScheduledFuture>();
			 taskFutureMap.put(component, map);
		 }
		 map.put(roleId, future);
	}

	public void cancelSchedule(String roleId,String component) {
		
		tokenManager.removeToken(component, Long.parseLong(roleId));
		
		ConcurrentMap<String, ScheduledFuture> map = taskFutureMap.get(component);
		if(null != map){
			ScheduledFuture future = map.remove(roleId);
			if(null != future){
				future.cancel(true);
			}
			if(map.size() == 0){
				taskFutureMap.remove(component);
			}
		}
		
		
	}


}
