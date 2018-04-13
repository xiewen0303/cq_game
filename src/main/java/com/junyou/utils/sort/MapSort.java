package com.junyou.utils.sort;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * map排序工具类
 */
public class MapSort {

	/**
	 * Map根据key按字符排序 
	 * @param unSortMap
	 * @return 得到一个排序好了的 SortedMap
	 */
	public static SortedMap<String, Object> mapSortByKey(Map<String, Object> unSortMap) {
		TreeMap<String, Object> result = new TreeMap<String, Object>();
		if(unSortMap == null || unSortMap.size() == 0){
			return result;
		}

		Object[] unSortKey = unSortMap.keySet().toArray();
		Arrays.sort(unSortKey);

		for (int i = 0; i < unSortKey.length; i++) {
			result.put(unSortKey[i].toString(), unSortMap.get(unSortKey[i]));
		}

		return result.tailMap(result.firstKey());
	}

}
