package com.junyou.stage.model.core.stage;

public enum PointTakeupType {
	/**
	 *  生物 
	 */
	BEING(1), 
	/**
	 *  物品
	 */
	GOODS(2);
	
	private int val;
	
	private PointTakeupType(int val){
		this.val = val;
	}
	
	public int getVal(){
		return val;
	}
}
