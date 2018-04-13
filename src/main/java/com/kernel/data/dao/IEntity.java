/**
 * 
 */
package com.kernel.data.dao;


/**
 * @description 实体接口
 * @author ShiJie Chi
 * @created 2011-10-31上午10:10:13
 */
public interface IEntity {
	
	/**
	 * 获取主键名
	 * @param
	 */
	public String getPirmaryKeyName();
	
	/**
	 * 获取主键值
	 * @param
	 */
	public Long getPrimaryKeyValue();
	
	/**
	 * 实体拷贝
	 * @param
	 */
	public IEntity copy();
}
