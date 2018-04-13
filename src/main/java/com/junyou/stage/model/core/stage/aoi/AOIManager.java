package com.junyou.stage.model.core.stage.aoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointType;

/**
 * @description 场景AOI管理器<br/>
 * 				处理AOI的生成、查找
 * @author hehj
 * 2010-5-26 下午03:24:02
 */
public class AOIManager {

	private Map<Object, AOI> aoiMap = new HashMap<Object, AOI>();
	
	private int AOI_WIDTH;
	private int AOI_HALF_WIDTH;
	private int AOI_HEGIHT;
	private int AOI_HALF_HEGIHT;
	
	private int AOI_XBORDER_WIDTH;
	private int AOI_YBORDER_WIDTH;
	
	private int mapWidth;
	private int mapHeight;
	
	
	private PointType pointType;
	
	private AoiFactory aoiFactory;
	
	/**
	 * @param aoiWidth 指定AOI宽度
	 * @param aoiHeight 指定AOI高度
	 * @param mapWidth 场景的地图宽度
	 * @param mapHeight 场景的地图高度
	 * @param aoiXBorderWidth x放宽宽度
	 * @param aoiYBorderWidth y放宽宽度
	 */
	public AOIManager(int aoiWidth, int aoiHeight, int mapWidth, int mapHeight, PointType pointType, AoiFactory aoiFactory){
		AOI_WIDTH = aoiWidth;
		AOI_HALF_WIDTH = AOI_WIDTH/2;
		AOI_HEGIHT = aoiHeight;
		AOI_HALF_HEGIHT = AOI_HEGIHT/2;
		
		AOI_XBORDER_WIDTH = 0;
		AOI_YBORDER_WIDTH = 0;
		
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		this.pointType = pointType;
		
		this.aoiFactory = aoiFactory;
		
		init();
	}
	
	/**
	 * @param aoiWidth 指定AOI宽度
	 * @param aoiHeight 指定AOI高度
	 * @param mapWidth 场景的地图宽度
	 * @param mapHeight 场景的地图高度
	 * @param aoiXBorderWidth x放宽宽度
	 * @param aoiYBorderWidth y放宽宽度
	 */
	public AOIManager(int aoiWidth, int aoiHeight, int mapWidth, int mapHeight, int aoiXBorderWidth, int aoiYBorderWidth, PointType pointType, AoiFactory aoiFactory){
		AOI_WIDTH = aoiWidth;
		AOI_HALF_WIDTH = AOI_WIDTH/2;
		AOI_HEGIHT = aoiHeight;
		AOI_HALF_HEGIHT = AOI_HEGIHT/2;
		
		AOI_XBORDER_WIDTH = aoiXBorderWidth;
		AOI_YBORDER_WIDTH = aoiYBorderWidth;
		
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		this.pointType = pointType;
		
		this.aoiFactory = aoiFactory;
		
		init();
	}
	
	public PointType getPointType() {
		return pointType;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}
	
	public int getStageAoiXBorderWidth(){
		return AOI_XBORDER_WIDTH;
	}
	
	public int getStageAoiYBorderWidth(){
		return AOI_YBORDER_WIDTH;
	}

	/**
	 * 初始化场景AOI
	 */
	public void init(){
		
		// 转换格子坐标到象素坐标
		/*
		 *   result.x=input.x*30
		 *   result.y=input.y*30+input.x%2*15
		*/	
		int aoiXCount = mapWidth / AOI_WIDTH;
		if( (mapWidth % AOI_WIDTH) > 0) aoiXCount ++;
		int aoiYCount = mapHeight / AOI_HEGIHT;
		if( (mapHeight % AOI_HEGIHT) > 0) aoiYCount ++;
		
		for(int i=1;i<=aoiXCount;i++){
			for(int j=1;j<=aoiYCount;j++){
				AOI aoi = aoiFactory.create(AoiPointManager.getAoiPoint(i,j), this);
				aoiMap.put(getAOIKey(aoi.getAoiPoint()), aoi);
			}
		}
		
		for(AOI aoi : aoiMap.values()){
			if(null!=aoi) aoi.relateOtherAois(this);
		}
		
	}
	
	public int getAOI_WIDTH() {
		return AOI_WIDTH;
	}

	public int getAOI_HALF_WIDTH() {
		return AOI_HALF_WIDTH;
	}

	public int getAOI_HEGIHT() {
		return AOI_HEGIHT;
	}

	public int getAOI_HALF_HEGIHT() {
		return AOI_HALF_HEGIHT;
	}

