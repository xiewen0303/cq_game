package com.junyou.utils.number;

public class LongUtils {
	
	/**
	 * object 转换为long,若为空的时候返回0
	 * @param obj
	 * @return
	 */
	public static long obj2long(Object obj){
		if( isEmpty(obj)){
			return 0l;
		}
		
		if( obj instanceof Long ){
			return (Long)obj;
		}else if( obj instanceof Integer ){
			return ( (Integer)obj ).longValue();
		}else if( obj instanceof Short ){
			return ( (Short)obj ).longValue();
		}else if( obj instanceof Double ){
			return ( (Double)obj ).longValue();
		}else if( obj instanceof Float ){
			return ( (Float)obj ).longValue();
		}else{
			String objStr = obj.toString().trim();
			int index = objStr.indexOf(".");
			try{
				if( index > -1 ){
					objStr = objStr.substring(0, index);
				}
				return Long.parseLong(objStr);
			}catch( Exception e ){
				return 0l;
			}
		}
	}
	
	/**
	 * 是否为空
	 * @param val
	 * @return true:为空null 
	 */
	public static boolean isEmpty(Object val){
		
		if(null == val){
			return true;
		}else{
			return false;
		}
	}
}
