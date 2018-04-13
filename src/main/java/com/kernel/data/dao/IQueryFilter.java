/**
 * 
 */
package com.kernel.data.dao;

/**
 * @description 查询过滤器
 * @author ShiJie Chi
 * @created 2011-11-14上午10:00:06
 */
public interface IQueryFilter<T extends IEntity> {

	/**
	 * 验证指定实体是否满足条件
	 * @param entity 实体
	 */
	public boolean check(T entity);
	
	/**
	 * 是否停止查询,用于控制查询数量所需
	 * @param
	 */
	public boolean stopped();
	
}
