/**
 * 
 */
package com.junyou.stage.model.core.attribute;

import java.util.Map;
 
/**
 * @description 基本属性
 * @author ShiJie Chi
 * @created 2011-11-25上午10:13:59
 */
public interface IBaseAttribute {

	/**
	 * 获取某个属性类型值
	 * @param attributeType {@link AttributeType}
	 */
	public long get(String attributeType);

	/**
	 * 设置某个属性类型值
	 * @param attributeType {@link AttributeType}
	 */
	public void set(String attributeType, long val);
	
	/**
	 * 转换成map
	 * @param
	 */
	public Map<String,Long> toMap();

	
}
