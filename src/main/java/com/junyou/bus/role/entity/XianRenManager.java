package com.junyou.bus.role.entity;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author LiuYu
 * 2015-9-22 下午5:22:57
 */
public class XianRenManager {
	private static XianRenManager manager = new XianRenManager();
	public static XianRenManager getManager(){
		return manager;
	}
	
	private ConcurrentMap<Long, String> xianrenMap = new ConcurrentHashMap<>();
	private static int needXianRenCount = 5;
	
	/**
	 * 现在是否需要线人
	 * @return
	 */
	public boolean needXianren(){
		return xianrenMap.size() < needXianRenCount;
	}
	
	/**
	 * 增加线人
	 * @param userRoleId
	 * @param ip
	 * @return	是否成功增加为线人
	 */
	public boolean addXianren(Long userRoleId,String ip){
		if(ip == null){
			return false;
		}
		for (String xip : new ArrayList<>(xianrenMap.values())) {
			if(ip.equals(xip)){
				return false;
			}
		}
		xianrenMap.put(userRoleId, ip);
		return true;
	}
	
	/**
	 * 玩家下线业务
	 * @param userRoleId
	 */
	public void offline(Long userRoleId){
		xianrenMap.remove(userRoleId);
	}
}
