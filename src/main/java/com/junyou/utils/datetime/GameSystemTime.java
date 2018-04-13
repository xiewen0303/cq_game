package com.junyou.utils.datetime;

/**
 * 获取系统时间方法的类
 * @author DaoZheng Yuan
 * 2015年1月24日 下午3:10:14
 */
public class GameSystemTime {

	/**
	 * gm增量毫秒
	 */
	private static long GM_ADD_MILL_TIME = 0L;
	
	/**
	 * 增加gm增量毫秒属性值(这个方法一般最好不要调用,方法调试用的)
	 * @param addMillTime
	 */
	public static void addGmAddMillTime(long addMillTime){
		GM_ADD_MILL_TIME = addMillTime;
	}
	
	/**
	 * 获取当前系统当前时间毫秒(游戏内所有获取时间全部统一调用这个方法)
	 * @return
	 */
	public static long getSystemMillTime(){
		return System.currentTimeMillis() + GM_ADD_MILL_TIME;
	}
}
