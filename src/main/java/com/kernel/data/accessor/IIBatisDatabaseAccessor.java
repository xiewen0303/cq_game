/**
 * 
 */
package com.kernel.data.accessor;

import java.sql.SQLException;
import java.util.List;

import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.QueryParamMap;

/**
 * @description ibatis数据访问接口
 * @author ShiJie Chi
 * @created 2011-10-31下午2:59:07
 */
public interface IIBatisDatabaseAccessor extends IDbAccessor {

	/**
	 * 自定义查询
	 * @param
	 * @throws SQLException 
	 */
	public List<IEntity> customQuery(String statement,QueryParamMap parameters);
	
	/**
	 * 根据指定参数查询
	 * @param
	 * @throws SQLException 
	 */
	public List<IEntity> queryList(QueryParamMap<String,Object> map,Class<? extends IEntity> clazz);
	
	/**
	 * 自定义更新
	 * @param
	 */
	public void customUpdate(String statement,QueryParamMap parameters);

	public void customDelete(String statement, QueryParamMap queryParams);
}
