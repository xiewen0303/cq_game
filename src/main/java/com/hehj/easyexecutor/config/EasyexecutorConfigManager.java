package com.hehj.easyexecutor.config;

import java.util.List;

import org.apache.commons.digester.Digester;

public class EasyexecutorConfigManager implements IEasyexecutorMeta{

	private EasyexecutorConfig easyConfig;
	
	public EasyexecutorConfigManager(String configFilePath) {

		Digester digester = new Digester();
		digester.setValidating(false);
		
		digester.addObjectCreate("easyexecutorconfig", EasyexecutorConfig.class);
		digester.addCallMethod("easyexecutorconfig/worker-scan","addScanPackage",0);
//		digester.addCallMethod("easyexecutorconfig/worker-global-interceptor","addGlobalInterceptor",0);
//		digester.addCallMethod("easyexecutorconfig/worker-global-interceptor","addGlobalInterceptor",0);
		digester.addObjectCreate("easyexecutorconfig/worker-global-interceptor",InterceptorConfig.class);
		digester.addSetProperties("easyexecutorconfig/worker-global-interceptor");
		digester.addSetNext("easyexecutorconfig/worker-global-interceptor","addGlobalInterceptor");
//		digester.addCallMethod("easyexecutorconfig/worker-global-interceptor/class","setClassName",0);
//		digester.addCallMethod("easyexecutorconfig/worker-global-interceptor/spring-bean","setSpringBean",0);
		try {
			easyConfig = (EasyexecutorConfig)digester.parse(EasyexecutorConfigManager.class.getResourceAsStream("/"+configFilePath));
		} catch (Exception e) {
			throw new RuntimeException("failed to parse ' "+configFilePath+" ',check it",e);
		}

	}

	@Override
	public boolean isScanPackage(String packageName) {
		
		for(String s : easyConfig.getScanPackages()){
			if(packageName.startsWith(s)) return true;
		}
		return false;
		
	}

	@Override
	public List<InterceptorConfig> globalInterceptors() {
		return easyConfig.getGlobalInterceptorClasses();
	}
	
}
