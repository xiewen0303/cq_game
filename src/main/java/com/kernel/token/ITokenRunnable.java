package com.kernel.token;

public interface ITokenRunnable extends Runnable {

	/**
	 * 设置当前对象的token值
	 * @param token [identity,token]
	 */
	public void setToken(Object[] token);
	
}
