/**
 * 
 */
package com.kernel.data.cache.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.IQueryFilter;

/**
 * @description 默认缓存模型
 * @author ShiJie Chi
 * @created 2011-11-3下午3:31:24
 */
public class EntityCacheModel {

	private Map<Long,IEntity> entitys = new ConcurrentHashMap<Long, IEntity>();
	
	/**
	 * 
	 */
	public EntityCacheModel(List<? extends IEntity> entityData) {
		if(null != entityData){
			for(IEntity entity : entityData){
				this.entitys.put(entity.getPrimaryKeyValue(),entity);
			}
		}
	}
	
	public IEntity load(Object primaryKey) {
		return this.entitys.get(primaryKey);
	}
	
	public boolean insert(IEntity entity) {
		
		IEntity result = this.entitys.get(entity.getPrimaryKeyValue());
		
		if(null != result){
			
			return false;
			
		}
		
		this.entitys.put(entity.getPrimaryKeyValue(), entity);
		
		return true;
	}

	public boolean update(IEntity entity) {
		
		IEntity result = this.entitys.get(entity.getPrimaryKeyValue());
		
		if(null == result){
			
			return false;
			
		}
		
		this.entitys.put(entity.getPrimaryKeyValue(), entity);
		
		return true;
	}

	public IEntity delete(Object primaryKey) {
		
		IEntity result = entitys.remove(primaryKey);
		
		return result;
	}

	public List<IEntity> loadAll() {
		return new ArrayList<IEntity>(entitys.values());
	}
	
	public List<IEntity> loadAll(IQueryFilter filter){
		
		if(null != entitys && entitys.size() > 0){
			
			List<IEntity> result = null;
			for(IEntity tmp : entitys.values()){
				
				if(!filter.stopped()){
					if(filter.check(tmp)){
						if(null == result){
							result = new ArrayList<IEntity>();
						}
						
						result.add(tmp);
					}
				}else{
					break;
				}
			}
			
			return result;
		}
		
		
		return null;
		
		
	}

}
