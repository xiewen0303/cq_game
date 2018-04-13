package com.kernel.sync;

import com.junyou.utils.datetime.GameSystemTime;

public class Lock {

	private long timestamp = GameSystemTime.getSystemMillTime();
	private static final long idle = 60 * 60 * 1000; 
	
	private String key;
	
	public Lock(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}

	public void update(){
		timestamp = GameSystemTime.getSystemMillTime();
	}
	
	public boolean canClean(){
		return (GameSystemTime.getSystemMillTime() - timestamp) > idle;
	}
}
