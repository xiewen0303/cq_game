package com.junyou.stage.model.core.stage;


public class Point {
	private int x;
	private int y;
	
	private int hisX;
	private int hisY;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	public int getHisX() {
		return hisX;
	}
	public int getHisY() {
		return hisY;
	}
	
	/**
	 * 是否真实移动
	 * @return true:是
	 */
	public boolean isReallyMove(){
		if(hisX == 0 || hisY == 0){
			return true;
		}else if(x == hisX && y == hisY){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 设置历史坐标
	 * @param x
	 * @param y
	 */
	public void setHisPoint(int x,int y){
		this.hisX = x;
		this.hisY = y;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		
		if(!(obj instanceof Point)){
			return false;
		}
		
		Point compare = (Point)obj;
		return (compare.getX() == getX()) && (compare.getY() == getY());
	}
	
	public String toString(){
		return "cur:"+this.x+","+this.y +"	his:"+this.hisX+","+this.hisY;
	}
	
	
}
