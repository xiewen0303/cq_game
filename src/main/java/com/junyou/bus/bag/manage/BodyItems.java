package com.junyou.bus.bag.manage;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.entity.RoleItem;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:49:44
 *@Description: 身上容器类 
 */
public class BodyItems extends AbstractContainer{
	/**
	 * 穿戴在身上的部位  
	 * @key:部位Id
	 * @value: 物品guid
	 */
	private Map<Integer,RoleItem> positions=new HashMap<Integer, RoleItem>();
	
	/**
	 * 容器
	 * @key: guid
	 * @value: item
	 */
	private Map<Long,RoleItem> equipSlots=new HashMap<Long, RoleItem>();
	
	public RoleItem removeItem(long guid){
		RoleItem roleItem =  equipSlots.remove(guid);
		return  positions.remove(roleItem.getSlot());
	}
	public RoleItem getItemByGuid(long guid){
		return equipSlots.get(guid);
	}
	
	public void addItem(RoleItem item){
		equipSlots.put(item.getId(), item);
		positions.put(item.getSlot(), item);
	} 
	
	/**
	 * 通过部位获得部位上的物品
	 * @param positionId
	 * @return
	 */
	public RoleItem getItemByPositionId(Integer positionId){
		return positions.get(positionId); 
	}
	
	@Override
	public Integer getType(){
		return ContainerType.BODYTITEM.getType();
	}
	@Override
	public List<RoleItem> getItems() {
		return new  ArrayList<RoleItem>(equipSlots.values());
	}
	@Override
	public void resetEmptySolt() {
		positions.clear();
		for (RoleItem roleItem : equipSlots.values()) {
			positions.put(roleItem.getSlot(), roleItem);
		}
	}
}