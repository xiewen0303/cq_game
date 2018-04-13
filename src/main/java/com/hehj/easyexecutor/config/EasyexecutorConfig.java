package com.hehj.easyexecutor.config;

import java.util.ArrayList;
import java.util.List;


public class EasyexecutorConfig {

	private List<String> scanPackages = new ArrayList<String>();
	private List<InterceptorConfig> globalInterceptorClasses = new ArrayList<InterceptorConfig>();
	
	public List<String> getScanPackages(){
		return scanPackages;
	}
	
	public void addScanPackage(String scanPackage){
		scanPackages.add(scanPackage);
	}
	
	public void addGlobalInterceptor(InterceptorConfig interceptor){
		globalInterceptorClasses.add(interceptor);
	}

	public List<InterceptorConfig> getGlobalInterceptorClasses() {
		return globalInterceptorClasses;
	}
	
}
