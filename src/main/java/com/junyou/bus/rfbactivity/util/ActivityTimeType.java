package com.junyou.bus.rfbactivity.util;

/**
 * 热发布活动时间类型
 * @author DaoZheng Yuan
 * 2015年6月10日 下午7:59:58
 */
public class ActivityTimeType {

	/**
	 * 基于具体时间  类型=0
	 */
	public static final int TIME_0_SJ = 0;
	
	/**
	 * 基于星期几 类型=1
	 */
	public static final int TIME_1_WEEK = 1;
	
	/**
	 * 基于开服 类型=2
	 */
	public static final int TIME_2_KAI_FU = 2;
	
	/**
	 * 基于合服 类型=3
	 */
	public static final int TIME_3_HE_FU = 3;
	
	/**
	 * 基于开服循环 类型=4 （暂时实现单日循环,后面要实现多日循环）
	 */
	public static final int TIME_4_KAI_FU_LOOP = 4;
	
	/**
	 * 基于合服循环 类型=5（暂时实现单日循环,后面要实现多日循环）
	 */
	public static final int TIME_5_HE_FU_LOOP = 5;
}
