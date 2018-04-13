package com.junyou.gameconfig.utils;

import java.util.Map;

public class EmailConfigUtil {

	private static final String SPLIT_CHAR = ",";
	private static final String SPLIT_SUB_CHAR = ":";
	
	/**
	 * 特殊字符 用于间隔CODE码和参数的字符 (用于客户端解析)
	 */
	private static final String TS_CHAR = "ü";
	
	
	public static String getEmailCodeParam(Integer errrorCode,Object ... args){
		StringBuffer buff = new StringBuffer();
		buff.append(errrorCode);
		
		if(args.length > 0){
			
			for (Object param : args) {
				buff.append(TS_CHAR).append(param);
			}
		}
		
		return buff.toString();
	}
	
	public static String getEmailAttachments(Map<String,Integer> reward){
		if(reward == null || reward.size() == 0){
			return null;
		}
		
		StringBuffer buff = new StringBuffer();
		for(Map.Entry<String, Integer> entry : reward.entrySet()){
			buff.append(entry.getKey()).append(SPLIT_SUB_CHAR).append(entry.getValue()).append(SPLIT_CHAR);
		}
		
		return buff.toString().substring(0,buff.lastIndexOf(SPLIT_CHAR));
	}
}
