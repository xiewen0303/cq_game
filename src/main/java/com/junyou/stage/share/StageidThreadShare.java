package com.junyou.stage.share;

public class StageidThreadShare {

	private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	
	public static void setStageId(String stageid){
		threadLocal.set(stageid);
	}
	
	public static String getStageId(){
		return threadLocal.get();
	}
}
