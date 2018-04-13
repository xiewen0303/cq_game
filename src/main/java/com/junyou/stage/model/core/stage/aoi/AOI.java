package com.junyou.stage.model.core.stage.aoi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;

/**
 * @description 场景AOI模型
 * @author hehj
 * 2010-5-26 下午03:08:24
 */
public class AOI {
	
	public static final int CENTER = 9; 
	public static final int TOP = 2; 
	public static final int BOTTOM = 6; 
	public static final int LEFT = 8; 
	public static final int RIGHT = 4; 
	public static final int RIGHTTOP = 3; 
	public static final int LEFTTOP = 1; 
	public static final int LEFTBOTTOM = 7; 
	public static final int RIGHTBOTTOM = 5; 
	
	private final transient AoiPoint point; // aoi坐标信息
	private final Integer aoiKey;
	private final int centerX; // aoi包含的中央普通坐标x
	private final int centerY; // aoi包含的中央普通坐标y
	private int x1; // aoi左x坐标
	private int x2; // aoi右x坐标
	private int y1; // aoi上y坐标
	private int y2; // aoi下y坐标
	private final int ex1; // aoi放宽后左x坐标
	private final int ex2; // aoi放宽后右x坐标
	private final int ey1; // aoi放宽后上y坐标
	private final int ey2; // aoi放宽后下y坐标
	
	private Map<Object,AOI> aroundAOIMap = new HashMap<Object, AOI>();
	private List<AOI> aroundAOIs = new ArrayList<AOI>();
	private Map<Integer, List<AOI>> directAoisMap = new HashMap<Integer, List<AOI>>();
	
	private AOIManager aoiManager;
	private Map<ElementType,Map<Long,IStageElement>> elementMaps = new HashMap<ElementType, Map<Long,IStageElement>>();
	private IAoiListener aoiListener;
	private transient CampRelationHelper campRelationHelper = new CampRelationHelper();
	
	public AOI(AoiPoint point,AOIManager aoiManager) {

		if(null == point) throw new NullPointerException("point can't be null.");
		
		this.point = point;
		this.aoiManager = aoiManager;
		
		this.centerX = (int) ((this.point.getX()-1+0.5)* aoiManager.getAOI_WIDTH());
		this.centerY = (int) ((this.point.getY()-1+0.5)* aoiManager.getAOI_HEGIHT());
		this.aoiKey = AoiPointManager.getAoiIntKey(centerX, centerY);
		
		this.x1 = centerX - aoiManager.getAOI_HALF_WIDTH() + 1;
		this.x1 = ( this.x1 < 0 ) ? 0 : this.x1;
		this.x2 = centerX + aoiManager.getAOI_HALF_WIDTH();
		this.x2 = ( this.x2 > aoiManager.getMapWidth() ) ? aoiManager.getMapWidth() : this.x2; 
		this.y1 = centerY - aoiManager.getAOI_HALF_HEGIHT() + 1;
		this.y1 = ( this.y1 < 0 ) ? 0 : this.y1;
		this.y2 = centerY + aoiManager.getAOI_HALF_HEGIHT();
		this.y2 = ( this.y2 > aoiManager.getMapHeight() ) ? aoiManager.getMapHeight() : this.y2; 
		
		int tmp = x1 - aoiManager.getStageAoiXBorderWidth();
		this.ex1 = ( tmp < 0 ) ? 0 : tmp;
		this.ex2 = x2 + aoiManager.getStageAoiXBorderWidth();
		tmp = y1 - aoiManager.getStageAoiYBorderWidth();
		this.ey1 = ( tmp < 0 ) ? 0 : tmp;
		this.ey2 = y2 + aoiManager.getStageAoiYBorderWidth();

		
	}

	public AoiPoint getAoiPoint() {
		return point;
	}
	
	/**
	 * 初始化当前AOI与周围其他AOI的关系
	 */
	public void relateOtherAois(AOIManager aoiManager){
		this.aroundAOIMap = aoiManager.getBabyAroundAOIMap(point);
		this.aroundAOIs = aoiManager.getBabyAroundAOIs(point);
		
		directAoisMap.put(LEFTTOP, aoiManager.getLeftTopAois(point));
		directAoisMap.put(TOP, aoiManager.getTopAois(point));
		directAoisMap.put(RIGHTTOP, aoiManager.getRightTopAois(point));
		directAoisMap.put(LEFT, aoiManager.getLeftAois(point));
		directAoisMap.put(RIGHT, aoiManager.getRightAois(point));
		directAoisMap.put(LEFTBOTTOM, aoiManager.getLeftBottomAois(point));
		directAoisMap.put(BOTTOM, aoiManager.getBottomAois(point));
		directAoisMap.put(RIGHTBOTTOM, aoiManager.getRightBottomAois(point));
		
		List<AOI> center = new ArrayList<AOI>();
		center.addAll(aroundAOIs);
		center.add(this);
		directAoisMap.put(CENTER, center);
	}
	
