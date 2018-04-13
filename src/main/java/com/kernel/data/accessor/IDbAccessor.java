/**
 * 
 */
package com.kernel.data.accessor;

import java.util.List;
import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.IQueryFilter;

/**
 * @description 数据访问接口
 * @author ShiJie Chi
 * @created 2011-10-31上午9:59:31
 */
public interface IDbAccessor {

	/**
	 * 插入数据
	 * @param entity 实体
	 * @param identity 访问身份
	 */
	Object insert(IEntity entity, Long identity,Class<? extends IEntity> clazz);

	/**
	 * 查询
	 * @param primaryKey 主键
	 * @param clazz 实体类型
	 * @param Identity 访问身份
	 */
	IEntity query(Object primaryKey,Class<? extends IEntity> clazz, Long identity);
	
	/**
	 * 查询多条记录
	 * @param indexName 索引名称(对应实体中变量)
	 * @param indexValue 索引值
	 * @param identity 访问身份
	 */
	List<IEntity> query(String indexName,Object indexValue,Class<? extends IEntity> clazz, Long identity);
	
	/**
	 * 
	 * @param
	 */
	List<IEntity> query(String indexName,Object indexValue,IQueryFilter filter,Class<? extends IEntity> clazz, Long identity);
	
	/**
	 * 删除数据
	 * @param primaryKey 主键
	 * @param clazz 实体类型
	 * @param identity 访问身份
	 */
	int delete(Object primaryKey, Class<? extends IEntity> clazz, Long identity);

	/**
	 * 更新数据
	 * @param entity 实体
	 * @param identity 访问身份
	 */
	int update(IEntity entity, Long identity);
	
	/**
	 * 交换数据,暂时仅在EntityCacheAccessor里面操作，其它的子类若需要,请再加具体的业务逻辑
	 * @param entity
	 * @param sourceRoleId
	 * @param targetRole
	 */
	public void changeCacheByIdentity(IEntity entity,long sourceRoleId,long targetRole);
	/**
	 * 删除缓存数据,不操作db
	 * @param entity
	 * @param userRoleId
	 * @return
	 */
	public boolean onlyAddCache(IEntity entity,long userRoleId);
	
	/**
	 * 只删除不操作db
	 * @param entity
	 * @param userRoleId
	 * @return
	 */
	public boolean onlyDeleteCache(IEntity entity,long userRoleId);
	
	/**
	 *  只操作db(异步操作)
	 * @param entity
	 * @param identity  主键id
	 * @return
	 */
	public void onlyUpdateDB(IEntity entity,long identity);
	
	/**
	 * 强制刷新角色缓存数据
	 * @param userRoleId
	 */
	public void qzFlush(long userRoleId);
 
}
