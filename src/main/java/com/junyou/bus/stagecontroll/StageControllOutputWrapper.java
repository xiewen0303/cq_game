package com.junyou.bus.stagecontroll;


public class StageControllOutputWrapper {

	/**
	 * 申请传送地图
	 */
	public static Object[] applyChangeMap(Object mapId,Object x,Object y){
		return new Object[]{1,mapId,new Object[]{x,y}};
	}
	/**
	 * 申请换线
	 */
	public static Object[] applyChangeLine(Object lineId){
		return new Object[]{1,lineId};
	}
}