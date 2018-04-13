package com.junyou.http.key;

public enum KeyEnum {
	NOMAL(1),
	/**
	 * 充值
	 */
	CONFIDENTIAL(2),
	/**
	 * 后台
	 */
	GMTOOLS_SAFE(3),SECRET(4);
	
	private int type;
	
	private KeyEnum(int type){
		this.type = type;
	}
	
	public int getType(){
		return type;
	}
}
