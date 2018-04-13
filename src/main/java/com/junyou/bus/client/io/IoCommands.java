package com.junyou.bus.client.io;

public class IoCommands {

	/**
	 * 客户端接入游戏
	 */
	public static final String IN = "10001";
	
	/**
	 * 创建角色
	 */
	public static final String LOGIN_CREATE_ROLE = "10002";
	
	/**
	 * 删除角色
	 */
	public static final String DELELT_ROLE = "10004";
	
	/**
	 * 查看目标属性
	 */
	public static final String TARGET_ATTRIBUTE = "12020";
	public static final String TARGET_ATTRIBUTE_FAIL = "12022";
	
	/**
	 * 内部查看目标属性
	 */
	public static final String INNER_TARGET_ATTRIBUTE = "S:12020";
	
	
	public static final String TEAM_TARGET_POSITION = "52015";
	
	public static final String INNER_TEAM_TARGET_POSITION = "S:52015";
	
	/**
	 * 查看物品详细信息
	 */
	public static final String QUERY_TARGET_GOODS = "30302";
	
	/**
	 * 内部查看物品详细信息
	 */
	public static final String INNER_QUERY_TARGET_GOODS = "B:30302";
	
}
