package com.junyou.bus.bag.manage;
 
import java.util.List;
import java.util.Map;

import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.entity.RoleItem;
 


/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午4:07:54
 *@Description: 容器抽象类
 */
public abstract class AbstractContainer {
	/**
	 * 获得类型
	 * @return
	 */
	
	public abstract Integer getType();
	/**
	 * 添加物品
	 * @param roleBagSlot
	 */
	public abstract  void addItem(RoleItem roleBagSlot);
	
	/**
	 * 获得所有物品
	 * @return
	 */
	public abstract List<RoleItem> getItems();
	
	/**
	 * 获得容器空格数
	 * @param containerType
	 * @return
	 */
	public int getContainerEmptyCount(ContainerType containerType){
		return 0;
	}
	
	/**
	 * 重置容器空格位， 不是所有容器都需要，如存储身上物品的容器
	 */
	public void resetEmptySolt() { 
	 
	}
	
	/**
	 * 设置容器描述信息，如大小等
	 * @param data
	 */
	public void setContainerDesc(Map<Integer,Integer> data){
		
	}
	
	/**
	 * 删除容器类数据
	 * @param guid
	 */
	public abstract RoleItem removeItem(long guid);
	
	/**
	 * 是否有
	 * @param slot
	 * @return
	 */
	public boolean hasEmptySlot(Integer slot){
		return true;
	}
	
	public abstract RoleItem getItemByGuid(long guid);
	
	public int getBeginSlot() { 
		return 0;
	}
	public int getEndSlot() { 
		return 0;
	}
	public void addSlot(int i) {
		 
	}
	public void setEndSlot(int endSlot) {
		
	}
}
