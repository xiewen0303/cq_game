package com.hehj.easyexecutor.context;

/**
 * @description
 * 执行上下文
 * @author hehj
 * @created 2009-12-1 下午02:56:09
 */
public interface IEasyContext {

	public void setData(Object data);

	public <T> T getData();
	
	public void setSession(IEasySession session);
	
	public IEasySession getSession();
	
	public void writeOut(Object message);
}
