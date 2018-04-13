package com.junyou.bus.bag.manage;
 
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午2:40:26
 *@Description: 背包
 */
public class Bag{
	
	private long roleId;
	
	/**
	 * userRoleId= 容器(bagitems、bodyItems、storageItems)
	 */
	private Map<Integer,AbstractContainer> stores=new HashMap<Integer,AbstractContainer>();
	
	
	public void addBagStores(AbstractContainer t){
		stores.put(t.getType(), t);
	}
	
	public AbstractContainer getStore(Integer type){
		return stores.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getStore(Class <T> c,Integer type){
		return (T)stores.get(type);
	}
	 
	public Collection<AbstractContainer> getContainers(){
		return stores.values();
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	/**
	 * 重置所有容器的的空格位
	 */
	public void resetEmptySlot(){ 
		for (AbstractContainer abstractStore : stores.values()) {
			abstractStore.resetEmptySolt();
		}
	}
}
