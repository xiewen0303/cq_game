package com.junyou.bus.bag;

 
import java.util.ArrayList;
import java.util.List; 
import com.junyou.bus.bag.vo.RoleItemOperation;
 

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-28上午9:57:28
 *@Description: 
 */
public class BagSlots {
	private List<RoleItemOperation> items=null;//通知前端需要对物品的操作
	private Object[] errorCode;
	
	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccee(){
		if(errorCode==null)return true;
		return false;
	}
	
	public void addRoleItemVo(RoleItemOperation roleItemVo){
		if(items==null){
			items=new ArrayList<RoleItemOperation>();
		}
		items.add(roleItemVo);
	}
	
	public void addRoleItemVos(List<RoleItemOperation> roleItemVos){
		if(items==null){
			items=new ArrayList<RoleItemOperation>();
		}
		items.addAll(roleItemVos);
	}
	
	public List<RoleItemOperation> getRoleItemVos(){
		return items;
	}

	public Object[] getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Object[] errorCode) {
		this.errorCode = errorCode;
	}
}
