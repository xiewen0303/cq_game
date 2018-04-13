package com.kernel.token;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author DaoZheng Yuan
 * 2014年12月3日 上午11:05:35
 */
public class TokenManager {

	
	private ConcurrentMap<String, ConcurrentMap<Object, Long>> componentsTokenMap = new ConcurrentHashMap<>();
	

	/**
	 * 创建一个新的token
	 * @param component token所属模块
	 * @param identity token所属实体标志
	 * @return
	 */
	public Long createToken(String component,Object identity){
		
		ConcurrentMap<Object, Long> componentTokens = getComponentTokens(component);
		synchronized (componentTokens){	
			Long token = GameSystemTime.getSystemMillTime();
			componentTokens.put(identity, token);
			
			return token;
		}
		
	}

	/**
	 * 移除一个指定的token
	 * @param component token所属模块
	 * @param identity token所属实体标志
	 */
	public void removeToken(String component,Object identity){

		ConcurrentMap<Object, Long> componentTokens = getComponentTokens(component);
		synchronized(componentTokens){
			componentTokens.remove(identity);
		}
		
	}
	
	/**
	 * 校验一个token的值是否有效
	 * @param tokenVaule 要校验的值
	 * @param component token所属模块
	 * @param identity token所属实体标志
	 * @return true:有效
	 */
	public Boolean checkToken(Long tokenVaule,String component,Object identity){
		ConcurrentMap<Object, Long> componentTokens = getComponentTokens(component);
		synchronized (componentTokens) {
			Long curToken = componentTokens.get(identity);
			if(null == curToken){
				return false;
			}
			return curToken.equals(tokenVaule);
		}
	}
	
	private ConcurrentMap<Object, Long> getComponentTokens(String component){
		ConcurrentMap<Object, Long> componentTokens = componentsTokenMap.get(component);
		if(null == componentTokens){
			synchronized (componentsTokenMap) {
				componentTokens = componentsTokenMap.get(component);
				if( null == componentTokens){
					componentTokens = new ConcurrentHashMap<Object, Long>();
					componentsTokenMap.put(component, componentTokens);
				}
			}
		}
		return componentTokens;
	}
	
}
