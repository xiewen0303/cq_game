package com.junyou.stage.utils;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.stage.model.core.stage.Point;



/**
 * @description 地图偏移量管理工具类
 * @author hehj
 * 2010-7-27 下午01:35:16
 */
public class OffUtils {

	public static final int GAIL_PX = 40;
	
	public static int cover2GailPx(int px){
		return px/40 + (px%40 >0 ? 1:0);
	}
	
	/**
	 * 判定给定的坐标是否在中心坐标指定的范围内
	 * @param cur 给定的坐标
	 * @param center 中心坐标
	 * @param maxScope 指定的范围(格数)
	 * @param pathFinderType 寻路类型
	 * @return
	 */
	public static boolean inScope(Point cur,Point center,int maxScope,PathFinderType pathFinderType){
		
		return inScope(cur, center, pathFinderType, convert2XsWidth(maxScope, pathFinderType));
	}
	
	/**
	 * 是否在指定半径内
	 * @param cur
	 * @param center
	 * @param radius 半径(像素)
	 */
	public static boolean inScope(Point cur,Point center,PathFinderType pathFinderType,int radius){
		if(cur == null || center == null){
			return false;
		}
		
		if(cur.getX() == center.getX() && cur.getY() == center.getY()){
			return true;
		}
		
		float tX = convert2XsWidth(cur.getX(), pathFinderType);
		float tY = convert2XsHeight(cur.getY(), pathFinderType);
		float sX = convert2XsWidth(center.getX(), pathFinderType);
		float sY = convert2XsHeight(center.getY(), pathFinderType);
		
		return ((tX - sX) * (tX - sX) + (tY - sY) * (tY - sY)) <= radius * radius;
		
	}
	
	/**
	 * 是否在指定格子内
	 * @param from
	 * @param target
	 * @param step
	 * @return true:在
	 */
	public static boolean inScopeGz(Point from,Point target,int step){
		
		int fx = from.getX();
		int fy = from.getY();
		
		int tx = target.getX();
		int ty = target.getY();
		
		return Math.abs(fx-tx) <= step && Math.abs(fy-ty) <= step;
		
	}
	
	/**
	 * 是否在指定格子内
	 * @param from
	 * @param tx
	 * @param ty
	 * @param step
	 * @return true:在
	 */
	public static boolean inScopeGz(Point from,int tx,int ty,int step){
		
		int fx = from.getX();
		int fy = from.getY();
		
		return Math.abs(fx-tx) <= step && Math.abs(fy-ty) <= step;
		
	}
	
	/**
	 * 转换格子x坐标到象素x坐标
	 * @param width 格子x坐标
	 * @return
	 */
	public static int convert2XsWidth(int width,PathFinderType pathFinderType){
		
		switch (pathFinderType) {
		case A:
			return width * GAIL_PX;
		case AExtend:
			return width * GAIL_PX;
		}
		return -1;
		
	}

	
	/**
	 * 在算AOE区域时的半径(容错用)
	 * @return
	 */
	public static int areaRadius(){
		Float temp = (float) (GAIL_PX * 5);
		return temp.intValue();
	}
	
	/**
	 * 转换格子y坐标到象素y坐标
	 * @param height 格子y坐标
	 * @param width 格子x坐标
	 * @return
	 */
	public static int convert2XsHeight(int height,PathFinderType pathFinderType){
		
		switch (pathFinderType) {
		case A:
			return height * GAIL_PX;
		case AExtend:
			return height * GAIL_PX;
		}
		return -1;
	}
	
	
	public static Point convert2XsPoint(Point point,PathFinderType pathFinderType){
		if(point == null || pathFinderType == null){
			return null;
		}else{
			return new Point(convert2XsWidth(point.getX(),pathFinderType), convert2XsHeight(point.getY(),pathFinderType));
		}
	}

}
