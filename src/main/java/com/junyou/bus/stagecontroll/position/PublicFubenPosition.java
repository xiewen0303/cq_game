package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.bus.stagecontroll.StageUtil;
/**
 * 公共副本场景
 * @author LiuYu
 * @date 2015-6-15 下午4:46:39
 */
public class PublicFubenPosition extends AbsRolePosition {
	
	private Integer activeType;
	
	public PublicFubenPosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y,Integer activeType) {
		this(roleId, mapId, mapType, x, y);
		this.activeType = activeType;
	}
	
	public PublicFubenPosition(Long roleId, Integer mapId,  Integer mapType, Integer x, Integer y){
		super(roleId, mapId, mapType, 0, x, y);
		
	}

	@Override
	public boolean isCopyMap() {
		return true;
	}
	
	public boolean isStatisticalLineNo(){
		return true;
	}

	/**
	 * 登陆场景位置信息格式
	 */
	public Object[] enterTransformat(){
		return new Object[]{getStageId(),getX(),getY(),getMapId(), false};
	}

	@Override
	public int getPostionType() {
		return PostionType.PUBLIC_FUBEN_POSITION;
	}

	@Override
	public Object[] remoteEntertransformat() {
		return null;
	}

	public Integer getActiveType() {
		return activeType;
	}

	@Override
	public String getStageId() {
		return StageUtil.getStageId(mapId, lineNo);
	}
	
}
