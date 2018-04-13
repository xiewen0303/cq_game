package com.kernel.pool.executor;


public interface IRunnablePool {

	public Runnable getRunnable(Message message); 
	
}
