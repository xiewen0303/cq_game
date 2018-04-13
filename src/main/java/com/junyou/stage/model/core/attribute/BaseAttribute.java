package com.junyou.stage.model.core.attribute;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.constants.EffectType;


/**
 * 
 * @description 基础属性
 *
 * @author LiuJuan
 * @created 2011-12-5 下午02:25:26
 */
public class BaseAttribute implements IBaseAttribute {
	

	/**
	 * 属性
	 */
	private Map<String,Long> attributes = null;
	
	/**
	 * 获取某个属性类型值
	 * @param attributeType {@link EffectType}
	 */
	public long get(String attributeType){
		
		attribteCheck(attributeType);
		
		if(null == attributes) return 0;
		
		Long val =  attributes.get(attributeType);
		
		return null != val ? val : 0;
	}

	/**
	 * 设置某个属性类型值
	 * @param attributeType {@link EffectType}
	 */
	public void set(String attributeType, long val){
		
		attribteCheck(attributeType);
		
		if(null == attributes){
			attributes = new HashMap<>();
		}
		
		attributes.put(attributeType, val);
	}
	
	
	/**
	 * 转换成map
	 * @param
	 */
	public Map<String,Long> toMap(){
		
		if(null == attributes) return null;
		
		return attributes;
	}
	
	/**
	 * @description 属性合法性检测
	 * @param attributeType
	 */
	private void attribteCheck(String attributeType){
		EffectType.checkContains(attributeType);
	}
}
