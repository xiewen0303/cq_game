package com.hehj.easyexecutor.config;

import java.util.List;

public interface IEasyexecutorMeta {

	public boolean isScanPackage(String packageName);
	
	public List<InterceptorConfig> globalInterceptors();
}
