/**
 * 
 */
package com.kernel.data.accessor;

import com.kernel.data.dao.IEntity;

/**
 * @description 实体元数据
 * @author ShiJie Chi
 * @created 2011-11-1下午3:16:33
 */
public class EntityMetaData {
	
	/**
	 * 
	 * @param
	 */
	public static String getEntityName(Class<? extends IEntity> entityClazz){
		
		return entityClazz.getSimpleName();
	}
	
	public static String getEntityName(IEntity entity){
		return getEntityName(entity.getClass());
	}

}
