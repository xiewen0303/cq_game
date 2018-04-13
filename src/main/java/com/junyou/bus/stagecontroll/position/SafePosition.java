package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;


public class SafePosition extends AbsRolePosition{
	
	private String stageId;
	
	public SafePosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y) {
		super(roleId, mapId, mapType, 0, x, y);
		stageId = mapId + "_" + mapType;
	}

	@Override
	public boolean isCopyMap() {
		return true;
	}

	@Override
	public Object[] enterTransformat() {
		return null;
	}

	@Override
	public Object[] remoteEntertransformat() {
		return null;
	}

	@Override
	public String getStageId() {
		return stageId;
	}

	@Override
	public int getPostionType() {
		return PostionType.SAFE_POSITION;
	}
	
}
