package com.junyou.stage.model.fight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.cmd.InnerCmdType;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.harm.HarmType;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;

/**
 * 战斗PK值处理
 *@author  DaoZheng Yuan
 *@created 2013-4-3下午8:38:16
 */
@Component
public class FightPkValueResult {

	
	private static DiTuConfigExportService diTuConfigExportService;
	
	
	
	@Autowired
	public void setDiTuConfigExportService(
			DiTuConfigExportService diTuConfigExportService) {
		FightPkValueResult.diTuConfigExportService = diTuConfigExportService;
	}



	/**
	 * 计算PK值的场景
	 * @param stage
	 * @return true:计算PK值
	 */
	private static boolean havePkValueStage(IStage stage){
		if(stage == null){
			return false;
		}
		
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(stage.getMapId());
		if(diTuConfig == null){
			return false;
		}
		
		if(isHouDongNoPkValue(stage.getMapId())){
			return false;
		}	
		
		return true;
	}		
	
	
	/**
	 * 活动期间不加PK值
	 * @param mapId
	 * @return true:不加PK值
	 */
	private static boolean isHouDongNoPkValue(Integer mapId){
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(mapId);
		if(diTuConfig == null){
			return false;
		}
		
		//活动时间内杀死后地图不加PK值,死亡后不掉落物品
		if(diTuConfig.isNoPkValue()){
			
		}
		
		
		return false;
	}
	

	public static void pkValueResultHandle(IFighter fighter,IFighter target,IHarm harm){
		try {
			//必须成功伤害
			if(!HarmType.isSucessHarmType(harm.getType())){
				return;
			}
			
			//目标必须玩家和宝宝,否则不计算PK值和灰名
			if(!ElementType.isRole(target.getElementType()) && !ElementType.isPet(target.getElementType()) && !ElementType.isBiaoChe(target.getElementType())){
				return;
			}
			
			//除去需要副本及活动地图
			IStage stage = fighter.getStage();
			if(!havePkValueStage(stage)){
				return;
			}
			
			boolean attackIsRole = false;
			//攻击者必须是玩家和玩家的宝宝
			if(ElementType.isRole(fighter.getElementType()) || ElementType.isPet(fighter.getElementType()) ){
				attackIsRole = true;
			}
			
			//目标是否是宝宝
			boolean targetIsPet = false;
			boolean targetIsBiaoChe = false;
			//目标
			IRole targetRole = null;
			Long targetId = target.getId();
			if(ElementType.isBiaoChe(target.getElementType())){
				Biaoche targetBiaoChe = (Biaoche) target;
				targetId = targetBiaoChe.getOwner().getId();
				
				targetRole = targetBiaoChe.getOwner();
				
				targetIsBiaoChe = true;
			}else if(ElementType.isPet(target.getElementType())){
				Pet targetPet = (Pet) target;
				targetId = targetPet.getOwner().getId();
				
				targetRole = targetPet.getOwner();
				
				targetIsPet = true;
			}else{
				targetRole = (IRole)target;
			}
			
			//攻击者
			Long attackId = fighter.getId();
			if(ElementType.isPet(fighter.getElementType())){
				Pet targetPet = (Pet) fighter;
				attackId = targetPet.getOwner().getId();
			}
			
			boolean targetIsDead = false;
			
			//目标死亡
			if(target.getStateManager().isDead()){
				targetIsDead = true;
			}
			
			//目标非红名才 计算PK值和灰名才加PK值
			if(stage.isAddPk() && attackIsRole && !targetRole.isWicked()){
				
//				if(!targetIsPet && !targetIsBiaoChe && targetIsDead){
//					int addPkValue = roleHongmingExportService.getGongGongConfigPKVal();//杀人所增加的PK值
//					//增加PK值
//					roleHongmingExportService.addPkValue(attackId, addPkValue);
//				}
//				
//				//攻击都灰名
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(attackId, InnerCmdType.ROLE_HUIMING, null);
				}else{
					StageMsgSender.send2Bus(attackId, InnerCmdType.ROLE_HUIMING, null);
				}
			}
			
//			if(!targetIsPet && !targetIsBiaoChe && targetIsDead){
//				String stageId = target.getStage().getId();
//				//通知声望道具掉落 
//				StageMsgSender.send2StageInner(targetId, stageId, InnerCmdType.PK_DROP_GOODS, new Object[]{attackId,targetId,targetRole.isRedRole()});
//			}
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
}
