package com.junyou.kuafu.share.tunnel;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.junyou.log.ChuanQiLog;

public abstract class AbsKuafuNetTunnelPool<T> {
	private final GenericObjectPool internalPool;
	private final GenericObjectPool.Config poolConfig;

	public AbsKuafuNetTunnelPool(final GenericObjectPool.Config poolConfig,
			PoolableObjectFactory factory) {
		this.internalPool = new GenericObjectPool(factory, poolConfig);
		this.poolConfig = poolConfig;
	}

	public GenericObjectPool.Config getPoolConfig() {
		return poolConfig;
	}
	
	public int getNumActive(){
		if(internalPool == null){
			return 0;
		}
		return internalPool.getNumActive();
	}
	
	public int getNumIdle(){
		if(internalPool == null){
			return 0;
		}
		return internalPool.getNumIdle();
	}

	@SuppressWarnings("unchecked")
	public T getResource() {
		try {
			return (T) internalPool.borrowObject();
		} catch (Exception e) {
			ChuanQiLog.error("can not get resource", e);
			return null;
		}
	}

	public void returnResource(final T resource) {
		try {
			internalPool.returnObject(resource);
		} catch (Exception e) {
			ChuanQiLog.error("can not return resource", e);
		}
	}

	public void returnBrokenResource(final T resource) {
		try {
			internalPool.invalidateObject(resource);
		} catch (Exception e) {
			ChuanQiLog.error("can not return broken resource", e);
		}
	}

	public void destroy() {
		try {
			internalPool.close();
		} catch (Exception e) {
			ChuanQiLog.error("can not destroy pool", e);
		}
	}
}