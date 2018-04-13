/**
 * 
 */
package com.kernel.data.cache;


/**
 * @description 角色缓存创建工厂
 * @author ShiJie Chi
 * @created 2011-11-2下午2:10:40
 */
public class EntityCacheFactory {

	/**
	 * 创建角色缓存
	 * @param userRoleId 角色唯一标识
	 * @return 
	 */
	public static IEntityCache create(Long userRoleId) {
		
		return new EntityCache(userRoleId);
	}

}
