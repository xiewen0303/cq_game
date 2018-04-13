package com.kernel.data.dao;



/**
 * @description 高级数据操作方法
 * @author ShiJie Chi
 * @created 2010-4-9 下午01:48:39	
 */
public interface IAdvancedDaoOperation<P extends IEntity> {
	
	/**
	 * 根据主键读取单条记录
	 * @param primaryKey 主键
	 * @return
	 */
	public P load(Object primaryKey,Long identity,String accessType);
	
	/**
	 * 插入单条记录
	 * @param pojo 对应表结构实体类对象
	 * @return 主键
	 */
	public Object insert(P pojo,Long identity,String accessType);
	
	/**
	 * 更新单条记录
	 * @param pojo 对应表结构实体类对象
	 * @return
	 */
	public int update(P pojo,Long identity,String accessType);
	
	/**
	 * 删除单条记录
	 * @param primaryKey 主键
	 * @return
	 */
	public int delete(Object primaryKey,Long identity,String accessType);
	
}
