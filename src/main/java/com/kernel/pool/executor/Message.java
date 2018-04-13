package com.kernel.pool.executor;

import com.alibaba.fastjson.JSONArray;


public class Message {

	private Object[] msgSource;
	
	public Message(Object[] msg) {
		this.msgSource = msg;
	}
	
	public Short getCommand(){
		return (Short) msgSource[0];
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData(){
		return (T)msgSource[1];
	}

	public Long getRoleId(){
		return (Long) msgSource[7];
	}
	
	public String getRoleIdInfo(){
		Object value = msgSource[7];
		return value.toString();
	}

	public String getStageId(){
		Object stageid = msgSource[8];
		if(null != stageid){
			return (String) stageid;
		}
		return "";
	}
	
	public String getSessionid() {
		Object sid = msgSource[5];
		if(null !=sid){
			if(sid instanceof String){
				return (String) sid;
			}
		}
		return "0";
	}

	public Object[] getToken(){
		if(msgSource.length == 11){
			Object tokenObject = msgSource[10];
			if(null != tokenObject){
				return (Object[]) tokenObject;
			}
		}
		return null;
	}
	
	public Object[] getMsgSource(){
		return this.msgSource;
	}
	
	@Override
	public String toString() {
		return JSONArray.toJSONString(msgSource);
	}
}
