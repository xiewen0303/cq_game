package com.junyou.stage.model.stage.territory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathNodeCopy;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.stage.configure.TerritoryConfig;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IElementSearchFilter;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.IStageProduceManager;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.active.ActiveUtil;

/**
 * 领地战场景
 * @author  作者：wind
 * @version 创建时间：2017-7-5 上午11:56:41
 */
public class TerritoryStage implements IStage{
	/**领地实际场景*/
	private AoiStage stage;
	/**领地战管理器*/
	private TerritoryManager territoryManager;
	/**场景定时器*/
	private StageScheduleExecutor scheduleExecutor;
	/**占领公会id*/
	private Long ownerGuildId = null;
	/**
	 * 旗帜拥有玩家
	 */
	private IRole flagOwnerRole = null;
	
	private IBuff flagOwnerBuff = null;
	
	private IStageElement longt = null;
	
	private boolean hasFlag = true;
	public boolean isHasFlag() {
		return hasFlag;
	}

	public void setHasFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
	}

	public IStageElement getLongt() {
		return longt;
	}

	public void setLongt(IStageElement longt) {
		this.longt = longt;
	}
	private Map<Long,Long> roleFlagMap = new HashMap<Long,Long>();
	private ReentrantLock lock = new ReentrantLock();
	
	private TerritoryConfig territoryConfig;
	
	public TerritoryStage(AoiStage stage,TerritoryManager territoryManager,Long ownerGuildId,TerritoryConfig territoryConfig){
		this.stage = stage;
		this.territoryManager = territoryManager;
		this.scheduleExecutor = new StageScheduleExecutor(getId());
		this.territoryConfig = territoryConfig;
		if(ownerGuildId != null && ownerGuildId.longValue() == 0){
			this.ownerGuildId = null;
		}else{
			this.ownerGuildId = ownerGuildId;
		}
	}
	
	public void lock(){
		try {
			lock.tryLock(1,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
	}
	public void unlock(){
		if(lock.isHeldByCurrentThread()){
			lock.unlock();
		}
	}
	
	/**
	 * 初始化，把原场景中的玩家加入管理器(如果已有所属，启动定时)
	 * @param delay	需坚守多久后结束战斗
	 */
	public void init(int delay){
		List<IStageElement> roles = stage.getAllElements(ElementType.ROLE);
		if(roles.size() > 0){
			for (IStageElement element : roles) {
				IRole role = (IRole)element;
				territoryManager.enter(role);
			}
		}
		if(ownerGuildId != null){
			//领地已有所属公会，坚守30分钟获胜
			startTobeWinnerSchedule(delay);
		}
	}
	public void startSynFlagPositionSchedule(){
		cancelSynFlagPositionSchedule();
		StageTokenRunable runable = new StageTokenRunable(null, getId(), InnerCmdType.TERRITORY_SYN_FLAG, getId());
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_TERRITORY_FLAG_SYN, runable, 1, TimeUnit.SECONDS);
	}
	public void cancelSynFlagPositionSchedule(){
		scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_TERRITORY_FLAG_SYN);
	}
	
	public Long getOwnerGuildId() {
		return ownerGuildId;
	}

	public void setOwnerGuildId(Long ownerGuildId) {
		this.ownerGuildId = ownerGuildId;
	}

	public IRole getFlagOwnerRole() {
		return flagOwnerRole;
	}

	public void setFlagOwnerRole(IRole flagOwnerRole) {
		this.flagOwnerRole = flagOwnerRole;
	}

	public IBuff getFlagOwnerBuff() {
		return flagOwnerBuff;
	}

	public void setFlagOwnerBuff(IBuff flagOwnerBuff) {
		this.flagOwnerBuff = flagOwnerBuff;
	}

	public void startTobeWinnerSchedule(long delay){
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.TERRITORY_HAS_WINNER, null);
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_TERRITORY_HAS_WINNER, runable, delay, TimeUnit.SECONDS);
	}
	
	public void cancelTobeWinnerSchedule(){
		scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_TERRITORY_HAS_WINNER);
	}
	
	public void setFlag(Long userRoleId,Long flagGuid){
		roleFlagMap.put(userRoleId, flagGuid);
	}
	public void clearFlagMap(){
		roleFlagMap.clear();
	}
	public Long getFlag(Long userRoleId){
		return roleFlagMap.get(userRoleId);
	}
	@Override
	public String getId() {
		return stage.getId();
	}

	@Override
	public Integer getMapId() {
		return stage.getMapId();
	}

	@Override
	public Integer getLineNo() {
		return stage.getLineNo();
	}

	@Override
	public boolean checkPoint(Point point) {
		return stage.checkPoint(point);
	}

	@Override
	public boolean checkCanUseStagePoint(Point point, PointTakeupType takeupType) {
		return stage.checkCanUseStagePoint(point, takeupType);
	}

	@Override
	public boolean checkCanUseStagePoint(int x, int y,
			PointTakeupType takeupType) {
		return stage.checkCanUseStagePoint(x, y, takeupType);
	}

	@Override
	public boolean checkCanUseStagePoint(Point point) {
		return stage.checkCanUseStagePoint(point);
	}

	@Override
	public boolean isCanUseStagePoint(int x, int y) {
		return stage.isCanUseStagePoint(x, y);
	}

	@Override
	public boolean isNoAttackePoint(int x, int y) {
		return stage.isNoAttackePoint(x, y);
	}

	@Override
	public boolean inSight(IStageElement from, IStageElement target) {
		return stage.inSight(from, target);
	}

	@Override
	public boolean inSight(IStageElement from, Point targetPoint) {
		return stage.inSight(from, targetPoint);
	}

	@Override
	public boolean inScope(Point from, Point target, int step,
			ScopeType scopeType) {
		return stage.inScope(from, target, step, scopeType);
	}

	@Override
	public boolean isScopeGeZi(Point from, Point target, int step) {
		return stage.isScopeGeZi(from, target, step);
	}

	@Override
	public boolean isScopeGeZi(Point from, int tx, int ty, int step) {
		return stage.isScopeGeZi(from, tx, ty, step);
	}

	@Override
	public void taskupPoint(Point position, boolean b,
			PointTakeupType takeipType, PathNodeSize pathNodeSize) {
		stage.taskupPoint(position, b, takeipType, pathNodeSize);
	}

	@Override
	public Point getSurroundValidPoint(Point point, boolean priorOrNot,
			PointTakeupType takeupType) {
		return stage.getSurroundValidPoint(point, priorOrNot, takeupType);
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		stage.enter(element, x, y);
		if(ActiveUtil.isTerritory() && ElementType.isRole(element.getElementType())){
			IRole role = (IRole)element;
			territoryManager.enter(role);
			int[] zuobiao = null;
			if(flagOwnerRole != null){
				if(flagOwnerRole.getStage().getId().equals(getId())){
					zuobiao = territoryConfig.getZuobiao();
					BusMsgSender.send2One(role.getId(), ClientCmdType.TERRITORY_FLAG_CHANGE, new Object[]{stage.getMapId(),flagOwnerRole.getBusinessData().getGuildId()});
				}else{
					zuobiao = new int[2];
					Point p = flagOwnerRole.getPosition();
					zuobiao[0] = p.getX();
					zuobiao[1] = p.getY();
				}
			}else{
				if(getOwnerGuildId()==null){
					BusMsgSender.send2One(role.getId(), ClientCmdType.TERRITORY_FLAG_CHANGE, new Object[]{stage.getMapId(),-1});
				}else{
					BusMsgSender.send2One(role.getId(), ClientCmdType.TERRITORY_FLAG_CHANGE, new Object[]{stage.getMapId(),getOwnerGuildId().longValue()});
				}
				zuobiao = territoryConfig.getZuobiao();
			}
			BusMsgSender.send2One(role.getId(),  ClientCmdType.TERRITORY_SYN_FLAG, zuobiao);
		}
	}

	@Override
	public void leave(IStageElement element) {
		if(ActiveUtil.isTerritory() && ElementType.isRole(element.getElementType())){
			IRole iRole=(IRole) element;
			if(iRole.getBusinessData().getGuildId() != null && flagOwnerRole != null){
				if(flagOwnerRole.getId().longValue() == iRole.getId().longValue()){
					StageMsgSender.send2StageInner(iRole.getId(),getId(), InnerCmdType.TERRITORY_FLAG_OWNER_DEAD,null);
				}
			}
			territoryManager.leave(iRole);
		}
		stage.leave(element);
	}

	@Override
	public int getStageRoleCount() {
		return stage.getStageRoleCount();
	}

	@Override
	public void moveTo(IStageElement element, int x, int y) {
		stage.moveTo(element, x, y);
	}

	@Override
	public void teleportTo(IStageElement element, int x, int y) {
		stage.teleportTo(element, x, y);
	}

	@Override
	public <T extends IStageElement> T getElement(Long elementId,
			ElementType elementType) {
		return stage.getElement(elementId, elementType);
	}

	@Override
	public Collection<IStageElement> getAroundEnemies(IStageElement role) {
		return stage.getAroundEnemies(role);
	}

	@Override
	public Collection<IStageElement> getAroundRedRoles(IStageElement fighter) {
		return stage.getAroundRedRoles(fighter);
	}

	@Override
	public <T extends IStageElement> Collection<T> getSurroundElements(
			Point from, ElementType elementType, IElementSearchFilter filter) {
		return stage.getSurroundElements(from, elementType, filter);
	}

	@Override
	public <T extends IStageElement> Collection<T> getSurroundElements(
			Point from, ElementType elementType) {
		return stage.getSurroundElements(from, elementType);
	}

	@Override
	public List<Long> getSurroundElementIds(Point from,
			ElementType elementType, IElementSearchFilter filter) {
		return stage.getSurroundElementIds(from, elementType, filter);
	}

	@Override
	public Long[] getSurroundRoleIds(Point from, IElementSearchFilter filter) {
		return stage.getSurroundRoleIds(from, filter);
	}

	@Override
	public Long[] getSurroundRoleIds(Point from) {
		return stage.getSurroundRoleIds(from);
	}

	@Override
	public Map<Long, IStageElement> getBaseStageRoles() {
		return stage.getBaseStageRoles();
	}

	@Override
	public Long[] getNoSelfSurroundRoleIds(Point from, Long selfId) {
		return stage.getNoSelfSurroundRoleIds(from, selfId);
	}

	@Override
	public IStageProduceManager getStageProduceManager() {
		return stage.getStageProduceManager();
	}

	@Override
	public void setStageProduceManager(IStageProduceManager stageProduceManager) {
		stage.setStageProduceManager(stageProduceManager);
	}

	@Override
	public boolean isCopy() {
		return stage.isCopy();
	}

	@Override
	public StageType getStageType() {
		return StageType.TERRITORY_WAR;
	}
	
	/**
	 * 获取当前领地实际场景
	 * @return
	 */
	public IStage getRealyStage(){
		return stage;
	}
	/**获取领地战管理器*/
	public TerritoryManager getTerritoryManager() {
		return territoryManager;
	}

	@Override
	public boolean canFuhuo() {
		return false;
	}

	@Override
	public boolean isCanRemove() {
		return false;
	}

	@Override
	public boolean isCanPk() {
		return true;
	}

	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public Short getBackFuHuoCmd() {
		return InnerCmdType.INNER_TOWN_REVIVE;
	}

	@Override
	public String getBackFuhuoConstants() {
		return GameConstants.COMPONENT_BACK_FUHUO;
	}

	@Override
	public Integer getQzFuhuoSecond() {
		return GameConstants.ROLE_QZ_FUHUO_SECOND;
	}
	
	public boolean isCanFeijian(){
		return true;
	}
	
	public boolean isCanDazuo(){
		return true;
	}
	
	public boolean isCanJump(){
		return true;
	}

	public Short getSameMoCmd(){
		return null;
	}

	@Override
	public boolean isCanHasTangbao() {
		return true;
	}
	/**
	 * 是否可以携带宠物
	 * @return
	 */
	public boolean isCanHasChongwu(){
		return true;
	}

	@Override
	public Object[] getAllRoleIds() {
		return stage.getAllRoleIds();
	}

	@Override
	public boolean isCanUseProp() {
		return true;
	}
	public AoiStage getStage() {
		return stage;
	}

	public boolean isCanUseShenQi() {
		return true;
	}
	/**
	 * 设置领地战的旗子
	 */
	public void setFlag(boolean has){
		hasFlag = has;
	}
	/**
	 * 是否有领地战的旗子
	 * @return
	 */
	public boolean hasFlag(){
		return hasFlag;
	}

	@Override
	public boolean isCanChangeSkill() {
		return true;
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
	public DeadDisplay getDeadHiddenState() {
		return DeadDisplay.NOEXIT;
	}

	@Override
	public Point getCanMovePosition(Point attackerPostion, Point targetPoint) {
		//TODO 
//		int oldX = attackerPostion.getX();
//		int oldY = attackerPostion.getY();
//		
//		int targetX = targetPoint.getX();
//		int targetY = targetPoint.getX();
//		
//		//y方向
//		if(oldX == targetX){
//			if(targetY > oldY){
//				for (int i = targetY; i > oldY; i--) {
//					Point point = new Point(oldX, targetY);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//			}
//			
//			if(targetY < oldY){
//				for (int i = targetY; i < oldY; i++) {
//					Point point = new Point(oldX, targetY);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//			}
//		}
//		
//		//x方向
//		if(oldY == targetY){
//			if(targetX > oldX){
//				for (int i = targetX; i > oldX; i--) {
//					Point point = new Point(oldX, targetY);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//			}
//			
//			if(targetY < oldY){
//				for (int i = targetY; i < oldY; i++) {
//					Point point = new Point(oldX, targetY);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//			}
//		}
//		
//		//xy 的"/"方向  x+   y+
//		if(oldY > targetY && oldX > targetX){
//				for (int i = targetY; i > oldY; i++) {
//					Point point = new Point(targetX + i, targetX +i);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//		}
//		
//		//xy 的"/"方向  x-   y-
//		if(oldY < targetY && oldX < targetX){
//				for (int i = 1; i < targetX - oldX; i++) {
//					Point point = new Point(targetX - i, targetY - i);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//		}
//		
//		//xy 的"\"方向   y-  x+
//		if(oldY < targetY && oldX > targetX){
//				for (int i = 1; i < oldX -targetX; i++) {
//					Point point = new Point(targetX + i, targetY - i);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//		}
//		
//		//xy 的"\"方向   y+  x-
//		if(oldY > targetY && oldX < targetX){
//				for (int i = 1; i < targetX - oldX; i++) {
//					Point point = new Point(targetX - i, targetY + i);
//					if(checkCanUseStagePoint(point)){
//						return point;
//					}
//				}
//		}
		return attackerPostion;
	}

	@Override
	public boolean checkCanMoveStagePoint(int x, int y) {
//		stage.getPathInfo()
		PathNodeCopy pathNode = stage.getPathInfo().getPathNode(x, y, PathNodeSize._1X);
		if(null == pathNode){
			return false;
		}
//		if(!pathNode.isNotTakeup(takeupType.getVal())){
//			return false;
//		}
		
		return true;
//		return false;
	}
}
