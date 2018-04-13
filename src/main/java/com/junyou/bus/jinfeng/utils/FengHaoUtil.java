package com.junyou.bus.jinfeng.utils;

import com.junyou.utils.datetime.GameSystemTime;

public class FengHaoUtil {
	private static final String[] codes = new String[]{"banreason1","banreason2","banreason3","banreason4"};
	
	public static String getReasonBySort(int sort,String defReason){
		if(sort < codes.length){
			return codes[sort];
		}else{
			return defReason;
		}
	}
	/**
	 * 获取截止时间
	 * @param keepTime	持续时间，单位分
	 * @return
	 */
	public static long getExpireTime(int keepTime){
		long expireTime = 0;
		if(keepTime > 0){
			expireTime = GameSystemTime.getSystemMillTime() + keepTime * 60l * 1000;
		}
		return expireTime;
	}
}
