package com.junyou.bus.share.dao;

import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IEntity;

public abstract class BusAbsCacheDao<P extends IEntity> extends AbsDao<P>{

	@Override
	public String getAccessType() {
		return AccessType.getRoleCacheDbType();
	}
	
}
