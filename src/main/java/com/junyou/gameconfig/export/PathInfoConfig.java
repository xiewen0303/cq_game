package com.junyou.gameconfig.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.constants.PathFinderType;
import com.junyou.gameconfig.utils.OffConfigUtils;
import com.junyou.gameconfig.vo.Off;
import com.junyou.gameconfig.vo.PathNode;
import com.junyou.gameconfig.vo.PointConfig;
import com.junyou.stage.model.core.stage.Point;

/**
 * 
 * @description 地图路径信息
 *
 * @author LiuJuan
 * @created 2011-12-12 上午10:19:47
 */
public class PathInfoConfig {
	
	private int width;
	private int xsWidth;
	private int height;
	private int xsHeight;
	private Map<Integer, PathNode> nodes = new HashMap<Integer, PathNode>();
	
//	private Map<String, PathNode> _2xNodes = new HashMap<String, PathNode>();
//	private Map<String, PathNode> _4xNodes = new HashMap<String, PathNode>();
	
	private PointConfig defaultCanMovedPoint;
	
	private PathFinderType pathFinderType;
	

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getXsWidth() {
		return xsWidth;
	}
	public int getXsHeight() {
		return xsHeight;
	}
	public void initXsWidthHeight(){
		this.xsWidth = OffConfigUtils.convert2XsWidth(this.width,pathFinderType);
		this.xsHeight = OffConfigUtils.convert2XsHeight(this.height, this.width,pathFinderType);
	}
	
	public void addPathNode(int x,int y,int value){
		
		if(null == defaultCanMovedPoint && (value & PathNode.andValue) > 0 && ( x>20 && y>20)){
			defaultCanMovedPoint = new PointConfig(x, y);
		}
		
		if((value & PathNode.andValue) > 0){
			PathNode pathNode = new PathNode(getKey(x, y), x, y,value);
			nodes.put(pathNode.getKey(),pathNode);
		}
		
	}
	
	public void preprocess(){
		
		List<Off> offs = null;
		for(PathNode node : nodes.values()){
			
			switch (getPathFinderType()) {
			case A:
				offs = ( (node.getX() & 1) > 0 ) ? OffConfigUtils.Off1s : OffConfigUtils.Off0s;
				break;
			case AExtend:
				offs = OffConfigUtils.AExtendOffs;
			default:
				break;
			}
			
			for(Off off : offs){
				PathNode surround = nodes.get(getKey(node.getX()+off.getX(), node.getY()+off.getY()));
				if(null!=surround) node.addSurround(surround);
			}
		}
		
		//处理两倍节点
//		for(int y = 1 ; y < height ; y++ ){
//			for(int x = 1 ; x < width ; x += 2){
//				ComponentPathNode pathNode = new ComponentPathNode(nodes.get(getKey(x, y)),nodes.get(getKey(x+1, y)));
//				
//				if(pathNode.isValid()){
//					_2xNodes.put(getKey(x, y), pathNode);
//				}
//				
//			}
//		}
//		for(PathNode node : _2xNodes.values()){
//			
//			for(Off off : offs){
//				PathNode surround = _2xNodes.get(getKey(node.getX() + off.getX() * 2, node.getY()+off.getY()));
//				if(null!=surround) node.addSurround(surround);
//			}
//		}
		
		//处理四倍节点
//		for(int y = 1 ; y < height ; y += 2 ){
//			for(int x = 1 ; x < width ; x += 2){
//				ComponentPathNode pathNode = new ComponentPathNode(_2xNodes.get(getKey(x, y)),_2xNodes.get(getKey(x, y+1)));
//				
//				if(pathNode.isValid()){
//					_4xNodes.put(getKey(x, y), pathNode);
//				}
//				
//			}
//		}
//		for(PathNode node : _4xNodes.values()){
//			
//			for(Off off : offs){
//				PathNode surround = _4xNodes.get(getKey(node.getX() + off.getX() * 2, node.getY()+off.getY() * 2));
//				if(null!=surround) node.addSurround(surround);
//			}
//		}
		
	}
	
	/**
	 * 获取当前坐标的路径节点信息
	 * @param point
	 * @return
	 */
	public PathNode getPathNode(int x, int y){
		return getPathNode(x, y, PathNodeSize._1X);
	}

	/**
	 * 获取当前坐标的路径节点信息
	 * @param key
	 * @return
	 */
	public PathNode getPathNode(Integer key){
		return getPathNode(key,PathNodeSize._1X);
	}
	
	public PointConfig getDefaultMovedPoint(){
		return defaultCanMovedPoint;
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
	
	/**
	 * 获取路径寻找类型
	 */
	public PathFinderType getPathFinderType() {
		return pathFinderType;
	}
	
	public void setPathFinderType(PathFinderType pathFinderType){
		this.pathFinderType = pathFinderType;
	}
	public Map<Integer, PathNode> getNodes() {
		return nodes;
	}
	
	/**
	 * 
	 */
	public Point getPathNodePoint(Point _start, PathNodeSize takeupSize) {
		
		switch (takeupSize) {
		case _1X:
			return _start;
		case _2X:
			
			//x对应1,3,5,7......不存在偶数节点坐标,因为坐标尺寸为2
			int x = (_start.getX() % 2) == 0 ? _start.getX() - 1 : _start.getX();
			int y = _start.getY();
			
			return new Point(x, y);
		case _4X:
			//x,y都对应1,3,5,7......不存在偶数节点坐标,因为坐标尺寸为2
			x = (_start.getX() % 2) == 0 ? _start.getX() - 1 : _start.getX();
			y = (_start.getY() % 2) == 0 ? _start.getY() - 1 : _start.getY();
			
			return new Point(x, y);
		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	public PathNode getPathNode(int x, int y, PathNodeSize takeupSize) {
		return getPathNode(getKey(x, y), takeupSize);
	}
	
	/**
	 * 
	 */
	public PathNode getPathNode(Integer key, PathNodeSize takeupSize) {
		
		switch (takeupSize) {
		case _1X:
			return nodes.get(key);
//		case _2X:
//			return _2xNodes.get(key);
//		case _4X:
//			return _4xNodes.get(key);
		default:
			break;
		}
		
		return null;
		
	}
	
}
