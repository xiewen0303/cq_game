package com.junyou.stage.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.export.PathInfoCopy;
import com.junyou.gameconfig.export.PathNodeCopy;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.StageOutputWrapper;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.configure.export.impl.SkillConfigExportService;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.hatred.IHatred;
import com.junyou.stage.model.core.heartbeat.AiHeartBeatTimeCarlc;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.StateEventType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.pet.PetVo;
import com.junyou.stage.model.fight.SkillProcessHelper;
import com.junyou.stage.model.hatred.IRoleHatredManager;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.model.state.AiBackState;
import com.junyou.stage.model.state.AiFightState;
import com.junyou.stage.model.state.AiMoveState;
import com.junyou.stage.model.state.AiXunLuoState;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.AStarUtil;
import com.junyou.stage.utils.BStarUtil;
import com.kernel.spring.container.DataContainer;

/**
 * 宠物
 * @author DaoZheng Yuan
 * 2013-11-5 下午4:00:00
 */
@Service
public class PetService{

	@Autowired
	private MonsterExportService monsterExportService;
	@Autowired
	private SkillConfigExportService skillConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	
	
	
	public void heartBeatHandle(Long petId, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		Pet pet = (Pet)stage.getElement(petId, ElementType.PET);
		if(pet == null){
			return;
		}
		
		IFighter role = pet.getOwner();
		if(role == null || !role.getStage().equals(stage)){
			stage.leave(pet);//主人不存在，离开场景
			return;
		}
		
		Point oldPoint = pet.getPosition();
		try{
			pet.petHuiFuHp();
			
			//获取攻击目标
			IFighter target = getTarget(stage,pet,role);
			
			refreshState(stage,pet,role,target);
			
			if(pet.getStateManager().contains(StateType.FIGHT)){
				fight(stage,pet, target);
			}else if(pet.getStateManager().contains(StateType.XUNLUO)){
				xunluo(pet, target);
			}else{
				move(pet,role,stage);
			}
			
		} catch (Exception e) {
			ChuanQiLog.error("pet heart beat error",e);
		}finally{
			//计算下次心跳间隔,不在(战斗，巡逻，返回)状态则无心跳
			int delay = calNextOptDelay(pet,oldPoint) - 100;
			pet.getAi().schedule(delay,TimeUnit.MILLISECONDS);
		}
		
	}
	/**
	 * 战斗操作
	 * @param pet	宠物
	 * @param target	目标
	 */
	private void fight(IStage stage,Pet pet,IFighter target){
		for (ISkill skill : pet.getSkills()) {
			SkillConfig skillConfig = skill.getSkillConfig();
			if(stage.inScope(target.getPosition(), pet.getPosition(), skillConfig.getRange(),ScopeType.PIXEL)){
				boolean checkFlag = SkillProcessHelper.skillReadyFireCheck(stage, pet, skill);
				if(!checkFlag){
					continue;
				}
				skillExecute(pet,target,skill,stage);
				return;
			}
		}
		//不能攻击朝目标移动
		move(pet, target, stage);
	}
	/**
	 * 巡逻操作
	 */
	private void xunluo(Pet pet,IFighter target) {
		
		if(pet.getStateManager().isForbidden(StateEventType.MOVE)){
			return;
		}
		
		if(target != null){
			pet.getStateManager().add(new AiFightState());
			pet.getAi().interruptSchedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
		}
			
	}
	
	private int calNextOptDelay(Pet pet,Point oldPoint){
		return AiHeartBeatTimeCarlc.calcHeartBeatTime(pet, oldPoint.getX(), oldPoint.getY());
	}
	
	public void clearPetHandle(Long userRoleId,String stageId,Long petId){
		PetVo petVo = dataContainer.getData(GameConstants.COMPONENT_NAME_PET, userRoleId.toString());
		if(petVo != null){
			dataContainer.removeData(GameConstants.COMPONENT_NAME_PET, userRoleId.toString());
		}
		
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		Pet pet = (Pet)stage.getElement(petId, ElementType.PET);
		if(pet != null){
			stage.leave(pet);
		}
		
	}
	
	
	private int[] getBstarFind(Point from,Point target,IStage stage){
		Point[] points = BStarUtil.findPints(from, target);
		
		int[] finalPint = new int[]{from.getX(),from.getY()};
		for (Point point : points) {
			int faX = from.getX() + point.getX();
			int faY = from.getY()+point.getY();
			
			if(stage.checkCanUseStagePoint(faX ,faY, PointTakeupType.BEING)){
				finalPint = new int[]{faX,faY};
				break;
			}
		}
		
		return finalPint;
	}
	
