package com.junyou.stage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.io.export.SessionManagerExportService;
import com.junyou.stage.OnlineStateConstants;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.buff.specialeffect.ISpecialEffectHandler;
import com.junyou.stage.model.skill.buff.specialeffect.SpecialEffectHandlerFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.DirectMsgWriter;

@Service
public class BuffService{
	
	@Autowired
	private RoleBehaviourService  roleBehaviourService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	
	
	public void buffEnd(Long id, String buffCategory, Long ownerId,
			Integer elementType, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IFighter fighter = (IFighter)stage.getElement(ownerId, ElementType.convert(elementType));
		if(fighter == null){
			return;
		}
		
		if(null != fighter){
			fighter.getBuffManager().removeBuff(id, buffCategory);
			fighter.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		}
		
		//进地图无敌BUFF消失后，加怪物仇恨
		if(OnlineStateConstants.ONLINE_EFFECT_TYPE_ID.equals(buffCategory)){
			roleBehaviourService.enterStageActivateHatred(stageId, ownerId);
		}
	}

	public void specialEffectTrigger(Long id, String buffCategory, Long ownerId,
			Integer elementType, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IFighter buffOwner = (IFighter)stage.getElement(ownerId, ElementType.convert(elementType));
		if(buffOwner == null){
			return;
		}
		
		IBuff buff = buffOwner.getBuffManager().getBuff(buffCategory,id);
		
		if(null != buff){
			//触发逻辑
			triggerHandle(stage,buffOwner,buff);
			
			//继续触发
			buff.scheduleSpecialEffectTrigger();
		}
		
		
	}

	/**
	 * 触发处理
	 */
	private void triggerHandle(IStage stage, IFighter buffOwner, IBuff buff) {
		
		ISpecialEffectHandler handler = SpecialEffectHandlerFactory.getHandler(buff.getSpecialEffect());
		handler.triggerHandle(buff);
	}
	
	/**
	 * 开始剧情状态
	 * @param roleId
	 * @param stageId
	 */
	public void junQinStart(Long roleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		//开始剧情前加一个无敌状态
		changeAddWuDiState(role);
		
		sessionManagerExportService.addJunQingState(roleId);
	}
	
	/**
	 * 开始剧情前加一个无敌状态
	 * @param role
	 */
	private void changeAddWuDiState(IRole role){
		//加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setBeiKuan(false);
		
		Pet pet = role.getPet();
		if(pet != null){
			pet.getStateManager().add(new NoBeiAttack());
		}
	}
	
	/**
	 * 去除无敌状态
	 * @param role
	 */
	private void removeWuDiState(IRole role){
		//去除无敌状态
		role.getStateManager().remove(StateType.NO_ATTACKED);
		role.setBeiKuan(true);
		
		Pet pet = role.getPet();
		if(pet != null){
			pet.getStateManager().remove(StateType.NO_ATTACKED);
		}
	}
	
	/**
	 * 删除剧情状态
	 * @param roleId
	 * @param stageId
	 */
	public void junQinEnd(Long roleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		removeWuDiState(role);
		
		sessionManagerExportService.delJunQingState(roleId);
	}
	
}
