/**
 * 
 */
package com.kernel.data.accessor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @description 访问入口对象管理器
 * @author ShiJie Chi
 * @created 2011-11-7下午3:44:48
 */
public class AccessorManager implements ApplicationContextAware{

	private ApplicationContext applicationContext;
	
	/**
	 * 默认db类型
	 */
	private String defaultAccessType;
	
	/**
	 * 访问对象集合
	 */
	public Map<String,String> accessors = new HashMap<String, String>();

	public void setAccessors(Map<String, String> accessors) {
		this.accessors = accessors;
	}
	
	public void setDefaultDbType(String defaultDbType) {
		this.defaultAccessType = defaultDbType;
	}

	/**
	 * 获取访问对象
	 * @param type 访问器类型 {@link AccessType}
	 */
	public IDbAccessor getAccessor(String type){
		
		IDbAccessor accessor = applicationContext.getBean(accessors.get(type),IDbAccessor.class);
		
		if(null == accessor){
			throw new NullPointerException("no accessor type:" + type);
		}
		
		return accessor;
	}
	
	/**
	 * 获取默认db类型
	 * @param
	 */
	public String getDefaultAccessType(){
		return defaultAccessType;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
