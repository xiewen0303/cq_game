package com.junyou.utils.lottery;

public class MathUtils {

	/**
	 * 两点之间距离
	 */
	public static float pointDistance(Integer chooseX, Integer chooseY, int x,
			int y) {
		return (float)Math.sqrt(Math.pow((chooseX - x), 2) * Math.pow((chooseY - y), 2));
	}
	
	/**
	 * 两值之间取最大值
	 * @param val
	 * @param compareVal
	 * @return
	 */
	public static int max(int val, int compareVal) {
		
		return val < compareVal ? compareVal : val;
	}
	/**
	 * 两值之间取最大值
	 * @param val
	 * @param compareVal
	 * @return
	 */
	public static long max(long val, long compareVal) {
		
		return val < compareVal ? compareVal : val;
	}
	
	/**
	 * 两值之间取最小值
	 * @param val
	 * @param compareVal
	 * @return
	 */
	public static int min(int val,int compareVal){
		
		return val > compareVal ? compareVal : val;
	}
	/**
	 * 两值之间取最小值
	 * @param val
	 * @param compareVal
	 * @return
	 */
	public static long min(long val,long compareVal){
		
		return val > compareVal ? compareVal : val;
	}
}
