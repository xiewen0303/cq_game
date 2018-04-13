package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.kuafuarena1v1.constants.KuafuArena1v1Constants;
import com.junyou.bus.stagecontroll.PostionType;


public class KuafuArenaFubenStagePosition extends AbsRolePosition {
	// 场景ID
	private String stageId;
	
	public KuafuArenaFubenStagePosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y, Long matchId) {
		
		this(roleId, KuafuArena1v1Constants.STAGE_ID_PREFIX+matchId, mapId, mapType, x, y);
	}
	
	public KuafuArenaFubenStagePosition(Long roleId,String stageId, Integer mapId, Integer mapType, Integer x, Integer y){
		super(roleId, mapId, mapType, 0, x, y);
		
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
		return new Object[]{getMapId(),getX(),getY(),getMapType(),getStageId()};
	}

	@Override
	public int getPostionType() {
		return PostionType.KUAFU_ARENA_SINGLE;
	}
	
}
