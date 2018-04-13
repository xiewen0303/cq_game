/**
 * 
 */
package com.junyou.configure.dao.accesser;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.kernel.data.accessor.IDbAccessor;
import com.kernel.data.cache.CacheManager;
import com.kernel.data.cache.IEntityCache;
import com.kernel.data.cache.IEntityCacheReader;
import com.kernel.data.cache.model.EntityCacheModel;
import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.IQueryFilter;

/**
 * @description 实体缓存访问器
 * @author ShiJie Chi
 * @created 2011-11-4下午1:50:54
 */
public class ConfigureCacheAccessor implements IDbAccessor {

	@Autowired
	@Qualifier("configureCacheManager")
	private CacheManager cacheManager;
	
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

	@Override
	public void changeCacheByIdentity(IEntity entity, long sourceRoleId,
			long targetRole) {
		
	}

	@Override
	public boolean onlyAddCache(IEntity entity, long userRoleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onlyDeleteCache(IEntity entity, long userRoleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onlyUpdateDB(IEntity entity, long identity) {
		// TODO Auto-generated method stub
	}
	
	public void qzFlush(long userRoleId){
		// TODO Auto-generated method stub
	}
}