	/**
	 * 根据用户x,y坐标获取AOI
	 * @param point
	 * @return
	 */
	public AOI getAOI(AoiPoint point){
		return aoiMap.get(getAOIKey(point));
	}
	
	/**
	 * 根据用户中心AOI坐标获取用户全部AOI坐标
	 * @param point
	 * @return
	 */
	public List<AoiPoint> getBabyAllAOIPoint(AoiPoint point){
		
		List<AoiPoint> pList = getBabyAroundAOIPoints(point);
		
		pList.add(point); // no.9(center aoi point)
		
		return pList;
	}
	
	/**
	 * 根据用户中心AOI坐标获取用户周围AOI坐标
	 * @param point
	 * @return(一个新的List，可以直接使用)
	 */
	public List<AoiPoint> getBabyAroundAOIPoints(AoiPoint point){
		List<AoiPoint> pList = new ArrayList<AoiPoint>();
		/*
		 * * * * * * *
		 * 1 * 2 * 3 *
		 * * * * * * * 
		 * 8 * 9 * 4 *
		 * * * * * * * 
		 * 7 * 6 * 5 *
		 * * * * * * *
		 */
		pList.add(getPoint1(point)); // no.1
		
		pList.add(getPoint2(point)); // no.2

		pList.add(getPoint3(point)); // no.3
		
		pList.add(getPoint4(point)); // no.4
		
		pList.add(getPoint5(point)); // no.5
		
		pList.add(getPoint6(point)); // no.6
		
		pList.add(getPoint7(point)); // no.7

		pList.add(getPoint8(point)); // no.8
		
		return pList;
	}
	
	public List<AOI> getBabyAroundAOIs(AoiPoint point){
		List<AOI> aois = new ArrayList<AOI>();
		List<AoiPoint> points = getBabyAroundAOIPoints(point);
		if(null!=points)
			for(AoiPoint p : points){
				AOI aoi = getAOI(p);
				if(null!=aoi) aois.add(aoi);
			}
		
		return aois;
	}
	
	public Map<Object, AOI> getBabyAroundAOIMap(AoiPoint point){
		Map<Object, AOI> aoiMap = new HashMap<Object, AOI>();
		List<AoiPoint> points = getBabyAroundAOIPoints(point);
		if(null!=points)
			for(AoiPoint p : points){
				AOI aoi = getAOI(p);
				if(null!=aoi) aoiMap.put(aoi.getAoiKey(), aoi);
			}
		
		return aoiMap;
	}
	
	private void addAoi(List<AOI> aois,AOI aoi){
		if(null!=aoi) aois.add(aoi);
	}
	
	/**
	 * 获取7,8,1,2,3的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getLeftTopAois(AoiPoint point){
		
		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint1(point)));
		addAoi(aois,getAOI(getPoint2(point)));
		addAoi(aois,getAOI(getPoint3(point)));
		addAoi(aois,getAOI(getPoint7(point)));
		addAoi(aois,getAOI(getPoint8(point)));
		
		return aois;
	}
	
	/**
	 * 获取1,2,3的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getTopAois(AoiPoint point){

		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint1(point)));
		addAoi(aois,getAOI(getPoint2(point)));
		addAoi(aois,getAOI(getPoint3(point)));
		
		return aois;
		
	}
	
	/**
	 * 获取1,2,3,4,5的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getRightTopAois(AoiPoint point){

		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint1(point)));
		addAoi(aois,getAOI(getPoint2(point)));
		addAoi(aois,getAOI(getPoint3(point)));
		addAoi(aois,getAOI(getPoint4(point)));
		addAoi(aois,getAOI(getPoint5(point)));
		
		return aois;
		
	}

	/**
	 * 获取1,8,7的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getLeftAois(AoiPoint point){

		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint1(point)));
		addAoi(aois,getAOI(getPoint8(point)));
		addAoi(aois,getAOI(getPoint7(point)));
		
		return aois;
		
	}

	/**
	 * 获取3,4,5的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getRightAois(AoiPoint point){

		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint3(point)));
		addAoi(aois,getAOI(getPoint4(point)));
		addAoi(aois,getAOI(getPoint5(point)));
		
		return aois;
		
	}
	
	/**
	 * 获取1,8,7,6,5的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getLeftBottomAois(AoiPoint point){

		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint1(point)));
		addAoi(aois,getAOI(getPoint8(point)));
		addAoi(aois,getAOI(getPoint7(point)));
		addAoi(aois,getAOI(getPoint6(point)));
		addAoi(aois,getAOI(getPoint5(point)));
		
		return aois;
		
	}

	/**
	 * 获取7,6,5的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getBottomAois(AoiPoint point){

		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint7(point)));
		addAoi(aois,getAOI(getPoint6(point)));
		addAoi(aois,getAOI(getPoint5(point)));
		
		return aois;
		
	}
	
	/**
	 * 获取7,6,5,4,3的区域
	 * @param point
	 * @return
	 */
	public List<AOI> getRightBottomAois(AoiPoint point){

		List<AOI> aois = new ArrayList<AOI>();
		
		addAoi(aois,getAOI(getPoint7(point)));
		addAoi(aois,getAOI(getPoint6(point)));
		addAoi(aois,getAOI(getPoint5(point)));
		addAoi(aois,getAOI(getPoint4(point)));
		addAoi(aois,getAOI(getPoint3(point)));
		
		return aois;
		
	}
	
