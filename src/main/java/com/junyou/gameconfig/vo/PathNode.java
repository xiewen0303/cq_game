package com.junyou.gameconfig.vo;

import java.util.ArrayList;
import java.util.List;

public class PathNode {

	//2014-09-10 程方要求改成下面的 1
//	public static final int andValue = 31;
	
	public static final int andValue = 1;
	
	private final int key;

	private int x;
	private int y;
	private int value;
	private List<PathNode> surrounds = new ArrayList<PathNode>();	
	
	public PathNode() {
		key = -1;
	}
	
	public PathNode(int key, int x, int y, int value) {
		this.key = key;
		this.x = x;
		this.y = y;
		this.value = value;
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<PathNode> getSurrounds() {
		return surrounds;
	}

	public void addSurround(PathNode pathNodeConfig) {
		surrounds.add(pathNodeConfig);
	}

	public int getKey() {
		return key;
	}
	
//	public boolean isValid() {
//		return ((value & andValue) > 0);
//	}
	
	/**
	 * 是否安全点
	 */
	public boolean isSafe(){
		//ba[y * w + x] >> 5 & 1
		return (value >> 5 & 1) > 0; 
	}
	
}
