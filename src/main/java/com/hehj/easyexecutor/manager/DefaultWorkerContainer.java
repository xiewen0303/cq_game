package com.hehj.easyexecutor.manager;

public class DefaultWorkerContainer implements IEasyWorkerContainer {

	@Override
	public Object getWorker(String workerName) {
		throw new UnsupportedOperationException("DefaultWorkerContainer.getWorker(String workerName)");
	}

	@Override
	public Object getWorker(Class<?> workerClass) {
		Object o;
		try {
		
			o = workerClass.newInstance();
		
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		
		} 
		
		return o;
	}

}
