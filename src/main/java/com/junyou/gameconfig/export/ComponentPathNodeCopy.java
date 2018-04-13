package com.junyou.gameconfig.export;

import java.util.List;

import com.junyou.gameconfig.vo.PathNode;
import com.junyou.stage.model.core.stage.PointTakeupType;

public class ComponentPathNodeCopy extends PathNodeCopy {

	public ComponentPathNodeCopy(PathNodeCopy pathNode1,PathNodeCopy pathNode2, List<PathNode> surroundNodes) {
		super(null);
		
		this.pathNode1 = pathNode1;
		this.pathNode2 = pathNode2;
		this.surroundNodes = surroundNodes;
	}

	private PathNodeCopy pathNode1;
	private PathNodeCopy pathNode2;
	private List<PathNode> surroundNodes;
	
	@Override
	public void setTakeupType(boolean takeupOrNot, PointTakeupType takeupType) {
		pathNode1.setTakeupType(takeupOrNot, takeupType);
		if(null != pathNode2)pathNode2.setTakeupType(takeupOrNot, takeupType);
	}


	@Override
	public boolean isNotTakeup(Integer _takeupType) {
		return pathNode1.isNotTakeup(_takeupType) && (null == pathNode2 || pathNode2.isNotTakeup(_takeupType));
	}


	@Override
	public int getX() {
		return pathNode1.getX();
	}


	@Override
	public int getY() {
		return pathNode1.getY();
	}


	@Override
	public int getKey() {
		return pathNode1.getKey();
	}


	@Override
	public List<PathNode> getSurrounds() {
		return surroundNodes;
	}


	@Override
	public boolean isSafe() {
		return pathNode1.isSafe();
	}
	
	
	
}
