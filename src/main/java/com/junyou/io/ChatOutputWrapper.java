package com.junyou.io;

 

/**
 * 聊天
 */
public class ChatOutputWrapper {
	
	public static Object[] getMessage2Public(Object obj1,Object obj2){
		Object[] result = new Object[3];
		Object[] obj = (Object[])obj1;
		result[0]=obj[0];
		result[1]=obj[1];
		result[2]=obj2; 
		return result;
		
	}
}
