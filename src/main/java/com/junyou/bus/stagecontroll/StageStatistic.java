package com.junyou.bus.stagecontroll;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.map.configure.export.MapLineConfig;

/**
 * @description 场景统计
 * @author ShiJie Chi
 * @date 2012-5-22 上午10:49:09 
 */
public class StageStatistic {
	
	private static final Integer ROLE_COUNT_LEVEL_FIRST = 1;
	
	private static final Integer ROLE_COUNT_LEVEL_SECOND = 2;
	
	private static final Integer ROLE_COUNT_LEVEL_THIRD = 3;

	private static final Integer EMPTY_LINE_NO = 1;
	
	private Integer mapId;
	
	private Map<Integer,Integer> lines = new HashMap<Integer, Integer>();
	

	private MapLineConfig config;
	
	/**
	 * @param stageId
	 */
	public StageStatistic(MapLineConfig mapLineConfig) {
		this.mapId = mapLineConfig.getMapId();
		this.config = mapLineConfig;
		
		
		Integer line = getDefaultLine();
		if(line >= 1 ){
			for(int i = 1 ; i <= line ; i++){
				lines.put(i, 0);
			}
		}
	}

	/**
	 * 检查所有线路是否已满,如果满了，则新开一条线
	 * @param lineNo
	 * @return
	 */
	public void checkLinesFullAndIncr(){
		for( Integer count : lines.values() ){
			if( count < getMaxLoad() ){
				return;
			}
		}
		lines.put(lines.size()+1, 0);
	}
	
	/**
	 * 获取最小负载线路
	 */
	public StageLine getMinLoadLine() {
		
		//循环遍历各线承载量
		Integer finalLineNo = findMinLine(getMaxLoad());
		
		boolean isNew = false;
		if(!lines.containsKey(finalLineNo)){
			isNew = true;
			lines.put(finalLineNo, 0);
		}
		
		return new StageLine(mapId,finalLineNo,isNew);
	}

	/**
	 * 寻找最少人数线路
	 */
	private Integer findMinLine(Integer maxLoad) {
		
		if(isEmptyLine()){
			return EMPTY_LINE_NO;
		}
		int lineNo = 0;
		
		for(int i = 1 ; i <= lines.size() ; i++){
			if(lines.get(i) < maxLoad){
				lineNo = i;
				maxLoad = lines.get(i);
			}
		}
		if(lineNo > 0){
			return lineNo;
		}else if(lines.size() < getMaxLine()){
			return lines.size() + 1;
		}
		
		if(maxLoad > GameConstants.STAGE_MAX_LOAD){//防止死循环，增加深度，超出深度默认返回1线
			return 1;
		}
		
		return findMinLine(maxLoad + getMaxLoadIncrease());
	}
	
	/**
	 * 寻找最佳线路
	 */
	private Integer findLine(Integer maxLoad) {
		
		if(isEmptyLine()){
			return EMPTY_LINE_NO;
		}
		
		for(int i = 1 ; i <= lines.size() ; i++){
			if(lines.get(i) < maxLoad){
				return i;
			}
		}
		
		if(lines.size() < getMaxLine()){
			return lines.size() + 1;
		}
		
		return findLine(maxLoad + getMaxLoadIncrease());
	}

	/**
	 * 是否此场景无分线概念
	 */
	private boolean isEmptyLine() {
		return getMaxLine() <= 1;
	}

	public boolean containLine(Integer lineNo) {
		return lines.containsKey(lineNo);
	}

	public Object[] getLineInfos() {
		
//		if(isEmptyLine()){
//			return null;
//		}
		
		if(lines.size() > 0){
			
			Object[] result = new Object[lines.size()];
			for(int i = 1 ; i <= lines.size() ; i++){
				
				result[i - 1] = new Object[]{i,getRoleCountLevel(lines.get(i))};
			}
			
			return result;
		}
		
		return null;
	}

	private Object getRoleCountLevel(Integer roleCount) {
		return roleCount < (getMaxLoad() * 0.8) ? ROLE_COUNT_LEVEL_FIRST : roleCount < (getMaxLoad() * 0.95) ? ROLE_COUNT_LEVEL_SECOND : ROLE_COUNT_LEVEL_THIRD ;
	}

	public void incrRoleCount(Integer lineNo) {
		lines.put(lineNo, lines.get(lineNo) + 1);
	}
	
	public void decrRoleCount(Integer lineNo){
		lines.put(lineNo, lines.get(lineNo) - 1);
	}

	
	/**
	 * 线路人数是否已满
	 * @param lineNo
	 * @return true:是
	 */
	public boolean isFullLine(int lineNo){
		int maxCount = getMaxLoad();
		Integer lineCurCount = lines.get(lineNo);
		if(lineCurCount == null){
			lineCurCount = 1;
		}
		
		return lineCurCount >= maxCount;
	}
	
	/**
	 * 动态获取最大承载数量
	 */
	private Integer getMaxLoad() {
		if(null != config){
			return config.getMaxLoad();
		}
		return 100;
//		return 2;
	}

	/**
	 * 动态获取最大承载增量
	 */
	private Integer getMaxLoadIncrease() {
		if(null != config){
			return config.getMaxLoadIncrease();
		}
		return 100;
		
//		return 1;
	}

	/**
	 * 动态获取最大分线
	 */
	private Integer getMaxLine() {
		
		if(null != config){
			return config.getMaxLine();
		}
		
//		return 0;
		return 3;
	}
	
	/**
	 * 默认开启线路
	 */
	private Integer getDefaultLine(){
		
		if(null != config){
			return config.getDefaultLine();
		}
		
		return 0;
		
	}
	
	/**
	 * 获得该地图的所有线数
	 * @return
	 */
	public Collection<Integer> getLines(){
		return lines.keySet();
	}
}
