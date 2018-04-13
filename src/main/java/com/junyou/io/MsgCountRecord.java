package com.junyou.io;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 临时打印日志计数
 * @author ydz
 */
public class MsgCountRecord {

	private static AtomicInteger count = new AtomicInteger();
	
	private static int maxTimes = 50000;
	
	
	public static boolean incrementAndGet(){
		if(count.get() >= maxTimes){
			return false;
		}else{
			count.incrementAndGet();
			return true;
		}
	}
	
}
