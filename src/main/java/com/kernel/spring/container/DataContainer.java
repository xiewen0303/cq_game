package com.kernel.spring.container;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
public class DataContainer {
	
	private ConcurrentMap<String, ConcurrentMap<String, Object>> componentsMap = new ConcurrentHashMap<String, ConcurrentMap<String,Object>>();
	
	/**
	 * 保存一个类型为 <b>T</b> 的数据对象
	 * @param component 所属组件
	 * @param dataId	数据对象id
	 * @param data		数据对象
	 */
	public <T> void putData(String component,String dataId,T data){
		getComponentDataMap(component).put(dataId, data);
	}
	
	/**
	 * 移除一个数据对象
	 * @param component 所属组件
	 * @param dataId	数据对象id
	 */
	public void removeData(String component,String dataId){
		getComponentDataMap(component).remove(dataId);
	}
	
	/**
	 * 获取一个类型为 <b>T</b> 的数据对象
	 * @param component 所属组件
	 * @param dataId	数据对象id
	 * @return 类型为 <b>T</b> 的数据对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(String component,String dataId){
		
		if(getComponentDataMap(component) == null){
			return null;
		}
		
		Object data = getComponentDataMap(component).get(dataId);
		if(null != data) return (T) data;
		
		return null;
	}
	
	private ConcurrentMap<String, Object> getComponentDataMap(String component){
		ConcurrentMap<String, Object> componentDataMap = componentsMap.get(component);
		if(null == componentDataMap){
			synchronized (this) {
				componentDataMap = componentsMap.get(component);
				if( null == componentDataMap){
					componentDataMap = new ConcurrentHashMap<String, Object>();
					componentsMap.put(component, componentDataMap);
				}
			}
		}
		return componentDataMap;
	}
	
	/**
	 * 获取组件数据大小
	 * @param component
	 * @return
	 */
	public int getComponentDataSize(String component){
		return getComponentDataMap(component).size();
	}
}
