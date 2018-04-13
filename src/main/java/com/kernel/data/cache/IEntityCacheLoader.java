package com.kernel.data.cache;

public interface IEntityCacheLoader {

	/**
	 * 初始化装载指定id的缓存对象
	 * @param identity
	 * @return
	 */
	public IEntityCache loadEntityCache(Long identity);
	
}
