package com.junyou.utils.math;
/**
 * 位运算工具
 * @author LiuYu
 * @date 2014-12-29 下午6:52:52
 */
public class BitOperationUtil {
	
	/**
	 * 计算状态
	 * @param state	数据值
	 * @param index	判断第几位数据，有效范围0-30
	 * @return	
	 */
	public static Boolean calState(Integer state, Integer index){
		if(!state.equals(0)){
			return (state >> index & 1) == 0;
		}
		return true;
	}
	
	/**
	 * 改变状态，将指定为真
	 * @param state	数据原值
	 * @param index	改变第几位数据，有效范围0-30
	 * @return	改变后的数据
	 */
	public static Integer chanageState(Integer state, Integer index){
		return (1 << index) | state;
	}
	/**
	 * 改变状态，将指定为0|false
	 * @param state	数据原值
	 * @param index	改变第几位数据，有效范围0-30 
	 * 注意：index从1开始 !!!!!!!!!!
	 * @return	改变后的数据
	 */
	public static Integer chanageStateZero(Integer state, Integer index){
		return state & ~(1 << index-1);
	}
	
	

}
