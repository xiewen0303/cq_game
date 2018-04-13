package com.junyou.gameconfig.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.gameconfig.vo.Off;
import com.junyou.gameconfig.vo.PointConfig;
import com.junyou.stage.utils.OffUtils;



/**
 * @description 地图偏移量管理工具类
 * @author hehj
 * 2010-7-27 下午01:35:16
 */
public class OffConfigUtils {
	
	public static final int LEFTTOP = 0; 
	public static final int TOP = 1; 
	public static final int RIGHTTOP = 2; 
	public static final int RIGHT = 3; 
	public static final int RIGHTBOTTOM = 4;  
	public static final int BOTTOM = 5; 
	public static final int LEFTBOTTOM = 6; 
	public static final int LEFT = 7; 
	public static final int CENTER = 8; 
	
	/**
	 * 
	 * [-1,-1],[0,-1],[1,-1],
	   [-1, 0],       [1, 0],
	   [-1, 1],[0, 1],[1, 1]
	 * 
	 * */
	public final static List<Off> AExtendOffs = new ArrayList<Off>();
	static{
		AExtendOffs.add(new Off(-1,-1));
		AExtendOffs.add(new Off(0,-1));
		AExtendOffs.add(new Off(1,-1));
		AExtendOffs.add(new Off(-1,0));
		AExtendOffs.add(new Off(1,0));
		AExtendOffs.add(new Off(-1,1));
		AExtendOffs.add(new Off(0,1));
		AExtendOffs.add(new Off(1,1));
	}
	
	

	/**
	  * 	aSurOff0 = [
	  *             	[-1,-1],[0,-1],[1,-1],
	  *            		[-1, 0],       [1, 0],
	  *            		[-1, 0],[0, 1],[1, 0]
	  *					]	 
	  */
	public final static List<Off> Off0s = new ArrayList<Off>();
	static{
		Off0s.add(new Off(-1,-1));
		Off0s.add(new Off(0,-1));
		Off0s.add(new Off(1,-1));
		Off0s.add(new Off(-1,0));
		Off0s.add(new Off(1,0));
		Off0s.add(new Off(-1,0));
		Off0s.add(new Off(0,1));
		Off0s.add(new Off(1,0));
	}

	/**
	 * aSurOff1 = [
	 * 				[-1, 0],[0,-1],[1, 0],
	 * 				[-1, 0],       [1, 0],
	 * 				[-1, 1],[0, 1],[1, 1]
	 * 				]
	 */
	public final static List<Off> Off1s = new ArrayList<Off>();
	static{
		Off1s.add(new Off(-1,0));
		Off1s.add(new Off(0,-1));
		Off1s.add(new Off(1,0));
		Off1s.add(new Off(-1,0));
		Off1s.add(new Off(1,0));
		Off1s.add(new Off(-1,1));
		Off1s.add(new Off(0,1));
		Off1s.add(new Off(1,1));
	}
	
	private final static Integer MAX_SEARCH_STEP = 20;

	private static Map<Integer, Map<String, LayerPoint>> allLayersMap0 = new HashMap<Integer, Map<String,LayerPoint>>();
	static{
		allLayersMap0 = getAllLayerMap(1000, 1000, MAX_SEARCH_STEP);
	}
	private static Map<Integer, Map<String, LayerPoint>> allLayersMap1 = new HashMap<Integer, Map<String,LayerPoint>>();
	static{
		allLayersMap1 = getAllLayerMap(1001, 1000, MAX_SEARCH_STEP);
	}
	private static Map<Integer, Map<String, LayerPoint>> allLayersMap2 = new HashMap<Integer, Map<String,LayerPoint>>();
	static{
		allLayersMap2 =  getAllLayerMap1(1000, 1000, MAX_SEARCH_STEP);
	}
	
	/**
	 * 判定给定的坐标是否在中心坐标指定的范围内
	 * @param cur 给定的坐标
	 * @param center 中心坐标
	 * @param maxScope 指定的范围(格数)
	 * @param pathFinderType 寻路类型
	 * @return
	 */
	public static boolean inScope(PointConfig cur,PointConfig center,int maxScope,PathFinderType pathFinderType){
		return inScope(cur, center, pathFinderType, convert2XsWidth(maxScope, pathFinderType));
	}
	
	/**
	 * 是否在指定半径内
	 * @param cur
	 * @param center
	 * @param radius 半径(像素)
	 */
	public static boolean inScope(PointConfig cur,PointConfig center,PathFinderType pathFinderType,int radius){
		
		float tX = convert2XsWidth(cur.getX(), pathFinderType);
		float tY = convert2XsHeight(cur.getY(), cur.getX(), pathFinderType);
		float sX = convert2XsWidth(center.getX(), pathFinderType);
		float sY = convert2XsHeight(center.getY(), center.getX(), pathFinderType);
		
		return ((tX - sX) * (tX - sX) + (tY - sY) * (tY - sY)) <= radius * radius;
		
	}
	
