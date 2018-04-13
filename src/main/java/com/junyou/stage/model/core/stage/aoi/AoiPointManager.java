package com.junyou.stage.model.core.stage.aoi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 值对象AoiPoint重复利用管理器
 * @author DaoZheng Yuan
 * 2015年1月22日 下午1:33:23
 */
public class AoiPointManager {

	private static final Map<Integer,AoiPoint> AOI_POINTS = new ConcurrentHashMap<>();
	
	/**
	 * 获取AoiPoint对象根据x和y(当对象不存时,会自动创建这个对象让下次可以用)
	 * @param x
	 * @param y
	 * @return
	 */
	public static AoiPoint getAoiPoint(int x,int y){
		int aoiKey = getAoiIntKey(x, y);
		if(aoiKey < 0){
			return null;
		}
		
		AoiPoint aoiPoint = AOI_POINTS.get(aoiKey);
		if(aoiPoint == null){
			aoiPoint = new AoiPoint(x, y);
			//加入管理
			AOI_POINTS.put(aoiKey, aoiPoint);
		}
		
		return aoiPoint;
	}
	
	/**
	 * int类型的key值  [32768 * 32768]
	 * @param x
	 * @param y
	 * @return
	 */
	public static Integer getAoiIntKey(int x,int y){
		return (x << 15) + y;
	}
}
