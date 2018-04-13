package com.junyou.gameconfig.export;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.gameconfig.vo.PathNode;
import com.junyou.stage.model.core.stage.Point;

/**
 * @description 地图路径信息
 * @author hehj
 * 2010-7-26 下午03:49:39
 */
public class PathInfoCopy{

	private PathInfoConfig sourcePathInfo;
	
	private ConcurrentMap<Integer, PathNodeCopy> copiedNodeMap = new ConcurrentHashMap<Integer, PathNodeCopy>();
//	private ConcurrentMap<String, PathNodeCopy> _2xCopiedNodeMap = new ConcurrentHashMap<String, PathNodeCopy>();
//	private ConcurrentMap<String, PathNodeCopy> _4xCopiedNodeMap = new ConcurrentHashMap<String, PathNodeCopy>();
	
	private NodeSearcher _1xNodeSearcher = null;
//	private NodeSearcher _2xNodeSearcher = null;
//	private NodeSearcher _4xNodeSearcher = null;
	
	public PathInfoCopy(PathInfoConfig pathInfo) {
		this.sourcePathInfo = pathInfo;
		
		_1xNodeSearcher = new _1xNodeSearcher();
//		_2xNodeSearcher = new _2xNodeSearcher();
//		_4xNodeSearcher = new _4xNodeSearcher();
		
	}
	
	/**
	 * 根据指定x,y获取对应节点信息
	 * @param x
	 * @param y
	 * @param pathNodeSize 占位尺寸{@link PathNodeSize}
	 */
	public PathNodeCopy getPathNode(int x,int y,PathNodeSize pathNodeSize){
		return getPathNode(new Point(x, y), pathNodeSize);
	}
	
	/**
	 * 根据指定x,y获取对应节点信息
	 */
	public PathNodeCopy getPathNode(Point point,PathNodeSize takeupSize){
		
		Point pathPoint = sourcePathInfo.getPathNodePoint(point, takeupSize);
		
		switch (takeupSize) {
		case _1X:
			return _1xNodeSearcher.getPathNode(getKey(pathPoint.getX(), pathPoint.getY()), takeupSize);
//		case _2X:
//			return _2xNodeSearcher.getPathNode(getKey(pathPoint.getX(), pathPoint.getY()), takeupSize);
//		case _4X:
//			return _4xNodeSearcher.getPathNode(getKey(pathPoint.getX(), pathPoint.getY()), takeupSize);
		default:
			break;
		}
		
		return null;
	}
	
	private class _1xNodeSearcher extends NodeSearcher{

		@Override
		public PathNodeCopy getPathNode(Integer key, PathNodeSize takeupSize) {
			
			PathNodeCopy node = copiedNodeMap.get(key);
			if(null == node){
				PathNode sourceNode = sourcePathInfo.getPathNode(key,takeupSize);
				if(sourceNode != null){
					node = new PathNodeCopy(sourceNode);
					copiedNodeMap.put(node.getKey(), node);
				}
			}
			
			return node;
			
		}
		
	}
	
	private class _2xNodeSearcher extends NodeSearcher{

		@Override
		public PathNodeCopy getPathNode(Integer key, PathNodeSize takeupSize) {
			
			
//			PathNodeCopy node = _2xCopiedNodeMap.get(key);
//			if(null == node){
//				
//				ComponentPathNode pathNode = (ComponentPathNode)sourcePathInfo.getPathNode(key, takeupSize);
//				if(null == pathNode) return null;
//				
//				PathNode pathNodeChilid1 = pathNode.getNode1();
//				PathNode pathNodeChilid2 = pathNode.getNode2();
//				
//				node = new ComponentPathNodeCopy(
//						_1xNodeSearcher.getPathNode(pathNodeChilid1.getKey(), PathNodeSize._1X),
//						null != pathNodeChilid2 ?_1xNodeSearcher.getPathNode(pathNodeChilid2.getKey(), PathNodeSize._1X) : null,
//						pathNode.getSurrounds()
//						);
//				
//				_2xCopiedNodeMap.put(node.getKey(), node);
//			}
//			
//			return node;
			
			return null;
			
		}
		
	}
	
	private class _4xNodeSearcher extends NodeSearcher{

		@Override
		public PathNodeCopy getPathNode(Integer key, PathNodeSize takeupSize) {
			
			
//			PathNodeCopy node = _4xCopiedNodeMap.get(key);
//			if(null == node){
//				
//				ComponentPathNode pathNode = (ComponentPathNode)sourcePathInfo.getPathNode(key, takeupSize);
//				if(null == pathNode) return null;
//				PathNode pathNodeChilid1 = pathNode.getNode1();
//				PathNode pathNodeChilid2 = pathNode.getNode2();
//				
//				node = new ComponentPathNodeCopy(
//						_2xNodeSearcher.getPathNode(pathNodeChilid1.getKey(), PathNodeSize._2X),
//						null != pathNodeChilid2 ? _2xNodeSearcher.getPathNode(pathNodeChilid2.getKey(), PathNodeSize._2X) : null,
//						pathNode.getSurrounds()
//						);
//				
//				_4xCopiedNodeMap.put(node.getKey(), node);
//			}
//			
//			return node;
			
			return null;
			
		}
		
	}
	
	
	private abstract class NodeSearcher{
		public abstract PathNodeCopy getPathNode(Integer key,PathNodeSize takeupSize);
	}
	
//	private String getKey(int x, int y){
//		return new StringBuffer().append(x).append("-").append(y).toString();
//	}
	
	/**
	 * int类型的key值  [32768 * 32768]
	 * @param x
	 * @param y
	 * @return
	 */
	private int getKey(int x,int y){
		return (x << 15) + y;
	}

	public PathFinderType getPathFinderType() {
		return sourcePathInfo.getPathFinderType();
	}

}
