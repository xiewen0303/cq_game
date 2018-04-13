package com.junyou.bus.daomoshouzha.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongdian
 * 2015-8-12 下午2:48:20
 */
public class DaoMoCountUtils {

		private static Map<Long, Integer> map = new HashMap<Long, Integer>();
		
		
		public static void setUserDaoMoCount(Long userRoleId,int count){
			map.put(userRoleId, count);
		}
		
		public static int getUserDaoMoCount(Long userRoleId){
			Integer count = map.get(userRoleId);
			if(count == null){
				return 0;
			}
			return count.intValue();
		}
		
}
