package com.junyou.bus.bag;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.vo.RoleItemOperation;
 

/**
 * 用来做交易数据回滚
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-28下午5:31:19
 *@Description: 
 */
public class TradesRollback {
	private Map<Long,List<RoleItemOperation>> items=null;//操作记录对cacheManage操作记录
	
	private Map<Long,List<RoleItem>> deleteDatas=null;//对角色缓存删除记录
	
	private Map<Long,List<RoleItem>> addDatas=null;	  //对角色缓存添加记录
	
	private boolean isSuccee=true; //操作是否成功
	
	/**
	 * 对角色缓存删除记录
	 * @param roleItem
	 * @param userRoleId
	 */
	public void recordDeleteRoleCache(RoleItem roleItem,long userRoleId){
		if(deleteDatas == null){
			deleteDatas= new HashMap<>();
		}
		
		List<RoleItem> roleItems = deleteDatas.get(userRoleId);
		if(roleItems == null){
			roleItems= new ArrayList<>();
			deleteDatas.put(userRoleId, roleItems);
		}
		roleItems.add(roleItem); 
	}
	
	/**
	 * 对角色缓存添加记录
	 * @param roleItem
	 * @param userRoleId
	 */
	public void recordUpdateRoleCache(RoleItem roleItem,long userRoleId){
		if(addDatas == null){
			addDatas= new HashMap<>();
		}
		
		List<RoleItem> roleItems = addDatas.get(userRoleId);
		if(roleItems == null){
			roleItems= new ArrayList<>();
			addDatas.put(userRoleId, roleItems);
		}
		roleItems.add(roleItem); 
	}
	/**
	 * 是否成功
	 * @return
	 */ 
	public boolean isSuccee() {
		return isSuccee;
	}

	public void setSuccee(boolean isSuccee) {
		this.isSuccee = isSuccee;
	}

	public void addRoleItemVo(RoleItemOperation roleItemVo,long userRoleId){
		if(items==null){
			items=new HashMap<Long,List<RoleItemOperation>>();
		}
		List<RoleItemOperation> vos=null;
		if((vos=items.get(userRoleId))==null){
			vos=new ArrayList<RoleItemOperation>();
			items.put(userRoleId, vos);
		}
		vos.add(roleItemVo);
	}
	
	public void addRoleItemVos(List<RoleItemOperation> roleItemVos,long userRoleId){
		if(items==null){
			items=new HashMap<Long,List<RoleItemOperation>>();
		}
		List<RoleItemOperation> vos=null;
		if((vos=items.get(userRoleId))==null){
			vos=new ArrayList<RoleItemOperation>();
			items.put(userRoleId, vos);
		}
		vos.addAll(roleItemVos);
	}

	public Map<Long, List<RoleItemOperation>> getItems() {
		return items;
	}

	public Map<Long, List<RoleItem>> getDeleteDatas() {
		return deleteDatas;
	}

	public Map<Long, List<RoleItem>> getAddDatas() {
		return addDatas;
	}
  
}