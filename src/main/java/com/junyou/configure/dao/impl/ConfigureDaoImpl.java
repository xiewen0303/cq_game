package com.junyou.configure.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.configure.dao.IConfigureDao;
import com.junyou.configure.export.impl.ConfigureContext;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.accessor.AccessorManager;
import com.kernel.data.accessor.IDbAccessor;
import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.IQueryFilter;


@Component
public class ConfigureDaoImpl implements IConfigureDao{
	
	@Autowired
	ConfigureContext configureContext;
	
	@PostConstruct
	public void init(){
		configureContext.init();
	}
	
	@Autowired
	private AccessorManager accessorManager;
	
	public IDbAccessor getAccessor() {
		return accessorManager.getAccessor(AccessType.getConfigureCacheDbType());
	}

	@Override
	public void deleteAll(Class<? extends IEntity> entityType) {
		
		List<IEntity> entitys = loadAll(entityType);
		
		if(null != entitys){
			for(IEntity entity : entitys){
				getAccessor().delete(entity.getPrimaryKeyValue(), entityType, configureContext.CONFIGURE_IDENTITY);
			}
		}
	}

	@Override
	public void deleteById(Class<? extends IEntity> entityType, Object id) {
		getAccessor().delete(id, entityType, configureContext.CONFIGURE_IDENTITY);
	}

	@Override
	public void insert(IEntity configureEntity) {
		getAccessor().insert(configureEntity, configureContext.CONFIGURE_IDENTITY, configureEntity.getClass());
	}

	@Override
	public <T extends IEntity> T get(Class<? extends IEntity> entityType,
			Object id) {
		return (T)getAccessor().query(id, entityType, configureContext.CONFIGURE_IDENTITY);
	}

	@Override
	public <T extends IEntity> List<T> loadAll(
			Class<? extends IEntity> entityType) {
		return (List<T>)getAccessor().query(null, null, entityType, configureContext.CONFIGURE_IDENTITY);
	}

	@Override
	public <T extends IEntity> List<T> loadAll(
			Class<? extends IEntity> entityType, IQueryFilter<? extends IEntity> filter) {
		return (List<T>)getAccessor().query(null, null, filter, entityType, configureContext.CONFIGURE_IDENTITY);
	}
	
	
	
	

	
}
