package com.junyou.stage.model.element.role;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.equip.export.EquipExportService;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.element.role.business.Equip;

@Component
public class RoleFactoryHelper {

	private static EquipExportService equipExportService;
	
	@Autowired
	public void setEquipExportService(EquipExportService equipExportService) {
		RoleFactoryHelper.equipExportService = equipExportService;
	}
	
	/**
	 * 重置BaseAttributeType类型属性数据
	 * @param attributeMap
	 * @param role
	 * @param baseAttributeType
	 */
	public static void setRoleBaseAttrs(Map<String, Long> attributeMap,IRole role,BaseAttributeType baseAttributeType){
		if(attributeMap != null ){
			role.getFightAttribute().setBaseAttribute(baseAttributeType, attributeMap);
		}
	}
	
//	/**
//	 * 增加装备影响的角色战斗属性
//	 * @param equip
//	 * @param role
//	 * @param baseAttributeType
//	 */
//	public static void addEquip(Equip equip, IRole role, BaseAttributeType baseAttributeType){
//		if( equip != null ){
//			Map<String, Integer> attributeMap = equipExportService.getEquipAllAttribute(equip.getGoodsId(), equip.getQianghuaLevel());
//			
//			if(attributeMap != null){
//				role.getFightAttribute().incrBaseAttribute(baseAttributeType, attributeMap);//incrBaseAttribute(baseAttributeType, attributeMap);
//			}
//		}
//	}
	
	
	/**
	 * 获得装备基础属性
	 * @param equip
	 * @param role
	 * @param baseAttributeType
	 */
	public static Map<String, Long> getEquipBaseAttrs(Equip equip,ContainerType type){
		if( equip != null ){
			return  equipExportService.getEquipAllAttribute(equip.getTpVal(),equip.getGoodsId(), equip.getQianghuaLevel(),equip.getZhuShenLevel(),type);
		}
		return null;
	}
	
	public static void addEquipSuit(Map<String, Long> attributeMap,IRole role,BaseAttributeType baseAttributeType){
		if(attributeMap != null ){
			role.getFightAttribute().incrBaseAttribute(baseAttributeType, attributeMap);
		}
	}
	
	/**
	 * 清空baseAttributeType装备的战斗属性
	 * @param role
	 * @param baseAttributeType
	 * @param isRefresh 是否刷新
	 */
	public static void clearEquip(IRole role, BaseAttributeType baseAttributeType,boolean isRefresh){
		role.getFightAttribute().clearBaseAttribute(baseAttributeType, isRefresh);
	}
	
	/**
	 * 增加技能
	 * @param role
	 * @param skillCategory
	 * @param level
	 * @param baseAttributeType
	 */
	public static void addSkill(IRole role, String skillCategory){
		ISkill skill = role.getSkillFactory().create(skillCategory, 1);
		if(skill == null){
			return;
		}
		role.addSkill(skill);
	}
	
	/**
	 * 增加技能
	 * @param role
	 * @param skillCategory
	 * @param level
	 * @param baseAttributeType
	 */
	public static void addSkill(IRole role, String skillCategory, Integer level, BaseAttributeType baseAttributeType){
		ISkill skill = role.getSkillFactory().create(skillCategory, level);
		if(skill == null){
			return;
		}
		role.addSkill(skill);
		
	}
	
	/**
	 * 删除技能
	 * @param role
	 * @param skillCategory
	 * @param level
	 * @param baseAttributeType
	 */
	public static void removeSkill(IRole role, String skillCategory, Integer level, BaseAttributeType baseAttributeType){
		//清除旧技能带来的影响(属性)
		ISkill oldSkill =  role.getSkill(skillCategory);
		if(null != oldSkill){
			
		}
	}
}
