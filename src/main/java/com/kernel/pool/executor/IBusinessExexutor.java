package com.kernel.pool.executor;

import java.util.Map;


public interface IBusinessExexutor {

	public void execute(Runnable runnable,RouteInfo routeInfo);
	
	public void addExecutorGroup(String name,int size);
	
	public Map<String, Map<String, Integer>> getExecutorInfos();
}
