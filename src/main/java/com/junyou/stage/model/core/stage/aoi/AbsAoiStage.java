package com.junyou.stage.model.core.stage.aoi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.AbsStage;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementSearchFilter;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointType;

public abstract class AbsAoiStage extends AbsStage {

	protected AOIManager aoiManager;

	public AOIManager getAoiManager() {
		return aoiManager;
	}

	public void setAoiManager(AOIManager aoiManager) {
		this.aoiManager = aoiManager;
	}

	public AbsAoiStage(AOIManager aoiManager){
		this.aoiManager = aoiManager;
	}

	@Override
	public void moveTo(IStageElement element, int x, int y) {
		
		AOI curCenterAoi = aoiManager.getAOI(aoiManager.getBabyCenterAOIPoint(getPointInAoi(element.getPosition())));
		
		Point xsposition = getPointInAoi(new Point(x, y));
		AOI newCenterAoi = aoiManager.getAOI(aoiManager.getBabyCenterAOIPoint(xsposition));
		
		if(null == newCenterAoi){
			return;
		}
		
		if(!aoiManager.outOfMapBorder(xsposition)){
			
			if(aoiManager.isNeedCheckOut() && curCenterAoi.outOfBoder(xsposition)){ // 以坐标越界判定aoi区域变化

				if(curCenterAoi.arroundContains(newCenterAoi)){
					// 计算移动方向
					int direction = curCenterAoi.getDirection(xsposition);
					// 离开旧aoi
					if(null != curCenterAoi) curCenterAoi.delete(element,direction);
					// 进入新aoi
					newCenterAoi.add(element,newCenterAoi.getOppositeDirection(direction));
					
				}else{
					curCenterAoi.delete(element, AOI.CENTER);
					newCenterAoi.add(element, AOI.CENTER);
				}
			}
			
			//放弃旧坐标点的占位
			this.taskupPoint(element.getPosition(), false,element.getTakeupType(),element.getPathNodeSize());
			
			element.setPosition(x, y);
			//新坐标点占位
			this.taskupPoint(element.getPosition(), true,element.getTakeupType(),element.getPathNodeSize());
		}
	}
	
	/**
	 * 获取元素在aoi中的坐标点
	 */
	protected Point getPointInAoi(Point elementPoint){
		
		if(aoiManager.getPointType().equals(PointType.PIXEL)){
			return convert2PixelPoint(elementPoint);
		}
		
		return elementPoint;
	}
	
	/**
	 * 转化为像素坐标
	 */
	protected abstract Point convert2PixelPoint(Point position);
	

	@Override
	public void teleportTo(IStageElement element, int x, int y) {
		moveTo(element, x, y);
	}

	@Override
	protected void deleteHandle(IStageElement element) {
		try{
			this.taskupPoint(element.getPosition(), false, element.getTakeupType(),element.getPathNodeSize());
			
			element.leaveStageHandle(this);
		}catch (Exception e) {
			ChuanQiLog.debug("",e);
		}
		try{
			AOI aoi = aoiManager.getAOI( aoiManager.getBabyCenterAOIPoint(getPointInAoi(element.getPosition())) );
			aoi.delete(element, AOI.CENTER);
		}catch (Exception e) {
			ChuanQiLog.stageError("aoi delete error:",e);
		}
	}

	@Override
	protected void addHandle(IStageElement element) {
		if(element.getPosition() == null){
			ChuanQiLog.error("addHandle error " + element.getName() + ","+element.getPosition());
			return;
		}
		
		AOI aoi = null;
		try {
			aoi = aoiManager.getAOI( aoiManager.getBabyCenterAOIPoint(getPointInAoi(element.getPosition())) );
		} catch (Exception e) {
			ChuanQiLog.error("addHandle error mapId:"+getMapId(),e);
		}
		
		if(aoi == null){
			ChuanQiLog.error("map error mapId:"+this.getMapId() + ",name:"+element.getName()+",elmentType:"+element.getElementType().getVal()+",position:"+element.getPosition().toString());
		}
		
		aoi.add(element, AOI.CENTER);
		
		element.enterStageHandle(this);
		this.taskupPoint(element.getPosition(), true, element.getTakeupType(),element.getPathNodeSize());
		
	}

