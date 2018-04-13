package com.junyou.utils.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 36进制工具
 * @author LiuYu
 */
public class StringTo36Util {

	private static Map<String,Integer> numMap = new HashMap<String, Integer>();
	private static Map<Integer,String> tenMap = new HashMap<Integer,String>();
	private static final Integer size = 36;
	static{
		String[] str = "0123456789abcdefghijklmnopqrstuvwxyz".split("|");
		for (int i = 1; i < str.length; i++) {
			int n = i - 1;
			String num = str[i];
			numMap.put(num, n);
			tenMap.put(n, num);
		}
	}
	/**
	 * 十进制转换36进制数
	 * @param num	仅支持正整数
	 * @return
	 */
	public static String tenTo36(int num){
		List<String> list = new ArrayList<String>();
		while(num >= size){
			int x = num % size;
			list.add(tenMap.get(x));
			num /= size;
		}
		list.add(tenMap.get(num));
		StringBuffer str = new StringBuffer();
		for (int i = list.size() - 1;i >= 0 ; i--) {
			str.append(list.get(i));
		}
		return str.toString();
	}
	
	/**
	 * 36进制转换十进制数
	 * @param num	
	 * @return	仅支持正整数
	 */
	public static int thirtySixTo10(String numStr){
		int num = 0;
		if(numStr == null || numStr.length() < 1){
			return num;
		}
		String[] str = numStr.split("|");
		for (int i = 1; i < str.length; i++) {
			num = num * size;
			String s = str[i];
			num += numMap.get(s);
		}
		return num;
	}
	
}
