package com.junyou.bus.equip;

import java.util.List;

import com.junyou.bus.bag.export.RoleItemExport;

/**
 * 装备
 *@author  DaoZheng Yuan
 *@created 2012-3-13上午10:23:08
 */
public class EquipOutputWrapper {

//	/**
//	 * 获取角色装备goodsIds
//	 * @param roleEquipSlots
//	 * @return [0:goodsId,1:goodsId,....]
//	 */
//	public static Object[] getRoleEquipClothIds(List<RoleEquipSlot> roleEquipSlots){
//		Object[] result = null;
//		if(roleEquipSlots != null){
//			result = new Object[roleEquipSlots.size()];
//			
//			RoleEquipSlot roleEquipSlot = null;
//			for (int i = 0; i < roleEquipSlots.size(); i++) {
//				roleEquipSlot = roleEquipSlots.get(i);
//				
//				result[i] = roleEquipSlot.getGoodsId();
//			}
//		}
//		
//		return result;
//	}
	
	
	/**
	 * 获取角色装备属性  (给区块的人)某人的当前装
	 * @param roleEquipSlots
	 * @return
	 */
	public static Object[] getRoleEquipAttribute(List<RoleItemExport> roleEquipSlots){
		Object[] equips = null;
		if(roleEquipSlots != null){
			equips = new Object[roleEquipSlots.size()];
			RoleItemExport roleEquipSlot = null;
			for (int i = 0; i < roleEquipSlots.size(); i++) {
				roleEquipSlot = roleEquipSlots.get(i);
				equips[i] = getSingleEquipAttribute(roleEquipSlot);
			}
		}
		return equips;
	}
	
	private static Object[] getSingleEquipAttribute(RoleItemExport equip){
		//若有新的数据  在数组索引最后添加即可 TODO
		return new Object[]{equip.getGuid(),equip.getGoodsId(),equip.getQianhuaLevel(),equip.getSlot(),equip.getExpireTime(),equip.getRandomAttrs(),equip.getZhushenLevel()==null?0:equip.getZhushenLevel(),equip.getTipinValue()};//,equip.getCurrentDurability(),equip.getZfLevel()};
	}
	
	public static Object[] getWingReturn(int userId, Long userRoleId, String roleName, Long guid, String goodId){ 
		return new Object[]{new Object[]{userId,userRoleId,roleName},guid,goodId};
	}
	
	 
}
