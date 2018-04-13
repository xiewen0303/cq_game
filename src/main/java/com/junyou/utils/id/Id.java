package com.junyou.utils.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author DaoZheng Yuan
 * 2014-11-12 下午6:55:25
 */
public class Id{
	
	private String prefix;
	
	public Id(String prefix) {
		this.prefix = prefix;
	}
	
	private AtomicLong increase = new AtomicLong();
	
	public String nextString(){
		return new StringBuffer().append(prefix).append(Long.toHexString(increase.getAndIncrement())).toString();
	}
	
	public long nextLong(){
		return increase.getAndIncrement();
	}
	
}
