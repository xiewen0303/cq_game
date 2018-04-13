package com.junyou.bus.shenqi;

public class ShenQiConstants {
	/**
	 * 累计在线时间（分钟）
	 */
	public static final int CONDITION_TYPE_ONLINE = 1;
	/**
	 * 累计签到（天数）
	 */
	public static final int CONDITION_TYPE_ASSIGN = 2;
	/**
	 * item 
	 */
	public static final int CONDITION_TYPE_ITEM = 3;
	/**
	 * 集齐前九把神器且需要消耗一定道具
	 */
	public static final int CONDITION_TYPE_SPECIAL = 4;

	/**
	 * 祝福类型：1：御剑
	 */
	public static final int ZHUFU_TYPE_ZUOQI = 1;
	/**
	 * 祝福类型：2：翅膀
	 */
	public static final int ZHUFU_TYPE_CHIBANG = 2;
	/**
	 * 祝福类型：3：仙剑
	 */
	public static final int ZHUFU_TYPE_XIANJIAN = 3;
	/**
	 * 祝福类型：4：战甲
	 */
	public static final int ZHUFU_TYPE_ZHANJIA = 4;
	
	/**
	 * 祝福类型：5：御剑  TODO wind 待策划加上
	 */
	public static final int ZHUFU_TYPE_WUQI = 5;

	/**
	 * 神器状态：1：不能激活
	 */
	public static final int SHENQI_STATUS_CAN_NOT_ACTIVATE = 1;
	/**
	 * 神器状态：2：可激活
	 */
	public static final int SHENQI_STATUS_CAN_ACTIVATE = 2;
	/**
	 * 神器状态：3：已激活
	 */
	public static final int SHENQI_STATUS_ACTIVATED = 3;
	/**
	 * 神器状态：4：已佩戴
	 */
	public static final int SHENQI_STATUS_WEARING = 4;
}
