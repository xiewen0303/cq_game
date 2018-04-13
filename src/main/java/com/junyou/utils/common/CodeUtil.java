package com.junyou.utils.common;
/**
 * code码工具
 * @author LiuYu
 * 2015-6-30 下午4:26:24
 */
public class CodeUtil {

	/**code码参数拼接符*/
	private static final String CODE_EMAIL_SPLIT = "ü";
	
	/**
	 * 拼接code码（带参数）
	 * @param str
	 * @param args
	 * @return
	 */
	public static String getCode(Object obj,Object... args){
		StringBuilder text = new StringBuilder();
		text.append(obj);
		for (Object arg : args) {
			text.append(CODE_EMAIL_SPLIT).append(arg);
		}
		return text.toString();
	}
}