	/**
	 * 移动操作
	 */
	private void move(Pet pet, IFighter target, IStage stage) {
		
		if(pet.getStateManager().isForbidden(StateEventType.MOVE)){
			return;
		}
		
		if(target == null || target.getStateManager().isDead()){
			//TODO wind
			Object[] moveData = (Object[])pet.getMoveData();
			
			if(pet.getPosition().getX() == Integer.parseInt(moveData[1].toString()) && pet.getPosition().getY() == Integer.parseInt(moveData[2].toString())){
				//ChuanQiLog.error("4-----selfXY:"+pet.getPosition().getX()+","+pet.getPosition().getY()+"=========targetXY:"+Integer.parseInt(moveData[1].toString())+","+Integer.parseInt(moveData[2].toString()));
				return;
			}
			StageMsgSender.send2Many(stage.getSurroundRoleIds(pet.getPosition()), ClientCmdType.BEHAVIOR_MOVE, pet.getMoveData());
			return;
		}
		if(stage.inScope(pet.getPosition(), target.getPosition(), GREN_MIN,ScopeType.GRID)){
			return;
		}
		
		int[] moveXy = getBstarFind(pet.getPosition(), target.getPosition(), stage);
		/**
		 * 如果发现移动目标点与当前带你相同则不移动
		 */
		//ChuanQiLog.error("1-----selfXY:"+pet.getPosition().getX()+","+pet.getPosition().getY()+"=========targetXY:"+moveXy[0]+","+moveXy[1]);
		if(pet.getPosition().getX() == moveXy[0] && pet.getPosition().getY() == moveXy[1]){
			//ChuanQiLog.error("2-----selfXY:"+pet.getPosition().getX()+","+pet.getPosition().getY()+"=========targetXY:"+moveXy[0]+","+moveXy[1]);
			return;
		}
		
		stage.moveTo(pet, moveXy[0], moveXy[1]);
		//ChuanQiLog.error("3-----selfXY:"+JSONObject.toJSONString(pet.getMoveData()));
		StageMsgSender.send2Many(stage.getSurroundRoleIds(pet.getPosition()), ClientCmdType.BEHAVIOR_MOVE, pet.getMoveData());
		
		//宝宝仇恨业务
//		hatredMonterHandle(pet, stage);
		pet.getStateManager().add(new AiMoveState());//设为移动状态
	}
	
	/**
	 * 宝宝仇恨业务
	 * @param pet
	 * @param stage
	 */
	private void hatredMonterHandle(Pet pet, IStage stage) {
		Collection<IStageElement> aroundMonsters = stage.getAroundEnemies(pet);
		if (null != aroundMonsters && aroundMonsters.size() > 0) {
			
			for (IStageElement tmp : aroundMonsters) {
				if(ElementType.isMonster(tmp.getElementType())){
					IMonster monster = (IMonster) tmp;
					
					MonsterConfig config = monsterExportService.load(monster.getMonsterId());
					
					//不是主动怪不处理仇恨
					if(config.getIfactive()){
						continue;
					}
					
					int eyeshot = config.getEyeshot();// 视野
					if (!config.isAttriackRedRold() && stage.inScope(monster.getPosition(),pet.getPosition(), eyeshot, ScopeType.GRID)) {
						
						//判断目标是否在仇恨列表内
//						if(!monster.getHatredManager().checkTargetHatred(pet)){
//						}
						monster.getHatredManager().addActiveHatred(pet, 1);
						monster.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
					}
				}
			}
		}
	}


	/**
	 * 技能释放操作
	 */
	private void skillExecute(Pet pet, IFighter target, ISkill skill,IStage stage) {
		SkillProcessHelper.skillReadyFireConsume(stage, pet, skill);
		
		IMsgWriter msgWriter = BufferedMsgWriter.getInstance();
			
		//目标确认
		Object[] helpParams = skill.getSkillFirePath().createHelpParams(stage,skill,pet,target);
		Collection<IFighter> targets = SkillProcessHelper.chooseTargets(stage, pet, skill, helpParams);
		
		//伤害计算
		List<Object[]> harmMsgs	 = SkillProcessHelper.fight(pet, skill, targets, msgWriter);
		if(null != harmMsgs && harmMsgs.size() > 0){
			StageMsgSender.send2Many(stage.getSurroundRoleIds(pet.getPosition()), ClientCmdType.SKILL_FIRE, StageOutputWrapper.skillHarm(pet,skill,harmMsgs.toArray(),helpParams));
			
		}
		
		//消息刷出
		pet.getFightStatistic().flushChanges(msgWriter);
		msgWriter.flush();
		
	}
	
