package com.junyou.bus.activityboss.util;

public class ActivityBossUtil {
	
	/**
	 * 获取boss刷怪表的唯一ID
	 * @param mapId
	 * @param configId
	 * @return
	 */
	public static String getKey(int mapId,int configId) {
		return mapId+"_"+configId;
	}
	
	/**
	 * 0:地图ID   1:是活动配置ID
	 * @param key
	 * @return
	 */
	public static int[] getMapConfigId(String key) {
		String[] values  =	key.split("_");
		return new int[]{Integer.parseInt(values[0]),Integer.parseInt(values[1])};
	}
}
