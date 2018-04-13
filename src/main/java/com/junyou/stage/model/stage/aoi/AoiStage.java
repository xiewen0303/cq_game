/**
 * 
 */
package com.junyou.stage.model.stage.aoi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.export.PathInfoCopy;
import com.junyou.gameconfig.export.PathNodeCopy;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.gameconfig.vo.PathNode;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.log.ChuanQiLog;
import com.junyou.messageswap.MsgUtil;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOI;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.core.stage.aoi.AbsAoiStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.utils.OffUtils;
import com.junyou.utils.KuafuCmdUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.lottery.Lottery;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-30上午9:53:36
 */
public class AoiStage extends AbsAoiStage {
	
	private String id;
	
	private Integer mapId;
	
	private Integer lineNo;
	
	private PathInfoCopy pathInfo;
	/**
	 * 场景类型
	 */
	private StageType stageType;
	
	public PathInfoCopy getPathInfo() {
		return pathInfo;
	}

	/**
	 * 
	 */
	public AoiStage(String id, Integer mapId, Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig,StageType stageType) {
		super(aoiManager);
		this.id = id;
		this.mapId = mapId;
		this.lineNo = lineNo;
		this.pathInfo = new PathInfoCopy(pathInfoConfig);
		this.stageType = stageType;
	}
	

	@Override
	public boolean inSight(IStageElement from, IStageElement target) {
		return inSight(from, target.getPosition());
	}
	
	public boolean inSight(IStageElement from, Point targetPoint) {
		
		AOI fromCenterAoi = aoiManager.getAOI(aoiManager.getBabyCenterAOIPoint(OffUtils.convert2XsPoint(from.getPosition(),pathInfo.getPathFinderType())));
		AOI targetCenterAoi = aoiManager.getAOI(aoiManager.getBabyCenterAOIPoint(OffUtils.convert2XsPoint(targetPoint,pathInfo.getPathFinderType())));
		
		return (null != fromCenterAoi && null != targetCenterAoi && (fromCenterAoi.equals(targetCenterAoi) ||  fromCenterAoi.arroundContains(targetCenterAoi)));
	}


	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public Collection<IStageElement> getAroundEnemies(IStageElement fighter) {
		
		AOI curCenterAoi = aoiManager.getAOI(aoiManager.getBabyCenterAOIPoint(OffUtils.convert2XsPoint(fighter.getPosition(),pathInfo.getPathFinderType())));
		if(curCenterAoi == null){
			ChuanQiLog.error("{},{},{},{}",new Object[]{KuafuConfigPropUtil.isKuafuServer() ,KuafuCmdUtil.isDirectSwap(Short.valueOf("700")),KuafuCmdUtil.isSwapInKuafu(Short.valueOf("700")),KuafuManager.kuafuIng(fighter.getId())});
			ChuanQiLog.error("aoi is null.stageId : " + fighter.getStage().getId() + " .position : " +fighter.getPosition().toString()+"\telementType:"+fighter.getElementType()+ "\tname:"+fighter.getName());
			return null;
		}
		return curCenterAoi.getEnemies(fighter.getCamp(),AOI.CENTER);
	}
	
	
	public Collection<IStageElement> getAroundRedRoles(IStageElement fighter) {
		AOI curCenterAoi = aoiManager.getAOI(aoiManager.getBabyCenterAOIPoint(OffUtils.convert2XsPoint(fighter.getPosition(),pathInfo.getPathFinderType())));
		
		Collection<IStageElement> redRoles = null;
		
		Collection<IStageElement> aroundRoles = curCenterAoi.getSurroundRole();
		if(aroundRoles != null && aroundRoles.size() > 0){
			redRoles = new ArrayList<IStageElement>();
			
			for (IStageElement stageElement : aroundRoles) {
				IRole role = (IRole) stageElement;
				
				if(role.isRedRole()){
					redRoles.add(stageElement);
				}
			}
		}
		
		return redRoles;
	}
	
	@Override
	public Integer getMapId() {
		return mapId;
	}