	public boolean arroundContains(AOI aroundAoi){
		return aroundAOIMap.containsKey(aroundAoi.getAoiKey());
	}
	
	public Integer getAoiKey(){
		return aoiKey;
	}
	
	/**
	 * 中心坐标x
	 * @return
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * 中心坐标
	 * @return
	 */
	public int getCenterY() {
		return centerY;
	}
	
	/**
	 * 获取坐标偏离aoi的方向(只有当元素离开aoi时检测)
	 * @param position
	 * @return
	 */
	public int getDirection(Point position){
		
		// 左边3个方向
		if(position.getX()<x1){
			
			if(position.getY()<y1) return LEFTTOP;
			if( position.getY()>y2 ) return LEFTBOTTOM;
			if( y1<=position.getY() && position.getY()<=y2) return LEFT;
		
		}
		// 右边3个方向
		if(position.getX()>x2){
			
			if(position.getY()<y1) return RIGHTTOP;
			if( position.getY()>y2 ) return RIGHTBOTTOM;
			if( y1<=position.getY() && position.getY()<=y2) return RIGHT;

		}
		
		// 中间两个方向
		if( x1<=position.getX() && position.getX() <= x2){
			if(position.getY()<y1) return TOP;
		}
		
		return AOI.BOTTOM;
	}
	
	/**
	 * 判定坐标是否越界
	 * @param position
	 * @return
	 */
	public boolean outOfBoder(Point position){
		
		boolean out = false;
		if((position.getX() - ex1<0)
			||(position.getX() - ex2>0)
			||(position.getY()-ey1<0)
			||(position.getY()-ey2>0) ){
			out = true;
		}
		
		return out;

	}
	
	/**
	 * 获取反方向
	 * @param enterDirection 指定的方向
	 * @return
	 */
	public int getOppositeDirection(int enterDirection){
		
		if(enterDirection > 8) return enterDirection;
		
		if(enterDirection <= 4) return enterDirection+4;
		
		return enterDirection -4;
		
	}
	
	@Override
	public String toString() {
		return (new StringBuffer("Aoi[")).append(point.toString()).append("]").toString();
	}

	/**
	 * @param
	 */
	public void add(IStageElement element, int direction) {
		
		//如果是fighter则加入到fighterMap
		if(ElementType.isFighter(element.getElementType())){
			getElementMap(ElementType.FIGHTER).put(element.getId(), element);
			
			//敌对目标加入
			campRelationHelper.add(element);
		}
		
		Map<Long, IStageElement> elementMap = getElementMap(element.getElementType());
		aoiListener.elementEnter(element, direction);
		elementMap.put(element.getId(), element);
	}

	/**
	 * @param
	 */
	public void delete(IStageElement element, int direction) {
		
		Map<Long, IStageElement> elementMap = getElementMap(element.getElementType());
		
		IStageElement exist = elementMap.remove(element.getId());
		
		if(null != exist){
			aoiListener.elementLeave(element, direction);
		}else{
			ChuanQiLog.stageError("aoi 离开时对象不存在,对象id：{},对象类型：{}",element.getId(),element.getElementType());
		}
		
		if(ElementType.isFighter(element.getElementType())){
			getElementMap(ElementType.FIGHTER).remove(element.getId());
			
			campRelationHelper.remove(element);
		}
		
	}
	
	/**
	 * 获取aoi指定方向的元素集合
	 * @param elementType 元素类型
	 * @param direction 方向
	 */
	public Collection<IStageElement> getElements(ElementType elementType, int direction) {
		
		List<AOI> aois = this.directAoisMap.get(direction);
		
		if(null != aois && aois.size() > 0){
			
			List<IStageElement> elements = new ArrayList<IStageElement>();
			
			for(AOI tmp : aois){
				Collection<IStageElement> tmpElements = tmp.getElements(elementType);
				if(null != tmpElements){
					elements.addAll(tmpElements);
				}
			}
			
			return elements;
		}
		
		return null;
	}
	
