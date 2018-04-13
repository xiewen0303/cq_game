package com.junyou.stage.model.fight;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.publicconfig.configure.export.RoleBasePublicConfig;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.CampRelationExportService;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.monster.TutengMonster;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.harm.HarmUtils;
import com.kernel.spring.container.DataContainer;

public class BattleModeRelation {
	
	private static DataContainer dataContainer;
	private static CampRelationExportService campRelationExportService;
	
	
	@Autowired
	public void setCampRelationExportService(
			CampRelationExportService campRelationExportService) {
		BattleModeRelation.campRelationExportService = campRelationExportService;
	}


	public void setDataContainer(DataContainer _dataContainer) {
		dataContainer = _dataContainer;
	}

	
	/**
	 * 是否是新手保护
	 * @param target
	 * @return true:是
	 */
	private static boolean isNewProtect(IFighter attacker ,IFighter target){
		IStage stage = target.getStage();
		if(stage == null){
			return false;
		}

		DiTuConfig diTuConfig = StageConfigureHelper.getDiTuConfigExportService().loadDiTu(stage.getMapId());
		if(diTuConfig == null){
			return false;
		}
		
		//第一  验证地图是否开启新手保护
		if(!diTuConfig.isNewProtect()){
			return false;
		}
		
		RoleBasePublicConfig roleBasePublicConfig = StageConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_ROLE_BASE);
		if(roleBasePublicConfig == null){
			return false;
		}
		
		int protectLevel = roleBasePublicConfig.getProtectLevel();
		//配置等级保护异常处理
		if(protectLevel <= 0){
			return false;
		}

		IRole targetRole = null;
		if(ElementType.isRole(target.getElementType())){
			targetRole = (IRole) target;
		}else if(ElementType.isPet(target.getElementType())){
			Pet pet = (Pet) target;
			targetRole = pet.getOwner();
		}else{
			return false;
		}
		
		//第二  验证目标保护等级
		if(targetRole != null && targetRole.getLevel() <= protectLevel){
			return true;
		}
		
		
		IRole attackRole = null;
		if(ElementType.isRole(attacker.getElementType())){
			attackRole = (IRole) attacker;
		}else if(ElementType.isPet(attacker.getElementType())){
			Pet pet = (Pet) attacker;
			attackRole = pet.getOwner();
		}else{
			return false;
		}
		
