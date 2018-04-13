/**
 * 
 */
package com.kernel.data.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.cache.model.EntityCacheModel;
import com.kernel.data.cache.model.EntityCacheModelFactory;
import com.kernel.data.dao.IEntity;

/**
 * @description 角色缓存
 * @author ShiJie Chi
 * @created 2011-10-31下午3:40:15
 */
public class EntityCache implements IEntityCache,IEntityCacheReader {
	
	/**
	 * 角色身份(userRoleId角色唯一标志)
	 */
	private Long roleIdentity;
	
	private boolean activate = true;
	
	private Long cleanGap = 1l;
	private long freezeTime = GameSystemTime.getSystemMillTime();
	
	/**
	 * 模块集合
	 */
	private Map<Class<? extends IEntity>,EntityCacheModel> roleDataModels = new HashMap<Class<? extends IEntity>, EntityCacheModel>();
	
	/**
	 * @param roleIdentity 角色身份(userRoleId角色唯一标志)
	 */
	public EntityCache(Long roleIdentity) {
		this.roleIdentity = roleIdentity;
	}
	
	public Long getRoleIdentity() {
		return roleIdentity;
	}
	
	public void addModelData(List<? extends IEntity> entitys, Class<? extends IEntity> clazz){
		
		EntityCacheModel roleModel = EntityCacheModelFactory.getInstance().create(entitys);
		roleDataModels.put(clazz, roleModel);
	}
	
	public EntityCacheModel getModel(Class<? extends IEntity> clazz){
		return roleDataModels.get(clazz);
	}

	@Override
	public void addModelData(IEntity entity, Class<? extends IEntity> clazz) {
		
		List<IEntity> entitys = new ArrayList<IEntity>();
		entitys.add(entity);
		
		addModelData(entitys, clazz);
	}

	@Override
	public void activate() {
		this.activate = true;
	}

	@Override
	public void freeze() {
		this.activate = false;
		this.freezeTime = GameSystemTime.getSystemMillTime();
	}

	@Override
	public boolean canClean() {
		return !this.activate && ( (GameSystemTime.getSystemMillTime() - freezeTime) > cleanGap);
	}

	@Override
	public void setCleanGap(Long cleanGap) {
		this.cleanGap = cleanGap;
	}

	@Override
	public IEntity deleteCacheEntity(Long identity,
			Class<? extends IEntity> clazz) {
		EntityCacheModel cacheModel=roleDataModels.get(clazz);
		return cacheModel.delete(identity);
	}

	@Override
	public boolean addCacheEntity(IEntity entity, Class<? extends IEntity> clazz) {
		boolean result = false;
		
		EntityCacheModel dataModel=roleDataModels.get(clazz); 
		if(null == dataModel){
			addModelData(entity, clazz);
			result = true;
		}else{
			result = dataModel.insert(entity);
		}
		return result;
	}
}
