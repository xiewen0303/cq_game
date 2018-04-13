package com.junyou.gameconfig.export;

import java.util.List;

import com.junyou.gameconfig.vo.PathNode;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.PointTakeupType;



/**
 * @description 地图路径节点信息
 * @author hehj
 * 2010-7-26 下午03:49:19
 */
public class PathNodeCopy{

	private PathNode sourcePathNode;
	
	private boolean beingTakeup;  //是否被人或怪占领
	private boolean goodsTakeup;  //是否被物品占领

	public PathNodeCopy(PathNode pathNode) {
		this.sourcePathNode = pathNode;
	}

	public PathNode getPathNode(){
		return sourcePathNode;
	}
	
	public int getX() {
		return sourcePathNode.getX();
	}

	public int getY() {
		return sourcePathNode.getY();
	}

	public int getKey(){
		return sourcePathNode.getKey();
	}
	
	public List<PathNode> getSurrounds(){
		return sourcePathNode.getSurrounds();
	}

//	public boolean isValid() {
//		return ((value & andValue) > 0);
//	}

	/**
	 * 设置被占领类型
	 * @param takeupOrNot 占领或不占领
	 * @param takeupType  1:生物 2：物品
	 */
	public void setTakeupType(boolean takeupOrNot, PointTakeupType takeupType) {
		if(takeupType == null){
			ChuanQiLog.error("添加的takeupType类型为null");
			return;
		}
		
		if(PointTakeupType.BEING.getVal() == takeupType.getVal()){
			this.beingTakeup = takeupOrNot;
		}else if(PointTakeupType.GOODS.getVal() == takeupType.getVal()){
			this.goodsTakeup = takeupOrNot;
		}
	}
	
	public boolean isNotTakeup(Integer _takeupType){
		
		if(_takeupType.equals(1)){
			return !beingTakeup;
		}
		
		if(_takeupType.equals(2)){
			return !beingTakeup && !goodsTakeup;
		}
		
		return !beingTakeup && !goodsTakeup;
	}

	/**
	 * 是否安全点
	 */
	public boolean isSafe(){
		return sourcePathNode.isSafe(); 
	}
	
	
}