/**
 * 
 */
package com.kernel.data.cache.model;

import java.util.List;

import com.kernel.data.dao.IEntity;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-3下午4:04:19
 */
public class EntityCacheModelFactory{

	private static EntityCacheModelFactory factory = new EntityCacheModelFactory();
	
	/**
	 * 获取工厂实例
	 * @param
	 */
	public static EntityCacheModelFactory getInstance(){
		return factory;
	}

	/**
	 * 
	 * @param data 实体集合
	 */
	public EntityCacheModel create(List<? extends IEntity> data) {
		return new EntityCacheModel(data);
	}

}
