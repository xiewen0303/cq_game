package com.junyou.utils.name;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.ChuanQiConfigUtil;

/**
 * 防刷YY
 * @author DaoZheng Yuan
 * 2015年9月24日 下午1:50:22
 */
public class YYTerminator {

	/**
	 * 数字字典
	 */
	private static Map<String,String> NUMBER_DICT = new HashMap<>();
	static{
		/*0*/
		NUMBER_DICT.put("0", null);
		NUMBER_DICT.put("零", null);
		NUMBER_DICT.put("０", null);
		NUMBER_DICT.put("〇", null);
		NUMBER_DICT.put("o", null);
		NUMBER_DICT.put("O", null);
		
		/*1*/
		NUMBER_DICT.put("1", null);
		NUMBER_DICT.put("１", null);
		NUMBER_DICT.put("㈠", null);
		NUMBER_DICT.put("Ⅰ", null);
		NUMBER_DICT.put("❶", null);
		NUMBER_DICT.put("①", null);
		NUMBER_DICT.put("⒈", null);
		NUMBER_DICT.put("⑴", null);
		NUMBER_DICT.put("一", null);
		NUMBER_DICT.put("壹", null);
		NUMBER_DICT.put("I", null);
		NUMBER_DICT.put("l", null);
		
		/*2*/
		NUMBER_DICT.put("2", null);
		NUMBER_DICT.put("２", null);
		NUMBER_DICT.put("㈡", null);
		NUMBER_DICT.put("❷", null);
		NUMBER_DICT.put("②", null);
		NUMBER_DICT.put("⒉", null);
		NUMBER_DICT.put("⑵", null);
		NUMBER_DICT.put("二", null);
		NUMBER_DICT.put("贰", null);
		
		/*3*/
		NUMBER_DICT.put("3", null);
		NUMBER_DICT.put("З", null);
		NUMBER_DICT.put("３", null);
		NUMBER_DICT.put("㈢", null);
		NUMBER_DICT.put("Ⅲ", null);
		NUMBER_DICT.put("❸", null);
		NUMBER_DICT.put("③", null);
		NUMBER_DICT.put("⒊", null);
		NUMBER_DICT.put("⑶", null);
		NUMBER_DICT.put("三", null);
		NUMBER_DICT.put("叁", null);
		NUMBER_DICT.put("彡", null);
		
		/*4*/
		NUMBER_DICT.put("4", null);
		NUMBER_DICT.put("４", null);
		NUMBER_DICT.put("㈣", null);
		NUMBER_DICT.put("Ⅳ", null);
		NUMBER_DICT.put("❹", null);
		NUMBER_DICT.put("④", null);
		NUMBER_DICT.put("⒋", null);
		NUMBER_DICT.put("⑷", null);
		NUMBER_DICT.put("四", null);
		NUMBER_DICT.put("肆", null);
		NUMBER_DICT.put("泗", null);
		
		/*5*/
		NUMBER_DICT.put("5", null);
		NUMBER_DICT.put("５", null);
		NUMBER_DICT.put("㈤", null);
		NUMBER_DICT.put("Ⅴ", null);
		NUMBER_DICT.put("❺", null);
		NUMBER_DICT.put("⑤", null);
		NUMBER_DICT.put("⒌", null);
		NUMBER_DICT.put("⑸", null);
		NUMBER_DICT.put("五", null);
		NUMBER_DICT.put("伍", null);
		
		/*6*/
		NUMBER_DICT.put("6", null);
		NUMBER_DICT.put("б", null);
		NUMBER_DICT.put("６", null);
		NUMBER_DICT.put("㈥", null);
		NUMBER_DICT.put("Ⅵ", null);
		NUMBER_DICT.put("❻", null);
		NUMBER_DICT.put("⑥", null);
		NUMBER_DICT.put("⒍", null);
		NUMBER_DICT.put("⑹", null);
		NUMBER_DICT.put("六", null);
		NUMBER_DICT.put("陆", null);
		
		/*7*/
		NUMBER_DICT.put("7", null);
		NUMBER_DICT.put("７", null);
		NUMBER_DICT.put("㈦", null);
		NUMBER_DICT.put("Ⅶ", null);
		NUMBER_DICT.put("❼", null);
		NUMBER_DICT.put("⑦", null);
		NUMBER_DICT.put("⒎", null);
		NUMBER_DICT.put("⑺", null);
		NUMBER_DICT.put("七", null);
		NUMBER_DICT.put("柒", null);
		
		/*8*/
		NUMBER_DICT.put("8", null);
		NUMBER_DICT.put("８", null);
		NUMBER_DICT.put("㈧", null);
		NUMBER_DICT.put("Ⅷ", null);
		NUMBER_DICT.put("❽", null);
		NUMBER_DICT.put("⑧", null);
		NUMBER_DICT.put("⒏", null);
		NUMBER_DICT.put("⑻", null);
		NUMBER_DICT.put("八", null);
		NUMBER_DICT.put("捌", null);
		
		/*9*/
		NUMBER_DICT.put("9", null);
		NUMBER_DICT.put("９", null);
		NUMBER_DICT.put("㈨", null);
		NUMBER_DICT.put("Ⅸ", null);
		NUMBER_DICT.put("❾", null);
		NUMBER_DICT.put("⑨", null);
		NUMBER_DICT.put("⒐", null);
		NUMBER_DICT.put("⑼", null);
		NUMBER_DICT.put("九", null);
		NUMBER_DICT.put("玖", null);
	}
	
	private static Map<String,String> YY_REGS = new HashMap<>();
	static{
		YY_REGS.put("y", null);
		YY_REGS.put("У", null);
		YY_REGS.put("ｙ", null);
		YY_REGS.put("γ", null);
		YY_REGS.put("Y", null);
		YY_REGS.put("歪", null);
		YY_REGS.put("￥", null);
		YY_REGS.put("丫", null);
		YY_REGS.put("Ｙ", null);
		YY_REGS.put("ⓨ", null);
		YY_REGS.put("ｙ", null);
		YY_REGS.put("外", null);
		YY_REGS.put("吖", null);
	}
			
	
	/**
	 * 验证名字是否有YY嫌疑
	 * @param name
	 * @return true:有,false:没有
	 */
	public static boolean checkYYTrue(String name){
		if(name == null || "".equals(name)){
			return false;
		}
		//是否需要验证中文字符区间,true:验证,false：不验证
		if(!ChuanQiConfigUtil.isCheckCharsetRegion() ){
			return false;
		}
		
		int yyHeadTimes = 0;
		int yyNumberTimes = 0;
		String[] strArr = name.split(""); 
		for (int i = 0; i < strArr.length; i++) {
			String innerName = strArr[i];
			
			/*
			 * 检测 字符串中是有怀疑YY头
			 */
			if(YY_REGS.containsKey(innerName)){
				yyHeadTimes++;
			}
			//1.yy头命中两次，直接不合法
			if(yyHeadTimes >= 2){
				return true;
			}
			
			/*
			 * 检测 字符串中是有怀疑数字
			 */
			if(NUMBER_DICT.containsKey(innerName)){
				yyNumberTimes++;
			}
			//2.数字体命中三次，直接不合法
			if(yyNumberTimes >= 3){
				return true;
			}
		}
		
		//3.yy头命中一次上同时数字体命中二次以上，判定不合法
		return yyHeadTimes >= 1 && yyNumberTimes >= 2;
	}
	
//	public static void main(String[] args) {
//		String testName = "歪---1----2";
//		
//		System.out.println("checkYYTrue:"+checkYYTrue(testName));
//	}
}
