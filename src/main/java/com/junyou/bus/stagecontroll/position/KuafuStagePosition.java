package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;


public class KuafuStagePosition extends AbsRolePosition {
	
	private Object[] additionalData;
	
	private String stageId;
	/**
	 * 场景是否存在
	 */
	private boolean stageExist;
	
	public KuafuStagePosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y, Object[] additionalData) {
		
		this(roleId, mapId + "_0", mapId, mapType, x, y, additionalData);
	}
	
	public KuafuStagePosition(Long roleId,String stageId, Integer mapId, Integer mapType, Integer x, Integer y, Object[] additionalData){
		super(roleId, mapId, mapType, 1, x, y);
		
		this.additionalData = additionalData;
		this.stageId = stageId;
		
	}

	@Override
	public boolean isCopyMap() {
		return true;
	}

	@Override
	public String getStageId() {
		return stageId;
	}

	public Object[] getAdditionalData() {
		return additionalData;
	}
	
	/**
	 * 登陆场景位置信息格式
	 */
	public Object[] enterTransformat(){
		return new Object[]{getStageId(),getX(),getY(),getMapId(), false};
	}

	/**
	 * 远程登录场景位置信息格式
	 */
	public Object[] remoteEntertransformat(){
		return new Object[]{getMapId(),getX(),getY(),getMapType(),getStageId(),additionalData};
	}

	public boolean isStageExist() {
		return stageExist;
	}

	public void setStageExist(boolean stageExist) {
		this.stageExist = stageExist;
	}

	@Override
	public int getPostionType() {
		return PostionType.KUAFU_STAGE_POSITION;
	}
	
}
