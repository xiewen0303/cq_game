/**
 * 
 */
package com.junyou.stage.model.core.attributeformula;

import com.junyou.stage.model.core.attribute.IFightAttribute;


/**
 * @description 属性计算公式
 * @author ShiJie Chi
 * @created 2011-12-2下午4:03:36
 */
public interface IAttributeFormula {
	
	/**
	 * 刷新属性值
	 * @param destination 刷新目的地
	 */
	public void refreshAttribute(IFightAttribute destination);
	
	/**
	 * 增加关联属性计算公式
	 * @param
	 */
	public void addRelateFormula(IAttributeFormula relateFormula);
	
}
