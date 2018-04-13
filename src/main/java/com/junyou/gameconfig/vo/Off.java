package com.junyou.gameconfig.vo;

/**
 * @description 偏移量
 * @author hehj
 * 2010-7-26 下午04:27:40
 */
public class Off{
	
	/**
	 * x偏移量
	 */
	private int x;
	/**
	 * y偏移量
	 */
	private int y;
	
	public Off(int x,int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
