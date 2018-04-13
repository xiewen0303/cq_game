package com.junyou.bus.stagecontroll;

public class StageUtil {

	public static String getStageId(Integer mapId, Integer lineNo) {
		return new StringBuffer().append(mapId).append("_").append(lineNo).toString();
	}
	
	public static String getMapId(String stageId){
		
		return stageId.substring(0,stageId.indexOf("_"));
	}


	public static Integer getLineNo(String stageId) {
		return Integer.parseInt(stageId.substring(stageId.indexOf("_") + 1,stageId.length()));
	}
	
	/**
	 * 获取巅峰之战场景编号(mapId_loop_room) 
	 * @param mapId 地图编号
	 * @param loop  活动轮次
	 * @param room  活动房间
	 * @return
	 */
	public static String getDianFengStageId(Integer mapId, Integer loop, Integer room){
	    return new StringBuffer().append(mapId).append("_").append(loop).append("_").append(room).toString();
	}
	
}
