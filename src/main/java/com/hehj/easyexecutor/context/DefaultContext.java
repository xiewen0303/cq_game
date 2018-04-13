package com.hehj.easyexecutor.context;


public class DefaultContext implements IEasyContext {

	private Object data;
	
	@Override
	public IEasySession getSession() {
		throw new UnsupportedOperationException("DefaultContext.getSession");
	}

	@Override
	public void setSession(IEasySession session) {
		throw new UnsupportedOperationException("DefaultContext.setSession");
	}

	@Override
	public void writeOut(Object message) {
		throw new UnsupportedOperationException("DefaultContext.writeOut");
	}
	
	public void setData(Object data){
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getData() {
		return (T)data;
	}

}
