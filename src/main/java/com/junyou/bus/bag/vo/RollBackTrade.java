package com.junyou.bus.bag.vo;
 
import java.util.ArrayList;
import java.util.List;
import com.junyou.bus.bag.entity.RoleItem;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-12-10下午6:09:20
 *@Description: 用来做交易数据回滚的
 */
public class RollBackTrade {
	
	List<RoleItem> deleteItems=null;
	
	List<RoleItem> insertRoleItems=null;
 
	
	public void addDeleteItem(RoleItem roleItem){
		if(deleteItems==null){
			deleteItems=new ArrayList<RoleItem>();
		}
		this.deleteItems.add(roleItem);
	}
	
	public void addInsertItem(RoleItem roleItem){
		if(insertRoleItems==null){
			insertRoleItems=new ArrayList<RoleItem>();
		}
		this.insertRoleItems.add(roleItem);
	}

	public List<RoleItem> getDeleteItems() {
		return deleteItems;
	}

	public List<RoleItem> getInsertRoleItems() {
		return insertRoleItems;
	} 
}
