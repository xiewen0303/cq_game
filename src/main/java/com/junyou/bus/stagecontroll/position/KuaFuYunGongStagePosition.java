package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.bus.stagecontroll.StageUtil;

/**
 * 
 *@Description 跨服云宫之巅路径信息
 *@Author Yang Gao
 *@Since 2016-9-29
 *@Version 1.1.0
 */

public class KuaFuYunGongStagePosition extends AbsRolePosition {

	// 场景ID
	private String stageId;
	
	public KuaFuYunGongStagePosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y) {
	    super(roleId, mapId, mapType, 0, x, y);
	    this.stageId = StageUtil.getStageId(mapId, 1);
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
		return new Object[]{getMapId(),getX(),getY(),getMapType(),getStageId(),null};
	}

	@Override
	public int getPostionType() {
		return PostionType.KUAFU_YUNGONG_STAGE_POSITION;
	}
	
}
