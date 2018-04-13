package com.hehj.easyexecutor.Interceptor;


public interface IInterceptor {

	/**
	 * @param context
	 * @return true:继续流程;false：流程中断
	 */
	public boolean before(Object message);
	
	/**
	 * @param context
	 * @return true:继续流程;false：流程中断
	 */
	public boolean after(Object message);
}