	@Override
	public boolean inScope(Point from, Point target, int step,ScopeType scopeType) {
		
		return OffUtils.inScope(target, from, getPathInfo().getPathFinderType(), ScopeType.GRID.equals(scopeType) ? ScopeType.grid2Pixel(step) : step);
	}
	
	/**
	 * 是否在指定格子内
	 * @param from
	 * @param target
	 * @param step
	 * @return true:在
	 */
	public boolean isScopeGeZi(Point from, Point target,int step){
		
		return OffUtils.inScopeGz(from, target, step);		
	}

	/**
	 * 是否在指定格子内
	 * @param from
	 * @param tx
	 * @param ty
	 * @param step
	 * @return true:在
	 */
	public boolean isScopeGeZi(Point from,int tx,int ty,int step){
		return OffUtils.inScopeGz(from, tx,ty, step);		
	}

	@Override
	public Point getSurroundValidPoint(Point point,boolean specificPrior, PointTakeupType takeupType) {
		
		PathNodeCopy pathNode = pathInfo.getPathNode(point.getX(), point.getY(), PathNodeSize._1X);
		if(null == pathNode){
			return point;
		}
		
		if(specificPrior && pathNode.isNotTakeup(PointTakeupType.BEING.getVal())){
			return point;
		}
		
		Point result =  getSurroundValidPoint(pathNode, takeupType);
		if(null == result){
			result = point;
		}
		
		return result;
	}
	
	public boolean checkCanUseStagePoint(Point point,PointTakeupType takeupType){
		
		PathNodeCopy pathNode = pathInfo.getPathNode(point.getX(), point.getY(), PathNodeSize._1X);
		if(null == pathNode){
			return false;
		}
		
		if(!pathNode.isNotTakeup(takeupType.getVal())){
			return false;
		}
		
		return true;
	}
	
	public boolean checkCanMoveStagePoint(int x, int y){
		
		PathNodeCopy pathNode = pathInfo.getPathNode(x, y, PathNodeSize._1X);
		if(null == pathNode){
			return false;
		}
//		if(!pathNode.isNotTakeup(takeupType.getVal())){
//			return false;
//		}
		
		return true;
	}
	
	public boolean checkCanUseStagePoint(int x,int y,PointTakeupType takeupType){
		
		PathNodeCopy pathNode = pathInfo.getPathNode(x, y, PathNodeSize._1X);
		if(null == pathNode){
			return false;
		}
		
		if(!pathNode.isNotTakeup(takeupType.getVal())){
			return false;
		}
		
		return true;
	}
	
	public boolean checkCanUseStagePoint(Point point){
		
		PathNodeCopy pathNode = pathInfo.getPathNode(point.getX(), point.getY(), PathNodeSize._1X);
		if(null == pathNode){
			return false;
		}
		
		if(!pathNode.isNotTakeup(PointTakeupType.GOODS.getVal()) || !pathNode.isNotTakeup(PointTakeupType.BEING.getVal())){
			return false;
		}
		
		return true;
	}
	
	public boolean isCanUseStagePoint(int x,int y){
		PathNodeCopy pathNode = pathInfo.getPathNode(x, y, PathNodeSize._1X);
		if(null == pathNode){
			return false;
		}
		
		return true;
	}


	public boolean isNoAttackePoint(int x,int y){
		PathNodeCopy pathNode = pathInfo.getPathNode(x, y, PathNodeSize._1X);
		if(null == pathNode ||  pathNode.isSafe()){
			return true;
		}
		
		return false;
	}

	private Point getSurroundValidPoint(PathNodeCopy pathNode, PointTakeupType takeupType) {

		List<PathNode> surrounds = pathNode.getSurrounds();
		try {

			for (PathNode surround : surrounds) {
				PathNodeCopy realNode = pathInfo.getPathNode(surround.getX(), surround.getY(), PathNodeSize._1X);

				if (realNode.isNotTakeup(takeupType.getVal())) {
					return new Point(realNode.getX(), realNode.getY());
				}
			}
		} catch (StackOverflowError e) {
			return null;
		}

		PathNode _nextNode = surrounds.get(Lottery.roll(surrounds.size()));
		PathNodeCopy _nextNodeCopy = pathInfo.getPathNode(_nextNode.getX(), _nextNode.getY(), PathNodeSize._1X);
		return getSurroundValidPoint(_nextNodeCopy, takeupType);
	}


