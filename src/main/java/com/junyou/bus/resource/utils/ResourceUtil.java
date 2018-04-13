package com.junyou.bus.resource.utils;

import com.junyou.utils.string.StringTo36Util;


public class ResourceUtil {
	/**
	 * 计算总和次数
	 * @param state
	 * @return
	 */
	public static int getSumTime(String state){
		if(state == null){
			return 0;
		}
		int num = 0;
		for (String str : state.split("|")) {
			num += StringTo36Util.thirtySixTo10(str);
		}
		return num;
	}
	
	/**
	 * 消耗一次
	 * @param state
	 * @return
	 */
	public static String costOnce(String state){
		if(state == null){
			return "0";
		}
		String[] strs = state.split("|");
		for (int i = 0; i < strs.length; i++) {
			int num = StringTo36Util.thirtySixTo10(strs[i]);
			if(num > 0){
				num--;
				strs[i] = StringTo36Util.tenTo36(num);
				break;
			}
		}
		StringBuffer out = new StringBuffer();
		for (String str : strs) {
			if(out.length() == 0 && "0".equals(str)){
				continue;
			}
			out.append(str);
		}
		return out.length() == 0 ? "0" : out.toString();
	}
	
	/**
	 * 增加昨日剩余次数
	 * @param state		历史次数
	 * @param maxDay	最大天数
	 * @param times		昨日次数
	 * @return
	 */
	public static String addLastTime(String state,int maxDay,int times){
		if(state == null){
			state = "";
		}
		if(state.length() >= maxDay){
			state = state.substring(state.length() - maxDay + 1);
		}
		if("0".equals(state)){
			state = "";
		}
		state += StringTo36Util.tenTo36(times);
		return state;
	}
}
