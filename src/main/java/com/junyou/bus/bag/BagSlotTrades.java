package com.junyou.bus.bag;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import com.junyou.bus.bag.vo.RoleItemOperation;
 

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-28下午5:31:19
 *@Description: 
 */
public class BagSlotTrades {
	private Map<Long,List<RoleItemOperation>> items=null;//通知前端需要对物品的操作
	private Object[] errorCode;
	
	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccee(){
		if(errorCode==null)return true;
		return false;
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
	
	public List<RoleItemOperation> getRoleItemVos(long userRoleId){
		return items.get(userRoleId);
	}

	public Object[] getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Object[] errorCode) {
		this.errorCode = errorCode;
	} 
}