	private MonsterConfig getMonsterConfig(String monsterId){
		return monsterExportService.load(monsterId);
	}
	
	
	/** 
	 * 移动最近距离  格子数  
	 */
	private final int GREN_MIN = 1;
	/** 
	 * 非战斗状态-巡逻  格子数  
	 */
	private final int GREN_XL = 4;
	
	/**
	 * 非战斗状态-跟随 格子数  
	 */
	private final int GREN_BK = 4;
	
	/**
	 * 战斗状态格子数 
	 */
	private final int GREN_GJ = 20;
	
	/**
	 * 糖宝视野范围 
	 */
	private final int GREN_EYE = 10;
	
	private void refreshState(IStage stage, Pet pet, IFighter role,IFighter target) {
		
		//不在视野内,瞬移过去
		if(noInSightHandle(stage, pet, role, new AiBackState())){
			return;
		}
		
		if(pet.getStateManager().contains(StateType.XUNLUO)){
			//巡逻状态下
			if(target != null){
				pet.getStateManager().add(new AiFightState());
				pet.getAi().interruptSchedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
				return;
			}
			
			//1b、如果离雇主在一定范围内，进入跟随状态
			if(!stage.inScope(pet.getPosition(), role.getPosition(), GREN_XL,ScopeType.GRID)){
				pet.getStateManager().add(new AiBackState());
				return;
			}
			
			return;
		}
		
		if(pet.getStateManager().contains(StateType.MOVE)){
			//移动状态下，有仇恨，则转换状态到战斗,没有则转为返回状态
			if(!stage.inScope(pet.getPosition(), role.getPosition(), GREN_GJ,ScopeType.GRID) || stage.inScope(pet.getPosition(), role.getPosition(),GREN_BK,ScopeType.GRID)){
				pet.getStateManager().add(new AiXunLuoState());
			}else if(null != target){
				pet.getStateManager().add(new AiFightState());
			}else{
				pet.getStateManager().add(new AiBackState());
			}
			return;
		}
		
		if(pet.getStateManager().contains(StateType.FIGHT)){
			//不在范围内
			if(!stage.inScope(pet.getPosition(), role.getPosition(), GREN_GJ,ScopeType.GRID)){
				pet.getStateManager().add(new AiXunLuoState());
				return;
			}
			
			//没有目标
			if(target == null){
				pet.getStateManager().add(new AiXunLuoState());
			}
			return;
		}
		
		if(pet.getStateManager().contains(StateType.BACK)){
			//跟随状态下，
			//1b、如果离雇主在一定范围内，进入巡逻状态
			if(stage.inScope(pet.getPosition(), role.getPosition(),GREN_BK,ScopeType.GRID)){
				pet.getStateManager().add(new AiXunLuoState());
				return;
			}
			
		}
	}
	
	
	/**
	 * 不在视野内业务处理
	 * @param stage
	 * @param huoBan
	 * @param employer
	 * @param state
	 */
	private boolean noInSightHandle(IStage stage,Pet pet, IStageElement role,IState state){
		//不在视野内业务
//		if(!stage.inScope(pet.getPosition(), role.getPosition(), GREN_EYE,ScopeType.GRID)){
		if(!stage.inSight(pet, role)){
			pet.getStateManager().add(state);
			
			Point targetPoint = role.getPosition();
			
			PathInfoCopy pathInfoCopy = ((AoiStage)pet.getStage()).getPathInfo();
			PathNodeCopy[] pathNode = AStarUtil.findPath(pet.getPosition(), targetPoint,pathInfoCopy, pet.getPathNodeSize());
			
			//瞬移
			if(null == pathNode || pathNode.length == 1){
				Point rolePoint = role.getPosition();
				stage.teleportTo(pet, rolePoint.getX(), rolePoint.getY());
				StageMsgSender.send2Many(stage.getSurroundRoleIds(pet.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, StageOutputWrapper.teleport(pet));
			}else{
				int size = pathNode.length;
				stage.teleportTo(pet, pathNode[size-1].getX(), pathNode[size-1].getY());
				StageMsgSender.send2Many(stage.getSurroundRoleIds(pet.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, StageOutputWrapper.teleport(pet));
			}
			return true;
		}
		
		return false;
	}
	
	private IFighter getTarget(IStage stage,Pet pet, IFighter role) {
		
		//选取雇主的攻击目标为伙伴的优先攻击目标
		
		IHatred hatred = ((IRoleHatredManager)role.getHatredManager()).getLastActiveAttackTarget();
		if(null == hatred) return null;
		IFighter target = (IFighter)stage.getElement(hatred.getId(), hatred.getElementType());
		
		return target;
	}
}
