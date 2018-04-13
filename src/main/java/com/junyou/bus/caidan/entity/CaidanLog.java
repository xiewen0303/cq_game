package com.junyou.bus.caidan.entity;

import java.io.Serializable;

public class CaidanLog implements Serializable {

	private static final long serialVersionUID = 1L;
		
	private String roleName;
	
	private String itemId;
	
	private Integer num;
	
	private Object[] msg;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public CaidanLog(String roleName, String itemId, Integer num) {
		this.roleName = roleName;
		this.itemId = itemId;
		this.num = num;
	}

	public CaidanLog() {
	}
	
	public Object[] outMsg(){
		if(msg == null){
			msg = new Object[]{roleName,itemId,num};
		}
		return msg;
	}
	
	
	

	



}
