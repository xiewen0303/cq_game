package com.junyou.configure.loader;

import org.springframework.stereotype.Component;

import com.junyou.configure.export.impl.ConfigureContext;
import com.kernel.data.cache.EntityCache;
import com.kernel.data.cache.IEntityCache;
import com.kernel.data.cache.IEntityCacheLoader;

@Component("configureCacheLoader")
public class ConfigureCacheLoader implements IEntityCacheLoader {

	@Override
	public IEntityCache loadEntityCache(Long identity) {
		return new EntityCache(ConfigureContext.CONFIGURE_IDENTITY);
	}

}