	/**
	 * 根据用户坐标取用户中心AOI坐标
	 * @param p
	 * @return
	 */
	public AoiPoint getBabyCenterAOIPoint(Point point){
		// get the center aoi point
		int aoi_x = point.getX() / AOI_WIDTH;
		if((point.getX() % AOI_WIDTH) > 0) aoi_x = aoi_x + 1;
		int aoi_y = point.getY() / AOI_HEGIHT;
		if((point.getY() % AOI_HEGIHT) > 0) aoi_y = aoi_y + 1;
		aoi_x = ( aoi_x == 0) ? ++aoi_x : aoi_x;
		aoi_y = ( aoi_y == 0) ? ++aoi_y : aoi_y;

		return getCalcAoiPoint(aoi_x,aoi_y);
	}
	
	private static Object getAOIKey(AoiPoint point){
		return AoiPointManager.getAoiIntKey(point.getX(), point.getY());
	}
	
	
	/**
	 * 根据x,y获取AoiPoint(内部有优化 比原来减少new AoiPoint对象)
	 * @param x
	 * @param y
	 * @return
	 */
	private AoiPoint getCalcAoiPoint(int x,int y){
		return AoiPointManager.getAoiPoint(x,y);
	}
	
	/**
	 * * * * * * *
	 * 1 * 2 * 3 *
	 * * * * * * * 
	 * 8 * 9 * 4 *
	 * * * * * * * 
	 * 7 * 6 * 5 *
	 * * * * * * *
	 */
	private AoiPoint getPoint1(AoiPoint point){
		int x = point.getX()-1;
		int y = point.getY()-1;
		
		return getCalcAoiPoint(x,y);
	}
	private AoiPoint getPoint2(AoiPoint point){
		int x = point.getX();
		int y = point.getY()-1;
		
		return getCalcAoiPoint(x,y);
	}
	private AoiPoint getPoint3(AoiPoint point){
		int x = point.getX()+1;
		int y = point.getY()-1;
		
		return getCalcAoiPoint(x,y);
	}
	private AoiPoint getPoint4(AoiPoint point){
		int x = point.getX()+1;
		int y = point.getY();
		
		return getCalcAoiPoint(x,y);
	}
	private AoiPoint getPoint5(AoiPoint point){
		int x = point.getX()+1;
		int y = point.getY()+1;
		
		return getCalcAoiPoint(x,y);
	}
	private AoiPoint getPoint6(AoiPoint point){
		int x = point.getX();
		int y = point.getY()+1;
		
		return getCalcAoiPoint(x,y);
	}
	private AoiPoint getPoint7(AoiPoint point){
		int x = point.getX()-1;
		int y = point.getY()+1;
		
		return getCalcAoiPoint(x,y);
	}
	private AoiPoint getPoint8(AoiPoint point){
		int x = point.getX()-1;
		int y = point.getY();
		
		return getCalcAoiPoint(x,y);
	}

	/**
	 * 
	 */
	public boolean outOfMapBorder(Point position) {
		
		boolean out = false;
		if(out){
			if(position.getX()<=0 
					|| position.getY() <=0
					|| position.getX() >= getMapWidth()
					|| position.getY() >= getMapHeight()
					){
				out = true;
			}
		}
		
		return out;
	}
	
	/**
	 * 是否需要验证出AOI边界 [2014-06-06]
	 * @return true:需要
	 */
	public boolean isNeedCheckOut(){
		if(aoiMap.size() <= 1){
			return false;
		}else{
			return true;
		}
	}

	public Map<Object, AOI> getAoiMap() {
		return aoiMap;
	}
	
	
}
