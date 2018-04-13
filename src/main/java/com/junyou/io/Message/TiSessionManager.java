package com.junyou.io.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author DaoZheng Yuan
 * 2015年7月10日 下午4:32:49
 */
public class TiSessionManager {
	
	private static final TiSessionManager INSTANCE = new TiSessionManager();

	private final ConcurrentMap<Long, AtomicInteger> S_TIMES = new ConcurrentHashMap<Long, AtomicInteger>();
	
	private int MAX_TIMES = 5;
	
	
	private TiSessionManager(){
		
	}
	
	public static TiSessionManager getInstance() {
		return INSTANCE;
	}
	
	
	
	public boolean addTimes(Long sid){
		AtomicInteger times = S_TIMES.get(sid);
		if(times == null){
			times = new AtomicInteger(0); 
			S_TIMES.put(sid, times);
		}
		
		return times.incrementAndGet() >= MAX_TIMES;
	}
	
	
	public void removeTimes(Long sid){
		S_TIMES.remove(sid);
	}
}
