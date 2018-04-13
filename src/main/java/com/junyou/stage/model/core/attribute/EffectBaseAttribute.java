package com.junyou.stage.model.core.attribute;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.constants.EffectType;


/**
 * 
 * @description 效果
 *
 * @author LiuJuan
 * @created 2011-12-5 下午02:43:11
 */
public class EffectBaseAttribute implements IBaseAttribute {
	

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
		if(null == attributes.get(attributeType)) return 0;
		
		return attributes.get(attributeType);
	}

	/**
	 * 设置某个属性类型值
	 * @param attributeType {@link EffectType}
	 */
	public void set(String attributeType, long val){
		
		attribteCheck(attributeType);
		
		if(null == attributes){
			attributes = new HashMap<String, Long>();
		}
		
		attributes.put(attributeType, val);
	}
	
	/**
	 * @description 增加属性值
	 */
	public void add(String attributeType,long val){
		
		attribteCheck(attributeType);
		
		if(null != attributes && attributes.containsKey(attributeType)){
			
			attributes.put(attributeType,attributes.get(attributeType) + val);
		}
		
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
