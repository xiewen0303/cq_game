/**
 * 
 */
package com.kernel.data.cache;

import com.kernel.data.cache.model.EntityCacheModel;
import com.kernel.data.dao.IEntity;

/**
 * @description 角色缓存读取接口
 * @author ShiJie Chi
 * @created 2011-11-2下午1:58:44
 */
public interface IEntityCacheReader {
	
	/**
	 * 获取模块
	 * @param modelEntityClass 模块对应实体类型
	 */
	public EntityCacheModel getModel(Class<? extends IEntity> modelEntityClass);

}
