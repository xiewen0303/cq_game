package com.kernel.data.dao;

import java.util.List;

public abstract class AbsFileDbDaoOperationAdapter<P extends IEntity> implements IDaoOperation<P> {

	@Override
	public P cacheLoad(Object primaryKey, Long userRoleId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("filedb : cacheload");
	}

	@Override
	public List<P> cacheLoadAll(Long userRoleId, IQueryFilter filter) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("filedb : cacheLoadAll");
	}

	@Override
	public int cacheUpdate(P pojo, Long userRoleId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("filedb : cacheUpdate");
	}

	@Override
	public int cacheDelete(Object primaryKey, Long userRoleId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("filedb : cacheDelete");
	}
	
	

}
