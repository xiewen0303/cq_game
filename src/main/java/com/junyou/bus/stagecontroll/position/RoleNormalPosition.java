package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.bus.stagecontroll.StageUtil;

public class RoleNormalPosition extends AbsRolePosition{
	
	/**
	 * @param roleId 角色id
	 * @param mapId 地图编号
	 * @param mapLine 地图线路
	 * @param x 
	 * @param y
	 */
	public RoleNormalPosition(Long roleId,Integer mapId,Integer mapLine,Integer x, Integer y) {
		this(roleId,mapId, mapLine, MapType.NORMAL_MAP, x, y);
	}
	
	/**
	 * @param roleId 角色id
	 * @param mapId 地图编号
	 * @param mapLine 地图线路
	 * @param mapType 地图类型{@link MapType}
	 * @param x
	 * @param y
	 */
	public RoleNormalPosition(Long roleId,Integer mapId,Integer mapLine,Integer mapType,Integer x, Integer y){
		super(roleId, mapId, mapType, mapLine, x, y);
	}

	@Override
	public boolean isCopyMap() {
		return false;
	}

	@Override
	public String getStageId() {
		return StageUtil.getStageId(mapId, lineNo);
	}
	
	/**
	 * 登陆场景位置信息格式
	 */
	public Object[] enterTransformat(){
		//最后位代表是否为同场景切线
		return new Object[]{getStageId(),getX(),getY(),getMapId(),false};
	}

	/**
	 * 远程登录场景位置信息格式
	 */
	public Object[] remoteEntertransformat(){
		return new Object[]{getMapId(),getX(),getY(),getMapType(),getStageId(),null};
	}

	@Override
	public int getPostionType() {
		return PostionType.ROLE_NORMAL_POSITION;
	}
}
