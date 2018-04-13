package com.junyou.configure.dao;

import java.util.List;

import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.IQueryFilter;

public interface IConfigureDao {

	public void deleteAll(Class<? extends IEntity> entityType);

	public void insert(IEntity configureEntity);

	public <T extends IEntity> T get(Class<? extends IEntity> entityType, Object id);

	public <T extends IEntity> List<T> loadAll(Class<? extends IEntity> entityType);

	public <T extends IEntity> List<T> loadAll(Class<? extends IEntity> entityType,
			IQueryFilter<? extends IEntity> filter);

	public void deleteById(Class<? extends IEntity> entityType, Object id);
	

}