	/**
	 * 获取aoi中元素
	 * @param elementType 元素类型
	 */
	public Collection<IStageElement> getElements(ElementType elementType) {
		
		Map<Long,IStageElement> elementMap = getElementMap(elementType);
		
		if(null != elementMap && elementMap.size() > 0){
			return elementMap.values();
		}
		
		return null;
		
	}
	
	
	/**
	 * 获取指定元素类型的存储map
	 * @param elementType 
	 */
	private Map<Long,IStageElement> getElementMap(ElementType elementType){
		
		Map<Long,IStageElement> elementMap = elementMaps.get(elementType);
		if(null == elementMap){
			elementMap = new ConcurrentHashMap<Long, IStageElement>();
		}
		elementMaps.put(elementType, elementMap);
		
		return elementMap;
	}
	
	public boolean removeElement(Long elementId,ElementType elementType){
		Map<Long,IStageElement> elementMap = elementMaps.get(elementType);
		if(elementMap != null){
			return elementMap.remove(elementId)!=null;
		}
		return false;
	}
	
	
	/**
	 * 获取敌对元素
	 */
	public Collection<IStageElement> getEnemies(Integer camp, int direction) {
		
		List<AOI> aois = this.directAoisMap.get(direction);
		
		if(null != aois && aois.size() > 0){
			
			List<IStageElement> elements = new ArrayList<IStageElement>();
			for(AOI tmp : aois){
				Collection<IStageElement> tmpElements = tmp.getEnemies(camp);
				if(null != tmpElements){
					elements.addAll(tmpElements);
				}
			}
			
			return elements;
		}
		
		return null;
	}
	
	/**
	 * 获取敌对元素
	 */
	private Collection<IStageElement> getEnemies(Integer camp) {
		return campRelationHelper.getEnemies(camp);
	}


	/**
	 * 通知指定方向相邻的aoi,角色位置发生变化
	 * @param direction 指定方向
	 */
	public void notifyRoleChange(int direction) {
		List<AOI> aois = this.directAoisMap.get(direction);
		if(null != aois){
			for(AOI tmp : aois){
				tmp.roleChange();
			}
		}
		
	}
	
	/**
	 * 角色发生变化
	 */
	private void roleChange() {
		aoiSurroundRoleCache.change();
	}

	
	/**
	 * 获取周边角色id集合
	 */
	public Long[] getSurroundRoleIds(){
		return aoiSurroundRoleCache.getSurroundRoleIds();
	}
	
	/**
	 * 获取周边角色对象
	 */
	public Collection<IStageElement> getSurroundRole(){
		return aoiSurroundRoleCache.getSurroundRole();
	}
	
	private AoiSurroundRoleCache aoiSurroundRoleCache = new AoiSurroundRoleCache();
	
	/**
	 * @description 周边角色缓存
	 * @author ShiJie Chi
	 * @date 2012-6-28 上午9:55:55 
	 */
	private class AoiSurroundRoleCache{
		
		private boolean cached = false;
		
		private Collection<IStageElement> roleCache;
		private Long[] roleIdCache;

		public void change() {
			cached = false;
			this.roleCache = null;
			this.roleIdCache = null;
			
		}

		public Collection<IStageElement> getSurroundRole() {

			if(cached){
				return roleCache;
			}
			
			Collection<IStageElement> elements = getElements(ElementType.ROLE, AOI.CENTER);
			cacheRoles(elements);
			
			cached = true;
			
			return roleCache;
		}

		public Long[] getSurroundRoleIds() {
			
			if(cached){
				return roleIdCache;
			}
			
			Collection<IStageElement> elements = getSurroundRole();
			if(null != elements && elements.size() > 0){
				Long[] result = new Long[elements.size()];
				
				int i = 0;
				for(IStageElement element : elements){
					result[i] = element.getId();
					i++;
				}
				
				cacheRoleIds(result);
			}
			
			return roleIdCache;
		}
		
		private void cacheRoles(Collection<IStageElement> elements) {
			this.roleCache = elements;
		}

		private void cacheRoleIds(Long[] result) {
			this.roleIdCache = result;
		}
		
	}

	public void setListener(IAoiListener aoiListener) {
		this.aoiListener = aoiListener;
	}

}
