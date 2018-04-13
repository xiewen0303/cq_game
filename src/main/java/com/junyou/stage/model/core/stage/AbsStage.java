/**
 * 
 */
package com.junyou.stage.model.core.stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.tunnel.DirectMsgWriter;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-30上午9:41:03
 */
public abstract class AbsStage implements IStage {
	
	private Map<ElementType,Map<Long,IStageElement>> elementMaps = new HashMap<ElementType, Map<Long,IStageElement>>();
	
	private IStageProduceManager stageProduceManager;
	
	private boolean canPk;
	
	private boolean isAddPk;
	
	@Override
	public void leave(IStageElement element) {
		if(element == null)return;
		
		Map<Long,IStageElement> elementMap = getElementMap(element.getElementType());
		
		IStageElement exist = elementMap.get(element.getId());
		if(null != exist){
			try{
				deleteHandle(exist);
				exist.setStage(null);
			}catch (Exception e) {
				ChuanQiLog.error("离开场景时报错：",e);
			}finally{
				elementMap.remove(element.getId());
				//特别将可攻击元素另外归为一类
				if(ElementType.isFighter(element.getElementType())){
					getElementMap(ElementType.FIGHTER).remove(element.getId());
				}
			}
			
		}else{
			ChuanQiLog.error("{}:{}离开场景时不在场景中",element.getId(),element.getElementType());
		}
	}
	
	protected abstract void deleteHandle(IStageElement element);
	
	@Override
	public void enter(IStageElement element, int x, int y) {
		
		Map<Long,IStageElement> elementMap = getElementMap(element.getElementType());
		elementMap.put(element.getId(), element);
		element.setStage(this);
		element.setBornPosition(x, y);
		element.setPosition(x, y);
		
		addHandle(element);
		
		if(ElementType.isFighter(element.getElementType())){
			getElementMap(ElementType.FIGHTER).put(element.getId(), element);
			
			//激活仇恨
			hatredHandle((IFighter)element);
		}
	}
	
	
	/**
	 * 激活仇恨
	 * @param fighter
	 */
	private void hatredHandle(IFighter fighter){
		if(ElementType.isMonster(fighter.getElementType()) || ElementType.isRole(fighter.getElementType())){
			enterHatredHandle(fighter);
		}
	}
	
	
	/**
	 * 激活怪物仇恨
	 */
	private void enterHatredHandle(IFighter fighter) {
		
		if(ElementType.isMonster(fighter.getElementType())){
			IMonster monster = (IMonster)fighter;

			// 移动到主动怪物的视野内，会触发主动怪物仇恨增加
			Collection<IStageElement> aroundMonsters = this.getAroundEnemies(monster);
			if (null != aroundMonsters && aroundMonsters.size() > 0) {
				MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(monster.getMonsterId());
					
				for (IStageElement tmp : aroundMonsters) {
					if(ElementType.isRole(tmp.getElementType())){
						
						IFighter ifighter = (IFighter) tmp;

						//攻击红名怪物不处理仇恨(只走心跳)
						if(monsterConfig.isAttriackRedRold() || monsterConfig.getIfactive()){
							return;
						}
						
						int eyeshot = monsterConfig.getEyeshot();// 视野

						if (this.inScope(ifighter.getPosition(),monster.getPosition(), eyeshot, ScopeType.GRID)) {

							//判断目标是否在仇恨列表内
//							if(!monster.getHatredManager().checkTargetHatred(ifighter)){
//							}
							monster.getHatredManager().addActiveHatred(ifighter,1);
							monster.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
							
						}
						
					}
					
				}
			}
			
		}else if(ElementType.isRole(fighter.getElementType())){
			//进入场景的是玩家
			IRole enterRole = (IRole)fighter;
			// 根据玩家移动到主动怪物的视野内，会触发主动怪物仇恨增加
			Collection<IStageElement> aroundMonsters = this.getAroundEnemies(enterRole);
			if (null != aroundMonsters && aroundMonsters.size() > 0) {
				for (IStageElement tmp : aroundMonsters) {
					
					if(ElementType.isMonster(tmp.getElementType())){
						IMonster lookMonster = (IMonster)tmp;
						MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(lookMonster.getMonsterId());
						
						//攻击红名怪物和被动怪物不处理仇恨(只走心跳)
						if(monsterConfig.isAttriackRedRold() || monsterConfig.getIfactive()){
							return;
						}
						
						int eyeshot = monsterConfig.getEyeshot();// 视野
						if (this.inScope(enterRole.getPosition(),lookMonster.getPosition(), eyeshot, ScopeType.GRID)) {

							if (lookMonster.getStateManager().contains(StateType.XUNLUO)) {
								//判断目标是否在仇恨列表内
								if(!lookMonster.getHatredManager().checkTargetHatred(enterRole)){
									lookMonster.getHatredManager().addActiveHatred(enterRole,1);
								}
								lookMonster.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
							}
						}
					}
				}
			}
		}
	}
	
	protected abstract void addHandle(IStageElement element);
	
	/**
	 * 获取指定元素类型的存储map
	 * @param elementType 
	 */
	protected Map<Long,IStageElement> getElementMap(ElementType elementType){
		
		Map<Long,IStageElement> elementMap = elementMaps.get(elementType);
		if(null == elementMap){
			elementMap = new HashMap<Long, IStageElement>();
			elementMaps.put(elementType, elementMap);
		}
		
		return elementMap;
	}
	/**
	 * 获取所有元素集合
	 * @param elementType
	 * @return
	 */
	public List<IStageElement> getAllElements(ElementType elementType){
		return new ArrayList<>(getElementMap(elementType).values());
	}
	
	public Map<Long,IStageElement> getBaseStageRoles(){
		Map<Long,IStageElement> roles = getElementMap(ElementType.ROLE);
		if(roles == null){
			return null;
		}
		return new HashMap<Long, IStageElement>(roles);
	}
	
	public int getStageRoleCount(){
		return getElementMap(ElementType.ROLE).size();
	}
	
	@Override
	public <T extends IStageElement> T getElement(Long elementId, ElementType elementType) {
		return (T) getElementMap(elementType).get(elementId);
	}

	public IStageProduceManager getStageProduceManager() {
		return stageProduceManager;
	}

	public void setStageProduceManager(IStageProduceManager stageProduceManager) {
		this.stageProduceManager = stageProduceManager;
	}

	public boolean isCanPk() {
		return canPk;
	}

	public void setCanPk(boolean canPk) {
		this.canPk = canPk;
	}

	public boolean isAddPk(){
		return isAddPk;
	}
	
	public void setAddPk(boolean isAddPk) {
		this.isAddPk = isAddPk;
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
		Map<Long,IStageElement> elementMap = elementMaps.get(ElementType.ROLE);
		if(elementMap != null){
			return elementMap.keySet().toArray();
		}
		return null;
	}
	
	@Override
	public boolean isCanUseProp() {
		return true;
	}

	@Override
	public boolean isCanUseShenQi() {
		return true;
	}

	@Override
	public boolean isCanChangeSkill() {
		return true;
	}
	
	@Override
	public DeadDisplay getDeadHiddenState(){
		return DeadDisplay.EXIT;
	}
}
