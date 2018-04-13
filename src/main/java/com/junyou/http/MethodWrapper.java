package com.junyou.http;

import java.lang.reflect.Method;

import com.junyou.http.key.KeyEnum;

/**
 * @author DaoZheng Yuan
 * 2014年11月25日 下午2:37:00
 */
public class MethodWrapper {

	private Method method;
	private Object instance;
	private Class<?> paramClazz;// 参数类型
	
	private KeyEnum keyEnum;

	public MethodWrapper(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
		this.paramClazz = method.getParameterTypes()[0];
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Class<?> getParamClazz() {
		return paramClazz;
	}

	public void setParamClazz(Class<?> paramClazz) {
		this.paramClazz = paramClazz;
	}

	public KeyEnum getKeyEnum() {
		return keyEnum;
	}

	public void setKeyEnum(KeyEnum keyEnum) {
		this.keyEnum = keyEnum;
	}

}
