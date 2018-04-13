package com.junyou.stage.model.element.role.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoleEquipData implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 装备IDs
	 */
	private Object[] equipIds;
	/**
	 * 所有装备
	 */
	private List<Equip> equips;
//	/**
//	 * 特效
//	 */
//	private Object weaponInfo;
	public Object[] getEquipIds() {
		return equipIds;
	}
	public List<Equip> getEquips() {
		return equips;
	}
//	public Object getWeaponInfo() {
//		return weaponInfo;
//	}
//	public void setWeaponInfo(Object weaponInfo) {
//		this.weaponInfo = weaponInfo;
//	}
	/**
	 * 更新装备，同步更新equipIds
	 * @param equipArr
	 */
	public void setEquips(Object[] equipArr){
		clear();
		
		List<String> equipIdList = new ArrayList<String>();
		
		for( Object tmp : equipArr ){
			Equip equip = Equip.convert2Equip( (Object[])tmp );
			addEquip(equip);
			
			equipIdList.add(equip.getGoodsId());
		}
		equipIds = equipIdList.toArray();
	}
	
//	public Object getAllEquipData(){
//		/**
//		 * 		Object guid = tmp[0];
//				Object goodsId = tmp[1];
//				Object qhLevel = tmp[2];
//				Object gems = tmp[3];
//				Object slot = tmp[4];
//		 */
//		Object[] result = null;
//		if(equips != null && equips.size() > 0){
//			result = new Object[equips.size()];
//			
//			int index = 0;
//			for (Equip eq : equips) {
//				result[index++] = getGoodsDate(eq);
//			}
//			
//		}
//		
//		return result;
//	}
	
	
	/*private Object getGoodsDate(Equip entity){
		Map<String,Object> outputMap = new HashMap<String,Object>();
		
		outputMap.put("0", entity.getGoodsId());
		outputMap.put("1", entity.getId());
		outputMap.put("2", entity.getSlot());
		outputMap.put("3", 1);
		outputMap.put("4", 0);
		outputMap.put("10", entity.getQianghuaLevel());		
		outputMap.put("11", 1);		
		outputMap.put("12", entity.getOpenKongCount());		
		
		return outputMap;
	}*/
	
	/**
	 * 清空
	 */
	private void clear(){
		if( equips == null ){
			equips = new ArrayList<Equip>();
		}
		equips.clear();
		
		equipIds = null;
	}
	
	private void addEquip(Equip equip){
		if( equips == null ){
			equips = new ArrayList<Equip>();
		}
		equips.add(equip);
	}
}
