package com.junyou.stage.model.skill.harm;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;

public class HarmUtils {

	/**
	 * 该元素是否属于角色
	 * @param original 原始战斗元素
	 */
	public static boolean belong2Role(IFighter original) {
		
		if(ElementType.isRole(original.getElementType())){
			return true;
		}else if(ElementType.isPet(original.getElementType())){
			return true;
		}
		
		return false;
	}

	/**
	 * 是否是自己的战斗团队(宝宝,镖车等)
	 * @param attacker
	 * @param target
	 * @return true:是自己的战斗团队
	 */
	public static boolean isSelfFigher(IFighter attacker,IFighter target){
		if(attacker.getId().equals(target.getId())){
			return true;
		}
		
		if(ElementType.isRole(attacker.getElementType())){
			
			if(ElementType.isPet(target.getElementType())){
				//目标是宝宝
				Pet pet = (Pet) target;
				IRole owner = pet.getOwner();
				
				if(owner != null && attacker.getId().equals(pet.getOwner().getId())){
					return true;
				}
			}
		}else if(ElementType.isPet(attacker.getElementType())){
			Pet attPet = (Pet) attacker;
			
			if(ElementType.isPet(target.getElementType())){
				
				//目标是宝宝
				Pet pet = (Pet) target;
				IRole owner = pet.getOwner();
				
				if(owner != null && attPet.getOwner() != null && attPet.getOwner().getId().equals(pet.getOwner().getId())){
					return true;
				}
			}
		}
		
		if(ElementType.isRole(target.getElementType())){
			
			if(ElementType.isPet(attacker.getElementType())){
				//攻击者是宝宝
				Pet pet = (Pet) attacker;
				IRole owner = pet.getOwner();
				
				if(owner != null && target.getId().equals(pet.getOwner().getId())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 获取该战斗元素所属玩家对象
	 */
	public static IRole getBelongRole(IFighter original) {
		if(ElementType.isRole(original.getElementType())){
			return (IRole)original;
		}else if(ElementType.isPet(original.getElementType())){
			Pet pet = (Pet)original;
			return pet.getOwner();
		}
		
		return (IRole)original;
	}

	/**
	 * 该战斗元素是否属于boss怪
	 */
	public static boolean belong2BossMonster(IFighter original) {
		
		if(ElementType.isRole(original.getElementType())){
			return false;
		}
		
		/*if(ElementType.isFaBao(original.getElementType())){
			
			IFighter owner = ((IFaBao)original).getOwner();
			if(ElementType.isMonster(owner.getElementType())){
				
				if(ConfigureExportServices.getMonsterExportService().load(((IMonster)owner).getMonsterId()).getDifficulty().equals(MonsterDifficulty.BOSS)){
					return true;
				}
				
			}
		}*/
		
		return true;
	}

	/**
	 * 获取战斗元素的所属的怪物对象
	 */
	public static IMonster getBelongMonster(IFighter original) {
		
		/*if(ElementType.isFaBao(original.getElementType())){
			IFighter owner = ((IFaBao)original).getOwner();
			return (IMonster)owner;
		}*/
		
		return (IMonster)original;
	}

}
