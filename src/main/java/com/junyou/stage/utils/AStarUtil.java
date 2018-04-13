package com.junyou.stage.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.gameconfig.export.PathInfoCopy;
import com.junyou.gameconfig.export.PathNodeCopy;
import com.junyou.gameconfig.export.PathNodeSize;
import com.junyou.gameconfig.vo.PathNode;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.utils.lottery.Lottery;

/**
 * @description A*算法工具类
 * @author hehj
 * 2010-7-26 下午05:10:45
 */
public class AStarUtil {

	private static Comparator<ANode> comparator = new Comparator<ANode>() {

		@Override
		public int compare(ANode o1, ANode o2) {
			if(o1.getF() < o2.getF()) return -1;
			if(o1.getF() > o2.getF()) return 1;
			return 0;
		}
		
	};
	
	/**
	 * 在指定的步长内执行搜索
	 * @param steps 期望的步长
	 * @param start 开始坐标
	 * @param target 目标坐标
	 * @param pathInfo 路径信息
	 * @param takeupSize 占位尺寸
	 * @return
	 */
	private static PathNodeCopy[] findPath(int steps,Point start,Point target,PathNodeSize takeupSize,PathInfoCopy pathInfo){
		
		AbsPathFinder pathFinder = PathFinderFactory.getFinder(pathInfo.getPathFinderType());
		PathNodeCopy[] pathNodes = pathFinder.find(steps, start, target,takeupSize, pathInfo);
		
		return pathNodes;
	}
	
	/**
	 * 在缺省步长内搜索
	 * @param start 开始坐标
	 * @param target 目标坐标
	 * @param pathInfo 路径信息
	 * @param takeupSize 占位尺寸
	 * @return
	 */
	public static PathNodeCopy[] findPath(Point start, Point target,PathInfoCopy pathInfo, PathNodeSize takeupSize) {
		return findPath(100,start, target, takeupSize, pathInfo);
	}
	
	
	private static PathNodeCopy[] outputResult(ANode aNode){
		
		if(null==aNode) return null;
		
		PathNodeCopy[] points = new PathNodeCopy[aNode.getStep()];
		ANode preANode = aNode;
		int steps = aNode.getStep()-1;
		for(int i=0;i<=steps;i++){
			
			points[steps-i] = preANode.getPathNode();
//			points[i] = new Point(preANode.getPathNode().getX(),preANode.getPathNode().getY());
			
			preANode = preANode.getPreANode();
			if(null==preANode) break;
			
		}
		
		return points;
	}
	
	
	
	private static ANode getMinANode(List<ANode> openList){
		
		ANode node = Collections.min(openList, comparator);
		openList.remove(node);
		
		return node;
	}
	
	private static class ANode{
		
		private PathNodeCopy node;
		private int f;
		private int g;
		private int h;
		private int step;
		private ANode preANode;
		
		public ANode(PathNodeCopy node,int g,int h,ANode preANode) {
			this.node = node;
			this.g = g;
			this.h = h;
			this.f = g + h;
			this.preANode = preANode;
			this.step = ( null==preANode ) ? 1 : (preANode.getStep() + 1);
		}
		
		public PathNodeCopy getPathNode(){
			return this.node;
		}
		
		public ANode getPreANode(){
			return this.preANode;
		}
		
		public int getKey(){
			return node.getKey();
		}

		public int getF(){
			return this.f;
		}
		
		public int getG(){
			return this.g;
		}
		
		public int getStep(){
			return this.step;
		}
		
		public boolean noMove(){
			return !(node).isNotTakeup(PointTakeupType.BEING.getVal());
		}
	}
	
	private static class PathFinderFactory{
		
		private static Map<PathFinderType,AbsPathFinder> pathFinderMap = new HashMap<PathFinderType, AbsPathFinder>();
		
		public static AbsPathFinder getFinder(PathFinderType type){
			return pathFinderMap.get(type);
		}
		
		static{
			
			AbsPathFinder aFinder = new AbsPathFinder() {
				
				@Override
				public ANode calSurroundANode(PathNodeCopy realNode,ANode curANode,PathNodeCopy target) {
					
					PathNodeCopy pathNode = curANode.getPathNode();
					
					int temp1 = Math.abs(realNode.getX()-pathNode.getX());
					temp1 = ( temp1==2 ) ? 17 : ( ( temp1==1 ) ? 10 : 14 );
					int temp2 = Math.abs(realNode.getX() - target.getX());
					int temp3 = Math.abs(realNode.getY() - target.getY());
					int dx = temp2 * 9;
					int dy = (temp3-( ( ( realNode.getX()&1 ) > 0) ? 0 : 1) )*10 + ( target.getY()&1 ) * 5;
					
//					int g = temp1 + curANode.getG() + curANode.getValue();
					int g = temp1 + curANode.getG();
					int h = dx * dx + dy * dy;
					
					return new ANode(realNode, g, h, curANode);
				}
			};
			
			pathFinderMap.put(PathFinderType.A, aFinder);
			
			AbsPathFinder aExtendPathFinder = new AbsPathFinder(){

				@Override
				public ANode calSurroundANode(PathNodeCopy realNode,
						ANode curANode, PathNodeCopy target) {
					
							int x = curANode.getPathNode().getX();
							int y = curANode.getPathNode().getY();
							int i = realNode.getX();
							int j = realNode.getY();

							int temp1 = i - x + j - y;
							temp1 = temp1 < 0 ? -temp1 : temp1;
							int temp2 = target.getX() - i;
							temp2 = temp2 < 0 ? -temp2 : temp2;
							int temp3 = target.getY() - j;
							temp3 = temp3 < 0 ? -temp3 : temp3;
							
							int g = curANode.getG();
							g = (i == x && j == y ?  0 : ( temp1 == 1 ?  10+g : 14+g));
							int h = (temp2 + temp3) * 10;
					
					return new ANode(realNode, g, h, curANode);
				}
				
			};
			
			pathFinderMap.put(PathFinderType.AExtend, aExtendPathFinder);
			
			
		}
		
		
	}
	
	
	private static abstract class AbsPathFinder{
		
