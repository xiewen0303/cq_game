/**
 * 
 */
package com.kernel.data.dao;

import java.util.List;


/**
 * @description 带参数查询操作
 * @author ShiJie Chi
 * @created 2011-11-2下午5:33:34
 */
public interface IParamDaoOperation<P> {
	
	/**
	 * 获取多条记录(无分页)
	 * @param queryParams
	 * @return
	 */
	public List<P> getRecords(QueryParamMap queryParams);
	
	/**
	 * 自定义查询
	 * @param
	 */
	public Object query(String statement,QueryParamMap queryParams);
	
	/**
	 * 自定义更新
	 * @param
	 */
	public void update(String statement,QueryParamMap queryParams);
	
	/**
	 * 删除
	 */
	public void delete(String statement,QueryParamMap queryParams);
	
	/**
	 * 获取多条记录(无分页)
	 * @param queryParams
	 * @return
	 */
	public List<P> getRecords(QueryParamMap queryParams,Long identity,String accessType);

}
