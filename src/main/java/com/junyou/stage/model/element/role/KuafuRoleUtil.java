package com.junyou.stage.model.element.role;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.attribute.IBaseAttribute;

public class KuafuRoleUtil {

	/**
	 * 根据场景role获取所有类型属性
	 * @param role
	 * @return Map<Integer,Map<String,Float>> 
	 */
	public static Map<Integer,Map<String,Long>> getRoleAllAttribute(IRole role){
		if(role == null || role.getFightAttribute() == null){
			return null;
		}
		
		Map<Integer,Map<String,Long>> roleAllMap = new HashMap<>();
		
		for (BaseAttributeType innerType : BaseAttributeType.values()) {
			IBaseAttribute baseAttribute = role.getFightAttribute().getBaseAttributeMap(innerType);
			if(baseAttribute != null){
				
				Map<String, Long> innerAttri =  baseAttribute.toMap();
				if(innerAttri != null){
					roleAllMap.put(innerType.getVal(), innerAttri);
				}
			}
			
		}
		
		return roleAllMap;
	}
	
	
	/**
	 * 给场景role复原属性
	 * @param roleAllMap
	 * @param role
	 */
	public static void saveKuafuAttribute2Role(Map<Integer,Map<String,Long>> roleAllMap,IRole role){
		if(roleAllMap == null || role == null){
			return;
		}
		
		for (Entry<Integer, Map<String, Long>> entry : roleAllMap.entrySet()) {
			BaseAttributeType type = BaseAttributeType.getBaseAttributeTypeByVal(entry.getKey());
			if(type != null){
				role.getFightAttribute().initBaseAttribute(type,entry.getValue());
			}
		}
	}
	
	/**
	 * 复制角色属性
	 * @param from	来源
	 * @param to	目标
	 */
	public static void copyRoleAttribute(IRole from,IRole to){
		if(from == null || to == null || from.getFightAttribute() == null || to.getFightAttribute() == null){
			return;
		}
		for (BaseAttributeType innerType : BaseAttributeType.values()) {
			IBaseAttribute baseAttribute = from.getFightAttribute().getBaseAttributeMap(innerType);
			if(baseAttribute != null){
				to.getFightAttribute().initBaseAttribute(innerType, baseAttribute.toMap());
			}
		}
	}
	
	/**
	 * 根据场景role获取所有类型属性
	 * @param role
	 * @return Map<Integer,Map<String,Float>> 
	 */
	public static Map<Integer,Map<String,Long>> getRoleAllAttributeExceptBuff(IRole role){
		if(role == null || role.getFightAttribute() == null){
			return null;
		}
		
		Map<Integer,Map<String,Long>> roleAllMap = new HashMap<>();
		
		for (BaseAttributeType innerType : BaseAttributeType.values()) {
			if(innerType.equals(BaseAttributeType.BUFF) || innerType.equals(BaseAttributeType.HONGMING)){
				continue;
			}
			IBaseAttribute baseAttribute = role.getFightAttribute().getBaseAttributeMap(innerType);
			if(baseAttribute != null){
				
				Map<String, Long> innerAttri =  baseAttribute.toMap();
				if(innerAttri != null){
					roleAllMap.put(innerType.getVal(), innerAttri);
				}
			}
			
		}
		
		return roleAllMap;
	}
	
}