	private static Map<Integer, Map<String, LayerPoint>> getAllLayerMap(int x,int y,int maxLayer){
		
		Map<Integer, Map<String, LayerPoint>> allLayersMap = new HashMap<Integer, Map<String,LayerPoint>>(); // 已求出的全部层
		Map<String, LayerPoint> allPoints = new HashMap<String, LayerPoint>(); // 已求出的全部坐标
		
		Map<String, LayerPoint> bottomLayer = new HashMap<String, LayerPoint>();
		LayerPoint centerPoint = new LayerPoint(x,y,x,y);
		allPoints.put(centerPoint.getKey(), centerPoint);
		bottomLayer.put(centerPoint.getOff(), centerPoint);
		
		allLayersMap.put(0, bottomLayer);
		for(int i=0;i<=maxLayer;i++){
			
			Map<String, LayerPoint> innerLayer = allLayersMap.get(i); // 内层
			Map<String, LayerPoint> layerMap = new HashMap<String, LayerPoint>(); // 要求取的层
			for(LayerPoint point : innerLayer.values()){
				
				List<Off> offs = ((point.x & 1)>0) ? Off1s : Off0s;
				for(Off off : offs){
					
					LayerPoint tmp = new LayerPoint(point.x+off.getX(), point.y+off.getY(),x,y);
					if(allPoints.containsKey(tmp.getKey())) continue;
					if(layerMap.containsKey(tmp.getOff())) continue;
					
					// 保存求取到的坐标
					layerMap.put(tmp.getOff(), tmp);
					allPoints.put(tmp.getKey(), tmp);
					
				}
			}
			
			// 保存求取到的层
			allLayersMap.put(i+1, layerMap);
		}

		return allLayersMap;
	}
	
	private static Map<Integer, Map<String, LayerPoint>> getAllLayerMap1(int x,int y,int maxLayer){
		
		Map<Integer, Map<String, LayerPoint>> allLayersMap = new HashMap<Integer, Map<String,LayerPoint>>(); // 已求出的全部层
		Map<String, LayerPoint> allPoints = new HashMap<String, LayerPoint>(); // 已求出的全部坐标
		
		Map<String, LayerPoint> bottomLayer = new HashMap<String, LayerPoint>();
		LayerPoint centerPoint = new LayerPoint(x,y,x,y);
		allPoints.put(centerPoint.getKey(), centerPoint);
		bottomLayer.put(centerPoint.getOff(), centerPoint);
		
		allLayersMap.put(0, bottomLayer);
		for(int i=0;i<=maxLayer;i++){
			
			Map<String, LayerPoint> innerLayer = allLayersMap.get(i); // 内层
			Map<String, LayerPoint> layerMap = new HashMap<String, LayerPoint>(); // 要求取的层
			for(LayerPoint point : innerLayer.values()){
				
				List<Off> offs = AExtendOffs;
				for(Off off : offs){
					
					LayerPoint tmp = new LayerPoint(point.x+off.getX(), point.y+off.getY(),x,y);
					if(allPoints.containsKey(tmp.getKey())) continue;
					if(layerMap.containsKey(tmp.getOff())) continue;
					
					// 保存求取到的坐标
					layerMap.put(tmp.getOff(), tmp);
					allPoints.put(tmp.getKey(), tmp);
					
				}
			}
			
			// 保存求取到的层
			allLayersMap.put(i+1, layerMap);
		}

		return allLayersMap;
	}

	
	private static class LayerPoint{
		int x;
		int y;
		int cx;
		int cy;
		
		LayerPoint(int x,int y,int cx,int cy){
			this.x = x;this.y = y;this.cx = cx;this.cy = cy;
		}
		
		String getOff(){
			return (x-cx)+","+(y-cy);
		}
		
		String getKey(){ 
			return x + "-" + y;
		}
	}
	
	/**
	 * 转换格子x坐标到象素x坐标
	 * @param width 格子x坐标
	 * @return
	 */
	public static int convert2XsWidth(int width,PathFinderType pathFinderType){
		
		switch (pathFinderType) {
		case A:
			return width * OffUtils.GAIL_PX;
//			return width * 60;
		case AExtend:
			return width * OffUtils.GAIL_PX;
		}
		return -1;
		
	}

	/**
	 * 转换格子y坐标到象素y坐标
	 * @param height 格子y坐标
	 * @param width 格子x坐标
	 * @return
	 */
	public static int convert2XsHeight(int height,int width,PathFinderType pathFinderType){
		
		switch (pathFinderType) {
		case A:
//			return height * 30 + width % 2 * 15;
			return height * OffUtils.GAIL_PX;
		case AExtend:
			return height * OffUtils.GAIL_PX;
		}
		return -1;
	}
	
	public static PointConfig convert2XsPoint(PointConfig point,PathFinderType pathFinderType){
		return new PointConfig(convert2XsWidth(point.getX(),pathFinderType), convert2XsHeight(point.getY(), point.getX(),pathFinderType));
	}

}
