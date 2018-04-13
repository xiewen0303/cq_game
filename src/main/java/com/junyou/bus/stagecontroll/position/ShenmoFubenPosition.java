package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.shenmo.constants.ShenmoConstants;

/**
 * 神魔战场场景
 * @author LiuYu
 * @date 2015-6-15 下午4:46:39
 */
public class ShenmoFubenPosition extends PublicFubenPosition {
	
	private String stageId;
	
	public ShenmoFubenPosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y,Long matchId) {
		this(roleId, mapId, mapType, x, y);
		stageId = ShenmoConstants.STAGE_ID_PREFIX + matchId;
	}
	
	public ShenmoFubenPosition(Long roleId, Integer mapId,  Integer mapType, Integer x, Integer y){
		super(roleId, mapId, mapType, x, y, null);
		
	}
	
	@Override
	public boolean isStatisticalLineNo(){
		return false;
	}
	/**
	 * 登陆场景位置信息格式
	 */
	public Object[] enterTransformat(){
		return new Object[]{getStageId(),getX(),getY(),getMapId(), false};
	}

	@Override
	public Object[] remoteEntertransformat() {
		return null;
	}

	@Override
	public String getStageId() {
		return stageId;
	}
	
}
