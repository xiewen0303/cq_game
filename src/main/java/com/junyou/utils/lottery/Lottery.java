package com.junyou.utils.lottery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.junyou.log.ChuanQiLog;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-16上午11:15:13
 */
public enum Lottery {
	
	ONE(1),HUNDRED(100),THOUSAND(1000),TENTHOUSAND(10000);
	
	private int val;
	
	private static RandomNumber r = new RandomNumber();
	static{
		try {
			r.generateRandomObject();
			r.setRange(0, 1);
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
	}
	
	
	Lottery(int val){
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	/**
	 * @param
	 */
	public static int roll(int maxNumber) {
		return RandomUtil.getIntRandomValue(maxNumber);
	}
	
	/**
	 * 从[min,max)取值，如果min比max大，则取min
	 * @param min
	 * @param max
	 * @return
	 */
	public static int roll(int min, int max) {
		if(max-min > 1){
			return min + RandomUtil.getIntRandomValue(max-min);
		}else{
			return min;
		}
	}
	
	/**
	 * 以baseVal按unit倍放大后得到maxNumber，按unit范围roll随机数和maxNumber相比较
	 * 1、小于等于，则roll成功
	 * 2、 大于，则roll失败
	 */
	public static boolean roll(float baseVal,Lottery unit){
		
		int maxNumber = (int)(baseVal * unit.getVal());
		return baseVal > 0 && roll(unit.getVal()) <= maxNumber;
	}
	
	/**
	 * 以baseVal按unit倍放大后得到maxNumber，按unit范围roll随机数和maxNumber相比较
	 * 1、小于等于，则roll成功
	 * 2、 大于，则roll失败
	 */
	public static boolean rollFloat(float baseVal,Lottery unit){
		
		float maxNumber = (float)(baseVal * unit.getVal());
		
		float rollFloatValue = RandomUtil.getFloatRandom();
		
		return baseVal > 0 && rollFloatValue <= maxNumber;
	}
	
	/**
	 * 随即获取Map中一个key
	 * @param items (key: value:比例值/权重值)
	 * @return 
	 */
	public static <T> T getRandomKey(Map<T, Float> items){
		
		float sum = 0f;
		
		float ran = RandomUtil.getFloatRandom();
		for(Map.Entry<T, Float> entry : items.entrySet()){
			if(ran < sum + entry.getValue()){
				return entry.getKey();
			}
			sum += entry.getValue();
		}
		return null;
	}
	
	/**
	 * 随即获取Map中一个key
	 * @param items (key: value:比例值/权重值)
	 * @return 
	 */
	public static <T> T getRandomNewKey(Map<T, Float> items){
		
		float sum = 0f;
		
		
		Number number = r.getRandom();
		float ran = number.floatValue();
		for(Map.Entry<T, Float> entry : items.entrySet()){
			if( ran < sum + entry.getValue()){
				return entry.getKey();
			}
			sum += entry.getValue();
		}
		return null;
	}
	
	/**
	 * 以baseVal按unit倍放大后得到maxNumber，在0-maxNumber之间随机取值
	 * 1、小于等于，则roll成功
	 * 2、 大于，则roll失败
	 */
	public static int rollInt(float baseVal, Lottery unit){
		int maxNumber = (int)(baseVal * unit.getVal());
		
		return roll(maxNumber);
	}
	
	/**
	 * 随即获取Map中一个key(0-和值)
	 * @param items (key: value:比例值/权重值)
	 * @return 
	 */
	public static <T> T getRandomKey2(Map<T, Float> items){
		
		if( items == null || items.size() == 0 ){
			return null;
		}
		
		//求和
		float total = 0;
		for(Map.Entry<T, Float> entry : items.entrySet()){
			total += entry.getValue();
		}
		
		//如果几率之和为0，随机一个key
		if( total == 0 ){
			Collection<T> keys = items.keySet();
			List<T> keyList = new ArrayList<T>(keys);
			return keyList.get( roll(keyList.size()) );
		}
		
		float sum = 0f;
		//随机取值
		int ran = rollInt(total, TENTHOUSAND);
		
		for(Map.Entry<T, Float> entry : items.entrySet()){
			
			float tmp = sum + entry.getValue();
			
			if( ran >= (sum * TENTHOUSAND.getVal()) && ran < (tmp * TENTHOUSAND.getVal()) ){
				return entry.getKey();
			}
			sum = tmp;
		}
		return null;
	}
	/**
	 * 随即获取Map中一个key
	 * @param items (value:权重,整型)
	 * @return
	 */
	public static <T> T getRandomKeyByInteger(Map<T, Integer> items){
		int total=0;
		for(Map.Entry<T, Integer> entry : items.entrySet()){
			//判断权重和是否溢出
			if(Integer.MAX_VALUE - entry.getValue()<total){
				total=Integer.MAX_VALUE;
				break;
			}
			total+=entry.getValue();
		}
		
		//判断权重和是否合法
		if(total <= 0){
			return null;
		}
		
		int ran = roll(total);
		int sum=0;
		for(Map.Entry<T, Integer> entry : items.entrySet()){
			if(Integer.MAX_VALUE - entry.getValue() < sum || ran < sum + entry.getValue()){
				return entry.getKey();
			}
			sum += entry.getValue();
		}
		return null;
	}
	
	/**
	 * 随即获取Map中一个key
	 * @param items (value:权重,整型)
	 * @param total 权重总和
	 * @return
	 */
	public static <T> T getRandomKeyByInteger(Map<T, Integer> items,Integer total){
		if(total == null){
			return getRandomKeyByInteger(items);
		}
		//判断权重和是否合法
		if(total <= 0){
			return null;
		}
		
		int ran = roll(total);
		int sum=0;
		for(Map.Entry<T, Integer> entry : items.entrySet()){
			if(Integer.MAX_VALUE - entry.getValue() < sum || ran < sum + entry.getValue()){
				return entry.getKey();
			}
			sum += entry.getValue();
		}
		return null;
	}
	
	

}