	@Override
	public void taskupPoint(Point position, boolean takeupOrNot, PointTakeupType takeupType,PathNodeSize pathNodeSize) {
		
		PathNodeCopy pathNode = pathInfo.getPathNode(position, pathNodeSize);
		
		if(null != pathNode){
			pathNode.setTakeupType(takeupOrNot, takeupType);
		}
		
		
	}
	
	public boolean isCopy() {
		return getStageType().isCopy();
	}

	@Override
	public Integer getLineNo() {
		return lineNo;
	}

	@Override
	protected Point convert2PixelPoint(Point position) {
		Point point = OffUtils.convert2XsPoint(position, pathInfo.getPathFinderType()); 
		return point;
	}

	public StageType getStageType() {
		return stageType;
	}

	@Override
	public boolean canFuhuo() {
		return true;
	}

	@Override
	public boolean isCanRemove() {
		return false;
	}
	
	/**
	 * 获取回城复活内部指令
	 * @return
	 */
	public Short getBackFuHuoCmd(){
		return InnerCmdType.INNER_TOWN_REVIVE;
	}
	/**
	 * 获取角色死亡强制回城复活时间
	 * @return
	 */
	public Integer getQzFuhuoSecond(){
		return GameConstants.ROLE_QZ_FUHUO_SECOND;
	}
	/**
	 * 角色死亡强制回城复活票据常量
	 * @return
	 */
	public String getBackFuhuoConstants(){
		return GameConstants.COMPONENT_BACK_FUHUO;
	}
	/**
	 * 设置领地战的旗子
	 */
	public void setFlag(boolean flag){
		
	}

	@Override
	public boolean hasFlag() {
		return false;
	}
	/**
	 * 能够变换妖神
	 * @return
	 */
	public boolean isCanChangeYaoShen(){
		return true;
	}

	@Override
	public boolean isCanChangeShow() {
		return true;
	}

	@Override
	public Point getCanMovePosition(Point attackerPostion, Point targetPoint) {
		int oldX = attackerPostion.getX();
		int oldY = attackerPostion.getY();
		
		int targetX = targetPoint.getX();
		int targetY = targetPoint.getY();
		
		//y方向
		if(oldX == targetX){
			if(targetY > oldY){
				for (int i = targetY; i > oldY; i--) {
					Point point = new Point(oldX, targetY);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
			}
			
			if(targetY < oldY){
				for (int i = targetY; i < oldY; i++) {
					Point point = new Point(oldX, targetY);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
			}
		}
		
		//x方向
		if(oldY == targetY){
			if(targetX > oldX){
				for (int i = targetX; i > oldX; i--) {
					Point point = new Point(oldX, targetY);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
			}
			
			if(targetY < oldY){
				for (int i = targetY; i < oldY; i++) {
					Point point = new Point(oldX, targetY);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
			}
		}
		
		//xy 的"/"方向  x+   y+
		if(oldY > targetY && oldX > targetX){
				for (int i = targetY; i > oldY; i++) {
					Point point = new Point(targetX + i, targetX +i);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
		}
		
		//xy 的"/"方向  x-   y-
		if(oldY < targetY && oldX < targetX){
				for (int i = 1; i < targetX - oldX; i++) {
					Point point = new Point(targetX - i, targetY - i);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
		}
		
		//xy 的"\"方向   y-  x+
		if(oldY < targetY && oldX > targetX){
				for (int i = 1; i < oldX -targetX; i++) {
					Point point = new Point(targetX + i, targetY - i);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
		}
		
		//xy 的"\"方向   y+  x-
		if(oldY > targetY && oldX < targetX){
				for (int i = 1; i < targetX - oldX; i++) {
					Point point = new Point(targetX - i, targetY + i);
					if(checkCanUseStagePoint(point)){
						return point;
					}
				}
		}
		return attackerPostion;
	}
}
