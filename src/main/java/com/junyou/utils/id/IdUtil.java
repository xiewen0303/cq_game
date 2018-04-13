package com.junyou.utils.id;


/**
 * @author DaoZheng Yuan
 * 2014-11-12 下午6:55:32
 */
public class IdUtil {
	
	private static ThreadLocal<Id> IdLocal = new ThreadLocal<Id>(){
		@Override
		protected Id initialValue() {
			return new Id(String.valueOf(Thread.currentThread().getId()));
		}
	};
	
	/**
	 * @param
	 */
	public static String nextString(String prefix){
		return new StringBuffer().append(prefix).append(IdLocal.get().nextString()).toString();
	}

}
