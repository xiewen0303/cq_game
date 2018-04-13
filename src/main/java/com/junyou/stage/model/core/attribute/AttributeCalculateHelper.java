/**
 * 
 */
package com.junyou.stage.model.core.attribute;
 

/**
 * @description 属性计算帮助类
 * @author ShiJie Chi
 * @created 2011-11-25上午9:54:21
 */
public class AttributeCalculateHelper {
	
	/**
	 * 增加属性
	 * @param attributeType {@link AttributeType}
	 * @param
	 */
	public static void increaseAttribute(String attributeType, long val, IBaseAttribute destination) {
	
		Long hisVal = destination.get(attributeType);
		if(null == hisVal){
			hisVal = 0l;
		}
	
		destination.set(attributeType,val + hisVal);
	}
	
	/**
	 * 设置属性
	 * @param attributeType {@link AttributeType}
	 * @param val
	 * @param destinaction
	 */
	public static void setAttribute(String attributeType, long val, IBaseAttribute destinaction) {
		
		destinaction.set(attributeType, val);
	}
	
	/**
	 * 减少属性
	 * @param attributeType {@link AttributeType}
	 * @param
	 */
	public static void decreaseAttribute(String attributeType,long val,IBaseAttribute destination){
		
		Long hisVal = destination.get(attributeType);
		if(null == hisVal){
			hisVal = 0l;
		}
		
		destination.set(attributeType,hisVal - val);
	}

}