		//第二  验证攻击者保护等级
		if(attackRole != null && attackRole.getLevel() <= protectLevel){
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 目标是否在不可攻击方位内
	 * @return 
	 */
	public static boolean isEnemy(BattleMode battleMode, IFighter attacker,
			IFighter target) {
		
		//怪物所有模式都可攻击
		if(attacker.getElementType().equals(ElementType.MONSTER) || target.getElementType().equals(ElementType.MONSTER)){
			return true;
		}
		if(target.getElementType().equals(ElementType.TUTENG)){
			long tarGuildId = ((TutengMonster)target).getGuildId();
			if(tarGuildId > 0){
				Long guildId = ((IRole)attacker).getBusinessData().getGuildId();
				if(guildId != null && guildId.equals(tarGuildId)){
					return false;//所有模式都不可攻击本公会图腾
				}
				return true;
			}
			return false;
		}
		
		//是否是自己的战斗团队(宝宝,镖车等)
		if(HarmUtils.isSelfFigher(attacker, target)){
			return false;
		}

		//是否是新手保护
		if(isNewProtect(attacker,target)){
			return false;
		}
		switch (battleMode) {
		case PEACE:
			//和平模式
			
			//优先考虑<无视模式可攻击列表>
//			if(ElementType.isRole(attacker.getElementType()) &&	((IRoleHatredManager)attacker.getHatredManager()).inIgnoreBattleAttackableList(target)){
//				
//				return true;
//				
//			}else if(ElementType.isPet(attacker.getElementType())){
//				Pet pet = (Pet)attacker;
//				 
//				if(((IRoleHatredManager)pet.getOwner().getHatredManager()).inIgnoreBattleAttackableList(target)){
//					return true;
//				}
//			}
			
			
//			if(ElementType.isBiaoChe(target.getElementType())){
//				BiaoChe biaoChe = (BiaoChe) target;
//				
//				if(attacker.getId().equals(biaoChe.getOwner().getId())){
//					
//					return false;
//				}else{
//					
//					return true;
//				}
//			}

			
			return false;
			
		case TEAM:
			//组队模式
			
			IRole attackCheck = findRole(attacker);
			if(attackCheck == null){
				return true;
			}
			
			IRole targetCheck = findRole(target);
			if(targetCheck == null){
				return true;
			}
			
			
			IRole attackerRole = attackCheck;
			IRole targetRole = targetCheck;
			Integer teamId = attackerRole.getTeamId();
			if(teamId != null && teamId.equals(targetRole.getTeamId())){
				return false;
			}
			
			return true;
		case GUILD:
			//公会模式
			
			IRole attackFind = findRole(attacker);
			if(attackFind == null){
				return true;
			}
			
			IRole targetFind = findRole(target);
			if(targetFind == null){
				return true;
			}
			
			
			target = targetFind;
			attacker = attackFind;
			
			Long guildId = ((IRole)attacker).getBusinessData().getGuildId();
			if(null != guildId){
				
				if(guildId.equals(((IRole)target).getBusinessData().getGuildId())){
					//相同公会
					return false;
				}
			}
			
			return true;
			
		case SHAN_E:
			//善恶PK模式 

			IRole targetSeach = findRole(target);
			if(targetSeach == null){
				return true;
			}
			
			return targetSeach.isWicked();
			
		case ALL:
			//全体模式
			
			return true;
			
		case SAME_SERVER:
			//本服模式
			IRole attackRole = findRole(attacker);
			if(attackRole == null){
				return true;
			}
			
			IRole tarRole = findRole(target);
			if(tarRole == null){
				return true;
			}
			
			return !attackRole.getBusinessData().getServerId().equals(tarRole.getBusinessData().getServerId());
			
		default:
			
			if(isSameCamp(battleMode, attacker, target)){
				return false;
			}else{
				return true;
			}
		}
		
	}
	
	private static IRole findRole(IFighter fighter){
		IRole findRole = null;
		if(ElementType.isRole(fighter.getElementType())){
			findRole = (IRole) fighter;
		}else if(ElementType.isPet(fighter.getElementType())){
			Pet pet = (Pet) fighter;
			findRole = pet.getOwner();
		}
		return findRole;
	}
	
	/**
	 * 是否是同阵营类型
	 * @param battleMode
	 * @param attacker
	 * @param target
	 * @return true:是非敌对阵营
	 */
	public static boolean isSameCamp(BattleMode battleMode, IFighter attacker,
			IFighter target) {
		
		return attacker.getCamp().equals(target.getCamp());
	}
	
	
	/**
	 *  是否是可攻击
	 * @param attacker
	 * @param target
	 * @return true:可攻击
	 */
	private static boolean isCheckNoSelfTeam(IFighter attacker,IFighter target){
		if(target.getId().equals(attacker.getId())){
			return false;
		}
		
		
//		if(ElementType.isRole(attacker.getElementType()) && ElementType.isRole(target.getElementType())){
//			
//			IRole attacRole = (IRole)attacker;
//			IRole targetRole = (IRole)target;
//			
//			//同队人过滤
//			if(!teamCheck(attacRole, targetRole)){
//				return false;
//			}
//		}
		
		
		return true;
	}
	
	/**
	 * 是否是敌对阵营
	 * @param attackerCamp
	 * @param checkCamp
	 * @return true:是敌对阵营 ,false:不是敌对阵营
	 */
	private static boolean isEnemyCamp(String attackerCamp,String checkCamp){
		return campRelationExportService.isEnemy(attackerCamp, checkCamp);
	}

}
