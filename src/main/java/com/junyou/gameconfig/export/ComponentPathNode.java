package com.junyou.gameconfig.export;

import java.util.ArrayList;
import java.util.List;

import com.junyou.gameconfig.vo.PathNode;


public class ComponentPathNode extends PathNode {
	
	private PathNode node1;
	private PathNode node2;
	
	private List<PathNode> surrounds = new ArrayList<PathNode>();	

	public ComponentPathNode(PathNode node1, PathNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	@Override
	public int getX() {
		return node1.getX();
	}

	@Override
	public void setX(int x) {
		throw new UnsupportedOperationException("ComponentPathNode.setX()");
	}

	@Override
	public int getY() {
		return node1.getY();
	}

	@Override
	public void setY(int y) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("ComponentPathNode.setY()");
	}

	@Override
	public int getValue() {
//		return super.getValue();
		throw new UnsupportedOperationException("ComponentPathNode.getValue()");
	}

	@Override
	public void setValue(int value) {
		// TODO Auto-generated method stub
//		super.setValue(value);
		throw new UnsupportedOperationException("ComponentPathNode.setValue()");
	}

	@Override
	public List<PathNode> getSurrounds() {
		// TODO Auto-generated method stub
		return surrounds;
	}

	@Override
	public void addSurround(PathNode pathNodeConfig) {
		// TODO Auto-generated method stub
		surrounds.add(pathNodeConfig);
	}

	public int getKey() {
		return node1.getKey();
	}

	@Override
	public boolean isSafe() {
		return node1.isSafe() && node2.isSafe();
	}

	public boolean isValid() {
		return null != node1 && null != node2;
//		return null != node1;//避免浪费
	}

	public PathNode getNode1() {
		return node1;
	}

	public PathNode getNode2() {
		return node2;
	}
	
}
