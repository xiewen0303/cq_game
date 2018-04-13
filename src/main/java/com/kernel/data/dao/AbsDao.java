/**
 * 
 */
package com.kernel.data.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.constants.GameConstants;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.accessor.AccessorManager;
import com.kernel.data.accessor.EntityCacheAccessor;
import com.kernel.data.accessor.GlobalIdentity;
import com.kernel.data.accessor.IIBatisDatabaseAccessor;
import com.kernel.data.cache.IEntityCache;
import com.kernel.sync.GlobalLockManager;
import com.kernel.sync.Lock;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-10-31上午11:33:11
 */
public abstract class AbsDao<P extends IEntity> implements IDaoContext,IParamDaoOperation<P>,IDaoOperation<P>,IAdvancedDaoOperation<P> {
	
	private Class<P> poClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	
	@Autowired
	private AccessorManager accessorManager;
	
	@Override
	public String getAccessType() {
		return accessorManager.getDefaultAccessType();
	}
	
	/**
	 * 实体，角色id索引变量名
	 */
	private static final String ENTITY_INDEX_USER_ROLE_ID = "userRoleId";

	/**
	 * 获取实体类型
	 * @param
	 */
	public Class<? extends IEntity> getEntityClass(){
		return poClass;
	}
	
	@Override
	public P load(Object primaryKey, Long identity,String accessType) {
		return (P)accessorManager.getAccessor(accessType).query(primaryKey, getEntityClass(), identity);
	}
	
	@Override
	public Object insert(P pojo, Long identity, String accessType) {
		return accessorManager.getAccessor(accessType).insert(pojo, identity,poClass);
	}

	@Override
	public int update(P pojo, Long identity, String accessType) {
		return accessorManager.getAccessor(accessType).update(pojo, identity);
	}

	@Override
	public int delete(Object primaryKey, Long identity,String accessType) {
		return accessorManager.getAccessor(accessType).delete(primaryKey, getEntityClass(), identity);
	}

	@Override
	public List<P> getRecords(QueryParamMap queryParams) {
		return getRecords(queryParams, GlobalIdentity.get(), getAccessType());
	}

	@Override
	public List<P> getRecords(QueryParamMap queryParams,
			Long identity, String accessType) {
		return (List<P>)((IIBatisDatabaseAccessor)accessorManager.getAccessor(accessType)).queryList(queryParams, getEntityClass());
	}

