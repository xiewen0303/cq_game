package com.junyou.utils.lottery;

import java.util.Random;

/**
 * 
 * @description 随机数
 *
 * @author LiuJuan
 * @created 2011-12-12 下午02:58:40
 */
public class RandomUtil {

	private static Random random = new Random();
	/**
	 * 从区间[0,n)获取一个随机数
	 * @param n n大于0
	 * @return
	 */
	public static int getIntRandomValue(int n){
		if (n == 0)
			return n;
		return random.nextInt(n);
	}
	/**
	 * 随机生成0-1之间的一个小数
	 * @return
	 */
	public static float getFloatRandom(){
		return random.nextFloat();
	}
	
	/**
	 * 从区间[m,n)获取一个随机数
	 * @param n n大于0
	 * @return
	 */
	public static int getIntRandomValue(int m,int n){
		if (m<=0||m>n)
			return 0;
		return random.nextInt(n-m)+m;
	}
	/**
	 * 产生[min,max]任意随机整数
	 * @param Max
	 * @param Min
	 * @return
	 */
	public static int getRondom(int Max,int Min){
		return (int)Math.round(Math.random()*(Max-Min)+Min) ;
	 }
}