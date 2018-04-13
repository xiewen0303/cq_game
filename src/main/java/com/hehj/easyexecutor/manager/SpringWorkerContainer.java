package com.hehj.easyexecutor.manager;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringWorkerContainer implements IEasyWorkerContainer,ApplicationContextAware {

	private ApplicationContext applicationContext; 
	
	public SpringWorkerContainer(){}
	
	public SpringWorkerContainer(ApplicationContext applicationContext) {
		if(null==applicationContext) throw new NullPointerException("applicationContext is null.");
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object getWorker(String workerName) {
		return applicationContext.getBean(workerName);
	}

	@Override
	public Object getWorker(Class<?> workerClass) {
		return applicationContext.getBean(workerClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext = arg0;
	}

}
