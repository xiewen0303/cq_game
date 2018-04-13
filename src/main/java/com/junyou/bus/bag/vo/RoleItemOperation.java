package com.junyou.bus.bag.vo;

import com.junyou.bus.bag.entity.RoleItem;

/**
 * 操作动作
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-28上午10:03:26
 *@Description: 
 */
public class RoleItemOperation {
	private int oldCount;//之前的数量,一般用来日志统计
	private RoleItem roleItem;	//物品信息
	private byte operationType; //操作类型
	private byte containerType;	//容器类型 
	
	public RoleItemOperation(int oldCount, RoleItem roleItem, byte operationType,byte containerType) {
		super();
		this.oldCount = oldCount;
		this.roleItem = roleItem.copy();
		this.operationType = operationType;
		this.containerType = containerType;
	}
	
	public RoleItem getRoleItem(){
		return roleItem;
	}
	
	/**
	 * 获得物品配置Id
	 * @return
	 */
	public String getGoodsId(){
		return roleItem.getGoodsId();
	}
	
	

	/**
	 * 获得物品唯一Id
	 * @return
	 */
	public long getGuid(){
		return roleItem.getId();
	}
	
	/**
	 * 获得物品数量
	 * @return
	 */
	public int getCount(){
		return roleItem.getCount();
	}
	
	public byte getContainerType() {
		return containerType;
	}
	 
	public byte getOperationType() {
		return operationType;
	} 
	public int getOldCount() {
		return oldCount;
	}

	/**
	 * 获得格位号
	 * @return
	 */
	public int getSlot() {
		return roleItem.getSlot();
	}

 
	public void setOperationType(byte type) {
		this.operationType=type;
	}
	
	/**
	 * 获取宠物id
	 * 
	 * @return
	 */
	public int getChongwuId(){
	    return null == roleItem.getChongwuId() ? 0 : roleItem.getChongwuId();
	}
	
	/**
	 * 获取神器id
	 * 
	 * @return
	 */
	public int getShenQiId(){
	    return null == roleItem.getShenqiId() ? 0 : roleItem.getShenqiId();
	}
}