	@Override
	public boolean checkPoint(Point point) {
		AOI curCenterAoi = aoiManager.getAOI( aoiManager.getBabyCenterAOIPoint(getPointInAoi(point)) );
		return curCenterAoi != null;
	}

	@Override
	public List<Long> getSurroundElementIds(Point from, ElementType elementType, IElementSearchFilter filter) {
		
		Collection<IStageElement> elements = getSurroundElements(from, elementType);
		if( elements != null ){
			List<Long> list = new ArrayList<Long>();
			for( IStageElement element : elements ){
				if( !filter.isEnough() && filter.check(element) ){
					
					list.add( element.getId() );
					
				}
			}
			return list;
		}
		
		return null;
	}

	@Override
	public <T extends IStageElement> Collection<T> getSurroundElements(
			Point from, ElementType elementType, IElementSearchFilter filter) {
		
		Collection<IStageElement> elements = getSurroundElements(from, elementType);
		if( elements != null ){
			List<T> list = new ArrayList<T>();
			for( IStageElement element : elements ){
				if( !filter.isEnough() && filter.check(element) ){
					
					list.add( (T)element );
				}
			}
			return list;
		}
		
		return null;
	}

	@Override
	public <T extends IStageElement> Collection<T> getSurroundElements(Point from, ElementType elementType) {
		AOI curCenterAoi = aoiManager.getAOI( aoiManager.getBabyCenterAOIPoint( getPointInAoi(from) ) );
		if( curCenterAoi != null ){
			return (Collection<T>)curCenterAoi.getElements(elementType, AOI.CENTER);
		}
		return null;
	}

	public Long[] getSurroundRoleIds(Point from, IElementSearchFilter filter) {
		
		AOI curCenterAoi = aoiManager.getAOI( aoiManager.getBabyCenterAOIPoint(getPointInAoi(from)) );
		
		Collection<IStageElement> elements = curCenterAoi.getSurroundRole();
		
		List<Long> result = null;
		if( elements != null && elements.size() > 0 ){
			
			result = new ArrayList<Long>();
//			StringBuilder idBuilder = new StringBuilder();

			for( IStageElement element : elements ){
			
				//需要filter
				if( filter != null ){
					if( !filter.isEnough() && filter.check(element) ){
						result.add(element.getId());
						
//						idBuilder.append(",").append(element.getId());
					}

				//不需要filter
				}else{
					result.add(element.getId());
					
//					idBuilder.append(",").append(element.getId());
				}
				
			}
			
			if(result.size() > 0){
				return result.toArray(new Long[result.size()]);
			}else{
				return null;
			}
		}
		return null;
	}
	
	
	public Long[] getNoSelfSurroundRoleIds(Point from,Long selfId) {
		if(selfId == null){
			return null;
		}
		
		AOI curCenterAoi = aoiManager.getAOI( aoiManager.getBabyCenterAOIPoint(getPointInAoi(from)) );
		
		Collection<IStageElement> elements = curCenterAoi.getSurroundRole();
		
		List<Long> result = null;
		if( elements != null && elements.size() > 0 ){
			
//			StringBuilder idBuilder = new StringBuilder();

			result = new ArrayList<Long>();
			for( IStageElement element : elements ){
			
				if(!selfId.equals(element.getId())){
					result.add(element.getId());
					
//					idBuilder.append(",").append(element.getId());
				}
				
			}
			
//			if(idBuilder.length() == 0){
//				return null;
//			}else{
//				return idBuilder.substring(1).split(",");
//			}
			
			if(result.size() > 0){
				return result.toArray(new Long[result.size()]);
			}else{
				return null;
			}
			
		}
		return null;
	} 
	

	public Long[] getSurroundRoleIds(Point from) {
		return getSurroundRoleIds(from, null);
	}

}
