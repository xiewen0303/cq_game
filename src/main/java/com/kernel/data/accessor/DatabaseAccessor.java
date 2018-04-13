/**
 * 
 */
package com.kernel.data.accessor;

import static com.kernel.data.accessor.IbatisStatementMapper.DELETE_OP;
import static com.kernel.data.accessor.IbatisStatementMapper.INSERT_OP;
import static com.kernel.data.accessor.IbatisStatementMapper.SELECT_MULTI_OP;
import static com.kernel.data.accessor.IbatisStatementMapper.SELECT_SINGLE_OP;
import static com.kernel.data.accessor.IbatisStatementMapper.UPDATE_OP;
import static com.kernel.data.accessor.IbatisStatementMapper.createStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.junyou.utils.KuafuConfigPropUtil;
import com.kernel.data.accessor.exception.DataExceptionFactory;
import com.kernel.data.dao.IEntity;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.data.dao.QueryParamMap;
import com.kernel.spring.SpringApplicationContext;

/**
 * @description 直接数据库访问器
 * @author ShiJie Chi
 * @created 2011-11-7下午4:11:03
 */
public class DatabaseAccessor implements IIBatisDatabaseAccessor {

	private SqlMapClient sqlmapClient;
	
	/**
	 * 
	 */
	public DatabaseAccessor() {
		if(!KuafuConfigPropUtil.isKuafuServer()){
			sqlmapClient = (SqlMapClient)SpringApplicationContext.getApplicationContext().getBean("mysqldbSqlMapClient");
		}
	}
	
	/**
	 * 获取代理
	 * @param
	 */
	public SqlMapClient getdelegate(){
		return sqlmapClient;
	}
	
	@Override
	public Object insert(IEntity entity, Long identity,Class<? extends IEntity> clazz) {
		try{
			sqlmapClient.insert(createStatement(INSERT_OP, EntityMetaData.getEntityName(entity)), entity);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("insert error ! entity:" + EntityMetaData.getEntityName(entity) + ",primaryKey :" + entity.getPrimaryKeyValue(), e);
		}
		
		return entity;
	}
	
	@Override
	public IEntity query(Object primaryKey, Class<? extends IEntity> clazz, Long Identity) {
		
		IEntity entity = null;
		
		try{
			entity = (IEntity)sqlmapClient.queryForObject(createStatement(SELECT_SINGLE_OP, EntityMetaData.getEntityName(clazz)), primaryKey);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("query single error ! entity:" + EntityMetaData.getEntityName(entity) + ",primaryKey :" + entity.getPrimaryKeyValue(), e);
		}
		
		return entity;
	}

	@Override
	public List<IEntity> queryList(QueryParamMap<String, Object> map,
			Class<? extends IEntity> clazz) {
		
		List<IEntity> result = null;
		try{
			result = (List<IEntity>)sqlmapClient.queryForList(createStatement(SELECT_MULTI_OP, EntityMetaData.getEntityName(clazz)), map);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("query multi error ! entity:" + EntityMetaData.getEntityName(clazz) , e);
		}
		
		return result;
	}

	@Override
	public int delete(Object primaryKey, Class<? extends IEntity> clazz, Long identity) {
		
		int result = 0;
		
		try{
			result = sqlmapClient.delete(createStatement(DELETE_OP, EntityMetaData.getEntityName(clazz)), primaryKey);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("delete error ! entity:" + EntityMetaData.getEntityName(clazz) + ",primaryKey :" + primaryKey, e);
		}
		
//		if(result == 0){
//			throw DataExceptionFactory.getException(DataErrorMsg.ERROR_FOR_DELETE + "entity:" + EntityMetaData.getEntityName(clazz));
//		}
		
		return result;
	}

	@Override
	public int update(IEntity entity, Long identity) {
		
		int result = 0;
		
		try{
			result = sqlmapClient.update(createStatement(UPDATE_OP, EntityMetaData.getEntityName(entity)), entity);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("update error ! entity:" + EntityMetaData.getEntityName(entity) + ",primaryKey :" + entity.getPrimaryKeyValue(), e);
		}
		
//		if(result == 0){
//			throw DataExceptionFactory.getException(DataErrorMsg.ERROR_FOR_DELETE + "entity:" + EntityMetaData.getEntityName(entity));
//		}
		
		return result;
	}

	@Override
	public List customQuery(String statement, QueryParamMap parameters) {
		
		List result = null;
		
		try{
			result = sqlmapClient.queryForList(statement, parameters);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("query  error ! statement:" + statement, e);
		}
		
		return result;
	}

	@Override
	public List<IEntity> query(String indexName, Object indexValue,
			Class<? extends IEntity> clazz, Long identity) {
		
		QueryParamMap<String,Object> map = new QueryParamMap<String,Object>();
		map.put(indexName, indexValue);
		
		return queryList(map, clazz);
	}

	@Override
	public void customUpdate(String statement, QueryParamMap parameters) {
		try{
			sqlmapClient.update(statement, parameters);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("customUpdate error !", e);
		}
	}

	@Override
	public List<IEntity> query(String indexName, Object indexValue,
			IQueryFilter filter, Class<? extends IEntity> clazz, Long identity) {
		QueryParamMap<String,Object> map = new QueryParamMap<String,Object>();
		map.put(indexName, indexValue);
		
		List<IEntity> entitys = queryList(map, clazz);
		if(null != entitys && entitys.size() > 0){
			
			List<IEntity> result = null;
			for(IEntity tmp : entitys){
				
				if(!filter.stopped()){
					if(filter.check(tmp)){
						if(null == result){
							result = new ArrayList<IEntity>();
						}
						
						result.add(tmp);
					}
				}else{
					break;
				}
			}
			
			return result;
		}
		
		return null;
	}

	@Override
	public void customDelete(String statement, QueryParamMap queryParams) {
		try{
			sqlmapClient.delete(statement, queryParams);
		}catch(SQLException e){
			throw DataExceptionFactory.getException("customDelete error !", e);
		}
	}

	@Override
	public void changeCacheByIdentity(IEntity entity, long sourceRoleId,long targetRole) {
	 
		
	}

	@Override
	public boolean onlyAddCache(IEntity entity, long userRoleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onlyDeleteCache(IEntity entity, long userRoleId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onlyUpdateDB(IEntity entity, long identity) {
		// TODO Auto-generated method stub
	}
	
	public void qzFlush(long userRoleId){
		// TODO Auto-generated method stub
	}
}
