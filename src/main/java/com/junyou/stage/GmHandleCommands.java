package com.junyou.stage;

public class GmHandleCommands {

	/**
	 * 更改GM权限
	 */
	public static final String GM_CHANGE_TYPE = "SC:CHANGE_GM";
	
	/**
	 * GM传送到指定地图
	 */
	public static final String GM_MOVE_TO_MAP = "6000";
	
	/**
	 * GM传送到本地图指定坐标
	 */
	public static final String GM_MOVE_TO_THIS_MAP_OTHER = "6001";
	
	/**
	 * 传送到指定玩家
	 */
	public static final String GM_MOVE_TO_OTHER_PLAYER = "6002";
	
	/**
	 * 根据GUID禁言指定角色
	 */
	public static final String GM_JINYAN_OTHER_ROLE = "6020";
	
	/**
	 * 封停指定帐号
	 */
	public static final String GM_FENG_OTHER_PLAYER = "6021";
	
	/**
	 * 封停指定角色
	 */
	public static final String GM_FENG_OTHER_ROLE = "6022";
	
	/**
	 * 隐身/现身
	 */
	public static final String GM_YINSHEN_OR_NOT = "6040";
	
	/**
	 * 获得/失去GM权限
	 */
	public static final String GM_GET_OR_LOSE = "6041";
	
	/**
	 * 模糊查询某个在线角色
	 */
	public static final String GM_SELECT_OTHER_ROLE = "6042";
}