		public PathNodeCopy[] find(int steps,Point start,Point _target,PathNodeSize pointSize,PathInfoCopy pathInfo){
			
			PathNodeCopy targetPathNode = pathInfo.getPathNode(_target, pointSize);
			if(null == targetPathNode) return null;
			
			//目标点被占用,就换周边的点
			if(!targetPathNode.isNotTakeup(PointTakeupType.BEING.getVal())){
				targetPathNode = getSurroundValidPathNode(pathInfo, targetPathNode, PointTakeupType.BEING);
			}
			if(null == targetPathNode) return null;
			
			
			List<ANode> openList = new ArrayList<ANode>();
			Map<Integer, Object> done = new HashMap<Integer, Object>();
			
			ANode result = null;
			PathNodeCopy tmp = pathInfo.getPathNode(start,pointSize);
			if(null == tmp) return null;
			openList.add(new ANode(tmp,0,0,null));
			int step = 0;
			while(openList.size()>0){
				
				if( ++step > steps ) 
					return null;
				
				ANode curANode = getMinANode(openList);
				
				// 如果不可走
//				if(curANode.noMove()) continue;
				// 如果已搜过
				if(done.containsKey(curANode.getKey())) continue;
				done.put(curANode.getKey(), curANode);

				// 结束条件
				if(curANode.getPathNode().getX() == targetPathNode.getX() && curANode.getPathNode().getY() == targetPathNode.getY()){
					result = curANode;
					break;
				}
				
				PathNodeCopy pathNode = curANode.getPathNode();
				for(PathNode node : pathNode.getSurrounds()){
					
					PathNodeCopy realNode = pathInfo.getPathNode(node.getX(),node.getY(),pointSize);
					if(realNode.getX() == targetPathNode.getX() && realNode.getY() == targetPathNode.getY()){
						//目标点永远是有效的
					}else{
//						if((realNode.getValue() & PathNode.andValue) < 1) continue;
						if(!(realNode).isNotTakeup(PointTakeupType.BEING.getVal())) continue;
					}
					
					ANode surroundANode = calSurroundANode(realNode,curANode,targetPathNode);
					
					// add into open list
					openList.add(surroundANode);
					
				}
				
			}
			return outputResult(result);
		}
		
		/**
		 * 计算周边点的ANode信息
		 * @param realNode 周边点
		 * @param pathNode 当前点
		 */
		public abstract ANode calSurroundANode(PathNodeCopy realNode,ANode curANode,PathNodeCopy target);
	}
	
	
	private static PathNodeCopy getSurroundValidPathNode(PathInfoCopy pathInfo,PathNodeCopy pathNode,PointTakeupType takeupType) {
		
		List<PathNode> surrounds = pathNode.getSurrounds();
		try{
		for(PathNode surround : surrounds){
			PathNodeCopy realNode = pathInfo.getPathNode(surround.getX(),surround.getY(),PathNodeSize._1X);
			
			if(realNode.isNotTakeup(takeupType.getVal())){
				return realNode;
			}
		}
		}catch (StackOverflowError e) {
			return null;
		}
		
		if(surrounds.size() == 0){
			return null;
		}
		
		PathNode _nextNode = surrounds.get(Lottery.roll(surrounds.size()));
		PathNodeCopy _nextNodeCopy =  pathInfo.getPathNode(_nextNode.getX(), _nextNode.getY(), PathNodeSize._1X);
		return getSurroundValidPathNode(pathInfo,_nextNodeCopy,takeupType);
	}
	
	
	public static void main(String[] args) {
//		PathInfo pathInfo = ShMapConfigBuilder.initMapPathInfo("d:\\");
//		PathNode node = pathInfo.getPathNode(new Point(116,72));
//		System.out.println(node.getValue());
//		PathNode[] points = findPath(50000,new Point(110,103), new Point(106,103), pathInfo);
//		if(null!=points){
//			for(PathNode p : points){
//				System.out.println(p.getX()+","+p.getY());
//			}
//		}
	}

	

	
	
}
