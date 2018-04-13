/**
 * 
 */
package com.kernel.data.cache;

import java.util.List;

import com.kernel.data.dao.IEntity;

/**
 * @description 角色缓存
 * @author ShiJie Chi
 * @created 2011-11-2下午1:56:52
 */
public interface IEntityCache {
	
	/**
	 * 获取角色身份(userRoleId角色唯一标志)
	 * @param
	 */
	public Long getRoleIdentity();
	
	/**
	 * 激活该对象
	 */
	public void activate();
	
	/**
	 * 冻结该对象
	 */
	public void freeze();
	
	public void setCleanGap(Long cleanGap);
	
	/**
	 * 判定该对象是否可以清除
	 * @return true:可以清除
	 */
	public boolean canClean();
	
	/**
	 * 初始化模块数据<br/>
	 * @param entitys 单表对应的实体集合
	 * @param clazz 实体类型
	 */
	public void addModelData(List<? extends IEntity> entitys, Class<? extends IEntity> clazz);
	
	/**
	 * 初始化模块数据
	 * @param entity 单表对应的实体
	 * @param clazz 实体类型
	 */
	public void addModelData(IEntity entity, Class<? extends IEntity> clazz);
	
	/**
	 * 只删除缓存Entity，不操作db
	 * @param identity
	 * @param clazz
	 */
	public IEntity deleteCacheEntity(Long identity,
			Class<? extends IEntity> clazz);
	
	/**
	 * 只添加缓存，不操作DB
	 * @param entity
	 * @param clazz
	 */
	public boolean addCacheEntity(IEntity entity, Class<? extends IEntity> clazz);
	
}
