package com.junyou.stage.model.core.stage.aoi;


/**
 * aoi坐标值对象
 * @author DaoZheng Yuan
 * 2015年1月22日 下午2:29:55
 */
public class AoiPoint {

	private int x;
	
	private int y;
	
	public AoiPoint(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean equals(AoiPoint point) {
		return point.getX() == x && point.getY() == y;
	}

}
