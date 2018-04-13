/**
 * 
 */
package com.kernel.data.dao;

import java.util.List;


/**
 * @description 角色dao操作接口
 * @author ShiJie Chi
 * @created 2011-11-2下午5:14:30
 */
public interface IDaoOperation<P extends IEntity> {

	/**
	 * 异步加载方法 (当缓存中没有数据时,会自动去数据库里查询,并把查询到的结果自动放到缓存里方便后面使用)<br/>
	 *   调用此方法时要注意,一般只在角色登录时第一次查询时用,在其它频繁查询的应用场景不要用这个方法
	 * @param primaryKey
	 * @param userRoleId
	 * @author ydz
	 * @time 2015-1-13 下午 14:53:20
	 * @return
	 */
	public P cacheAsynLoad(Object primaryKey, Long userRoleId);
	
	/**
	 * 异步加载方法 (当缓存中没有数据时,会自动去数据库里查询,并把查询到的结果自动放到缓存里方便后面使用)<br/>
	 *   调用此方法时要注意,一般只在角色登录时第一次查询时用,在其它频繁查询的应用场景不要用这个方法
	 * @param userRoleId
	 * @author ydz
	 * @time 2015-1-21 下午 20:45:20
	 * @return
	 */
	public List<P> cacheAsynLoadAll(Long userRoleId);
	
	/**
	 * 根据主键读取单条记录
	 * @param primaryKey 主键
	 * @return
	 */
	public P cacheLoad(Object primaryKey,Long userRoleId);
	
	/**
	 * 获取表与userRoleId相关的所有记录
	 * @param userRoleId TODO
	 * @return
	 */
	public List<P> cacheLoadAll(Long userRoleId);
	
	/**
	 * 获取表与userRoleId相关的所有记录
	 * @param userRoleId
	 * @param filter 过滤器
	 */
	public List<P> cacheLoadAll(Long userRoleId,IQueryFilter filter);
	
	/**
	 * 插入单条记录
	 * @param pojo 对应表结构实体类对象
	 * @return 主键
	 */
	public Object cacheInsert(P pojo,Long userRoleId);
	
	/**
	 * 更新单条记录
	 * @param pojo 对应表结构实体类对象
	 * @return
	 */
	public int cacheUpdate(P pojo,Long userRoleId);
	
	/**
	 * 删除单条记录
	 * @param primaryKey 主键
	 * @return
	 */
	public int cacheDelete(Object primaryKey,Long userRoleId);
	 
	public void changeByIdentity(P pojo,long sourceRoleId,long targetRole,String accessType);
	
	public void changeCacheByIdentity(P pojo,long sourceRoleId,long targetRole);
}
