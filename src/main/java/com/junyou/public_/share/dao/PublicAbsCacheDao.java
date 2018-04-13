package com.junyou.public_.share.dao;

import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IEntity;

public abstract class PublicAbsCacheDao<P extends IEntity> extends AbsDao<P> {

	@Override
	public String getAccessType() {
		return AccessType.getPublicCacheDbType();
	}
	
}
