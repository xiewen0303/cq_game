package com.junyou.utils;

public class ServerProtect {

	private static int PROTECT_STATE = 0;
	
	
	public static boolean isCanChange(String param){
		return PROTECT_STATE == 0 && param.equals("startDoneServer");
	}
	
	public static boolean isCanDone(String param){
		return PROTECT_STATE == 1 && param.equals("doDoneServer");
	}
	
	public static void changeReadyState(){
		PROTECT_STATE = 0;
	}
	
	public static void changeState(){
		PROTECT_STATE = 1;
	}
}
