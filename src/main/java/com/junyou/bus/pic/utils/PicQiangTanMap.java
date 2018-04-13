package com.junyou.bus.pic.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongdian
 * 2016-3-15 下午2:42:46
 */
public class PicQiangTanMap {

	private static final Map<String, String> map = new HashMap<String, String>();
	
	
	/**
	 * 判断这个子活动是否对某个玩家强制弹出过
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public static boolean getDataByUserAndSubId(Long userRoleId,int subId){
		String str = userRoleId+"_"+subId;
		if(map.get(str) != null){
			return false;
		}else{
			//map.put(str, str);
			return true;
		}
	}
	
}
