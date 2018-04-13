/**
 * 
 */
package com.kernel.data.accessor;

import java.util.List;

import com.kernel.data.cache.CacheManager;
import com.kernel.data.cache.IEntityCache;
import com.kernel.data.cache.IEntityCacheReader;
import com.kernel.data.cache.model.EntityCacheModel;
import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.data.write.async_1.AsyncWriteManager;

/**
 * @description 实体缓存访问器
 * @author ShiJie Chi
 * @created 2011-11-4下午1:50:54
 */
public class EntityCacheAccessor implements IDbAccessor {
	
	private CacheManager cacheManager;
	
	private AsyncWriteManager asyncWriteManager;
	
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setAsyncWriteManager(AsyncWriteManager asyncWriteManager) {
		this.asyncWriteManager = asyncWriteManager;
	}
	
	public IEntityCache getRoleCache(Long roleIdentity){
		return cacheManager.getRoleCache(roleIdentity);
	}
	
	public void changeCacheByIdentity(IEntity entity,long sourceRoleId,long targetRole){
		Long identity = entity.getPrimaryKeyValue();//物品唯一Id
		IEntityCache  sourceCache=cacheManager.getRoleCache(sourceRoleId);
		//删除之前的玩家缓存队列
		IEntity deleteEntity=sourceCache.deleteCacheEntity(identity,entity.getClass());
		
		IEntityCache targetCache=cacheManager.getRoleCache(targetRole);
		boolean addFlag=targetCache.addCacheEntity(entity,entity.getClass());
		
		asyncWriteManager.getDataContainer(identity).update(entity);
	}

	@Override
	public Object insert(IEntity entity, Long identity,Class<? extends IEntity> clazz) {
		
		boolean result = false;
		
		IEntityCache roleCache = cacheManager.getRoleCache(identity);
		IEntityCacheReader reader = (IEntityCacheReader)roleCache;
		
		EntityCacheModel dataModel = reader.getModel(entity.getClass());
		if(null == dataModel){
			//初始化模块和数据
			roleCache.addModelData(entity, clazz);
			
			result = true;
		}else{
			result = dataModel.insert(entity);
		}
		
		asyncWriteManager.getDataContainer(identity).insert(entity);
		return entity;
	}

	@Override
	public IEntity query(Object primaryKey, Class<? extends IEntity> clazz, Long identity) {
		
		IEntityCache roleCache = cacheManager.getRoleCache(identity);
		IEntityCacheReader reader = (IEntityCacheReader)roleCache;
		
		EntityCacheModel dataModel = reader.getModel(clazz);
		if(null != dataModel){
			return dataModel.load(primaryKey);
		}
		
		return null;
	}
	
	@Override
	public int update(IEntity entity, Long identity) {
		
		boolean result = false;
		
		IEntityCache roleCache = cacheManager.getRoleCache(identity);
		IEntityCacheReader reader = (IEntityCacheReader)roleCache;
		
		EntityCacheModel dataModel = reader.getModel(entity.getClass());
		
		if(null != dataModel){
			result = dataModel.update(entity);
		}
		
		asyncWriteManager.getDataContainer(identity).update(entity);
		
		return result ? 1 : 0;
	}
	
	@Override
	public int delete(Object primaryKey, Class<? extends IEntity> class1, Long identity) {
		
		boolean result = false;
		
		IEntityCache roleCache = cacheManager.getRoleCache(identity);
		IEntityCacheReader reader = (IEntityCacheReader)roleCache;
		
		EntityCacheModel dataModel = reader.getModel(class1);
		
		IEntity entity = null;
		if(null != dataModel){
			entity = dataModel.delete(primaryKey);
			result = (null != entity);
		}
		
		asyncWriteManager.getDataContainer(identity).delete(entity);
		
		return result ? 1 : 0;
	}

	@Override
	public List<IEntity> query(String indexName, Object indexValue,
			Class<? extends IEntity> clazz, Long identity) {
		
		IEntityCache roleCache = cacheManager.getRoleCache(identity);
		IEntityCacheReader reader = (IEntityCacheReader)roleCache;
		
		EntityCacheModel dataModel = reader.getModel(clazz);
		if(null != dataModel){
			return dataModel.loadAll();
		}
		
		return null;
	}

	@Override
	public List<IEntity> query(String indexName, Object indexValue,
			IQueryFilter filter, Class<? extends IEntity> clazz, Long identity) {
		IEntityCache roleCache = cacheManager.getRoleCache(identity);
		IEntityCacheReader reader = (IEntityCacheReader)roleCache;
		
		EntityCacheModel dataModel = reader.getModel(clazz);
		if(null != dataModel){
			return dataModel.loadAll(filter);
		}
		
		return null;
	}

	/**
	 * 删除缓存数据,不操作db
	 * @param entity
	 * @param userRoleId
	 * @return
	 */
	public boolean onlyAddCache(IEntity entity,long userRoleId){  
		IEntityCache targetCache=cacheManager.getRoleCache(userRoleId);
		
		return targetCache.addCacheEntity(entity,entity.getClass()); 
		
	}
	
	/**
	 * 只删除不操作db
	 * @param entity
	 * @param userRoleId
	 * @return
	 */
	public boolean onlyDeleteCache(IEntity entity,long userRoleId){
		boolean flag=false;
		//删除之前的玩家缓存队列
		Long identity = entity.getPrimaryKeyValue();//物品唯一Id 
		IEntityCache  sourceCache=cacheManager.getRoleCache(userRoleId); 
		IEntity deleteEntity=sourceCache.deleteCacheEntity(identity,entity.getClass());
		if(deleteEntity!=null){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 强制刷新角色缓存数据
	 * @param userRoleId
	 */
	public void qzFlush(long userRoleId){
		asyncWriteManager.getDataContainer(userRoleId).flush();
	}
	
	/**
	 *  只操作db(异步操作)
	 * @param entity
	 * @param identity  主键id
	 * @return
	 */
	public void onlyUpdateDB(IEntity entity,long identity){
		asyncWriteManager.getDataContainer(identity).update(entity);
	}
}
