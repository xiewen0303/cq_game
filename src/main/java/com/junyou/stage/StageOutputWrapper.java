package com.junyou.stage;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.constants.AttributeType;
import com.junyou.stage.model.core.attribute.IFightAttribute;
import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.element.role.IRole;

public class StageOutputWrapper {

	public static Object skillFire(IFighter fighter, ISkill skill,IFighter target) {
		return new Object[]{skill.getCategory(),fighter.getId(),new Object[]{target.getId()}};
	}

	public static Object move(IElement monster) {
		
		if(monster.getBornPosition().equals(monster.getPosition())){
			return new Object[]{monster.getId(),monster.getPosition().getX(),monster.getPosition().getY(),0};
		}else{
			return new Object[]{monster.getId(),monster.getPosition().getX(),monster.getPosition().getY(),0};
		}
		
	}
	
	
	public static Object teleport(IElement fighter) {
		return new Object[]{fighter.getId(),fighter.getPosition().getX(),fighter.getPosition().getY()};
	}
	
	
	public static Object applyWorldChangeMap(String mapId,int x,int y) {
		return new Object[]{mapId,x,y};
	}
	

	public static Object changeEquip(String userRoleId, Object equipIds,
			Object weaponInfo) {
		return new Object[]{userRoleId,equipIds,weaponInfo};
	}

	public static Object fightEnd(IFighter role) {
		return role.getId();
	}

	public static Object faBaoRevive(String id, boolean miaoCd) {
		return new Object[]{id,miaoCd};
	}

	public static Object faBaoDead(String id,Long reviveLeftTime) {
		return new Object[]{id,reviveLeftTime};
	}

	
	
	public static Object getAttribute(IRole role) {
		
		IFightAttribute fightAttribute = role.getFightAttribute();
		
		
		Map<Integer,Long> attribute = new HashMap<>();
		//生命
		attribute.put(AttributeType.MaxHpIndex, fightAttribute.getMaxHp());
		attribute.put(AttributeType.AttackIndex, fightAttribute.getAttack());
		attribute.put(AttributeType.DefenseIndex, fightAttribute.getDefense());
		attribute.put(AttributeType.ShanBiIndex, fightAttribute.getShanBi());
		attribute.put(AttributeType.MingZhongIndex, fightAttribute.getMingZhong());
		attribute.put(AttributeType.BaoJiIndex, fightAttribute.getBaoJi());
		attribute.put(AttributeType.KangBaoIndex, fightAttribute.getKangBao());
		attribute.put(AttributeType.SpeedIndex, fightAttribute.getSpeed());
		attribute.put(AttributeType.ZplusIndex, fightAttribute.getZhanLi());
		
		return attribute;
		
	}

	public static Object skillHarm(IFighter fighter,ISkill skill, Object[] harmMsg,Object[] helpParams) {
		return new Object[]{
				skill.getCategory(),
				harmMsg,
				helpParams,
				fighter.getId()
		};
	}
	
	public static Object readyAttackSkill(String skillCategory, Integer leftCd,
			Integer readyAttackDuration) {
		return new Object[]{skillCategory,leftCd,readyAttackDuration};
	}
	
	public static Object readyAttackSkillError(String skillCategory) {
		return skillCategory;
	}


	public static Object changeEquipTeamNofity(IRole role) {
		return new Object[]{role.getId(),null,role.getBusinessData().getEquipData().getEquipIds()};
	}



	public static Object daZuo(IRole role) {
		return new Object[]{1,role.getId()};
	}

	public static Object daZuoShuangXiu(IRole role, IRole target) {
		return new Object[]{1,role.getId(),target.getId()};
	}

	public static Object daZuoExit(IRole role) {
		return role.getId();
	}
	
	public static Object gmOperationFail(int errorCode){
		return new Object[]{0,errorCode};
	}
}
