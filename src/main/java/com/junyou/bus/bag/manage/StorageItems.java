package com.junyou.bus.bag.manage;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.junyou.bus.bag.BagContants;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.entity.RoleItem;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:49:12
 *@Description: 仓库容器类
 */
public class StorageItems extends AbstractContainer {
	/**
	 * 开始格位
	 */
	private int beginSlot;
	
	/**
	 * 结束格位
	 */
	private int endSlot;
	
	
	/**
	 * 容器
	 */
	Map<Long,RoleItem> storageSlots=new HashMap<Long, RoleItem>();
	
	/**
	 * 空格位置,没放物品的
	 */
	private TreeSet<Integer> emptySlots=new TreeSet<Integer>();
	
	public RoleItem removeItem(long guid){
		RoleItem roleItem = storageSlots.remove(guid);
		emptySlots.add(roleItem.getSlot());
		return roleItem;
	}
	public RoleItem getItemByGuid(long guid){
		return storageSlots.get(guid);
	}
	
	public void addItem(RoleItem item){
		this.storageSlots.put(item.getId(), item);
	}
	@Override
	public Integer getType() {
		return ContainerType.STORAGEITEM.getType();
	}
	@Override
	public List<RoleItem> getItems() {
		return new  ArrayList<RoleItem>(storageSlots.values());
	}
 

	/**
	 * 得到下一个新的格位
	 * @return
	 */
	public Integer getNextNewSlot(){
		return emptySlots.first();
	}
	
	@Override
	public void resetEmptySolt() {
		emptySlots.clear();
		for(int i=beginSlot;i<=endSlot;i++){
			emptySlots.add(i);
		}
		
		for (RoleItem item: storageSlots.values()) {
			Integer solt=item.getSlot();
			boolean flag=emptySlots.contains(solt);
			 if(flag){
				 emptySlots.remove(solt);
			 }
		}
	}
	
	@Override
	public void addSlot(int slot) {
		 this.emptySlots.add(slot);
	}
	
//	/**
//	 * 指定删除对应的格子位
//	 * @param targetSlot
//	 */
//	@Autowired
//	public void removeSlot(Integer targetSlot) {
//		this.emptySolts.remove(targetSlot);
//	}
	
 
	@Override
	public void setContainerDesc(Map<Integer, Integer> data) {
		beginSlot= data.get(BagContants.Storage_B_SLOT);
		endSlot=data.get(BagContants.Stroage_E_SLOT);
	}
	
	public int getEmptyCount() {
		return emptySlots.size();
	}
	public int getEndSlot() {
		return endSlot;
	}
	public int getBeginSlot() {
		return beginSlot;
	}
	public void setBeginSlot(int beginSlot) {
		this.beginSlot = beginSlot;
	}
	@Override
	public void setEndSlot(int endSlot) {
		this.endSlot = endSlot;
	} 
}