	@Override
	public List query(String statement, QueryParamMap queryParams) {
		return ((IIBatisDatabaseAccessor)accessorManager.getAccessor(AccessType.getDirectDbType())).customQuery(statement, queryParams);
	}

	
	public P cacheAsynLoad(Object primaryKey, Long userRoleId){
		P pojo = (P)cacheLoad(primaryKey, userRoleId);
		if(pojo == null){
			String identityKey = userRoleId == null ? GameConstants.DEFAULT_IDENTITY_KEY : userRoleId.toString();
			Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_DAO_ASYN_LOCK, identityKey);
			
			synchronized (lock) {
				pojo = (P)cacheLoad(primaryKey, userRoleId);
				
				if(AccessType.getRoleCacheDbType().equals(getAccessType()) && pojo == null){
					//是缓存数据访问类型,同时缓存中没有数据
					pojo = (P)load(primaryKey, userRoleId, AccessType.getDirectDbType());
					
					EntityCacheAccessor entityCacheAccessor = (EntityCacheAccessor)accessorManager.getAccessor(AccessType.getRoleCacheDbType());
					IEntityCache entityCache = entityCacheAccessor.getRoleCache(userRoleId);
					if(entityCache != null && pojo!=null){
						entityCache.addModelData(pojo, getEntityClass());
					}
				}
			}
		}
		return pojo;
	}
	
	@SuppressWarnings("unchecked")
	public List<P> cacheAsynLoadAll(Long userRoleId) {
		List<P> pojos = cacheLoadAll(userRoleId);
		
		if(pojos == null){
			String identityKey = userRoleId == null ? GameConstants.DEFAULT_IDENTITY_KEY : userRoleId.toString();
			Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_DAO_ASYN_LOCK, identityKey);
			
			synchronized (lock) {
				pojos = cacheLoadAll(userRoleId);
				
				if(AccessType.getRoleCacheDbType().equals(getAccessType()) && pojos == null){
					//是缓存数据访问类型,同时缓存中没有数据
					pojos = (List<P>)accessorManager.getAccessor(AccessType.getDirectDbType()).query(ENTITY_INDEX_USER_ROLE_ID, userRoleId, getEntityClass(), userRoleId);
					
					EntityCacheAccessor entityCacheAccessor = (EntityCacheAccessor)accessorManager.getAccessor(AccessType.getRoleCacheDbType());
					IEntityCache entityCache = entityCacheAccessor.getRoleCache(userRoleId);
					if(entityCache != null && pojos!=null){
						entityCache.addModelData(pojos, getEntityClass());
					}
				}
			}
		}
		
		return pojos;
	}
	
	@Override
	public P cacheLoad(Object primaryKey, Long userRoleId) {
		return load(primaryKey, userRoleId, getAccessType());
	}

	@Override
	public Object cacheInsert(P pojo, Long userRoleId) {
		return insert(pojo, userRoleId, getAccessType());
	}

	@Override
	public int cacheUpdate(P pojo, Long userRoleId) {
		return update(pojo, userRoleId, getAccessType());
	}

	@Override
	public int cacheDelete(Object primaryKey, Long userRoleId) {
		return delete(primaryKey, userRoleId, getAccessType());
	}

	@Override
	public List<P> cacheLoadAll(Long userRoleId) {
		
		if(!checkIdentity(userRoleId)){
			return null;
		}
		
		return (List<P>)accessorManager.getAccessor(getAccessType()).query(ENTITY_INDEX_USER_ROLE_ID, userRoleId, getEntityClass(), userRoleId);
	}

	@Override
	public void update(String statement, QueryParamMap queryParams) {
		((IIBatisDatabaseAccessor)accessorManager.getAccessor(AccessType.getDirectDbType())).customUpdate(statement, queryParams);
	}

	@Override
	public List<P> cacheLoadAll(Long userRoleId, IQueryFilter filter) {
		
		if(!checkIdentity(userRoleId)){
			return null;
		}
		
		return (List<P>)accessorManager.getAccessor(getAccessType()).query(ENTITY_INDEX_USER_ROLE_ID, userRoleId,filter, getEntityClass(), userRoleId);
	}
	
	/**
	 * 验证身份
	 */
	private boolean checkIdentity(Long userRoleId){
		if(null == userRoleId){
			return false;
		}
		
		return true;
	}

	@Override
	public void delete(String statement, QueryParamMap queryParams) {
		((IIBatisDatabaseAccessor)accessorManager.getAccessor(AccessType.getDirectDbType())).customDelete(statement, queryParams);
	}

	@Override
	public void changeCacheByIdentity(P pojo,long sourceRoleId,long targetRole){
		changeByIdentity(pojo, sourceRoleId, targetRole, getAccessType());
	}
	
	@Override
	public void changeByIdentity(P pojo,long sourceRoleId,long targetRole,String accessType){
		accessorManager.getAccessor(accessType).changeCacheByIdentity(pojo, sourceRoleId, targetRole);	
	}
	
	/**
	 * 删除缓存数据,不操作db
	 * @param entity
	 * @param userRoleId
	 * @return
	 */
	public boolean onlyAddCache(IEntity entity,long userRoleId){
		return	accessorManager.getAccessor(getAccessType()).onlyAddCache(entity, userRoleId); 
	}
	
	/**
	 * 只删除不操作db
	 * @param entity
	 * @param userRoleId
	 * @return
	 */
	public boolean onlyDeleteCache(IEntity entity,long userRoleId){
		return	accessorManager.getAccessor(getAccessType()).onlyDeleteCache(entity, userRoleId); 
	}
	
	/**
	 * 只操作db(异步操作)
	 * @param entity
	 * @param userRoleId
	 * @return
	 */
	public void onlyUpdateDB(IEntity entity,long identity){
		accessorManager.getAccessor(getAccessType()).onlyUpdateDB(entity, identity); 
	}
	
	/**
	 * 强制刷新角色缓存数据
	 * @param identity
	 */
	public void qzFlushCache(long identity){
		accessorManager.getAccessor(AccessType.getRoleCacheDbType()).qzFlush(identity);
	}
}
