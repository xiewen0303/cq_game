/**
 * 
 */
package com.junyou.gameconfig.vo;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-28下午1:43:54
 */
public class PointConfig {

	private int x;
	
	private int y;
	
	/**
	 * 
	 */
	public PointConfig(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof PointConfig)){
			return false;
		}
		
		PointConfig compare = (PointConfig)obj;
		return (compare.getX() == getX()) && (compare.getY() == getY());
	}
}
