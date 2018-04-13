package com.junyou.gs.share.thread;

public class RoleidThreadShare {

	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
	
	public static void setRoleId(Long roleid){
		threadLocal.set(roleid);
	}
	
	public static Long getRoleId(){
		return threadLocal.get();
	}
}
