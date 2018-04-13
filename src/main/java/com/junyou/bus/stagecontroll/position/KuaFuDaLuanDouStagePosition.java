package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.log.ChuanQiLog;


public class KuaFuDaLuanDouStagePosition extends AbsRolePosition {
	
	// 多人副本ID
	private Integer fubenId = null;
	// 场景ID
	private String stageId;
	
	public KuaFuDaLuanDouStagePosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y,  Integer teamId) {
		
		this(roleId, mapId+"_"+teamId.toString(), mapId, mapType, x, y);
	}
	
	public KuaFuDaLuanDouStagePosition(Long roleId,String stageId, Integer mapId, Integer mapType, Integer x, Integer y){
		super(roleId, mapId, mapType, 0, x, y);
		ChuanQiLog.error("KuaFuDaLuanDouStagePosition场景ID："+stageId);
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
		return new Object[]{getMapId(),getX(),getY(),getMapType(),getStageId(),fubenId};
	}

	@Override
	public int getPostionType() {
		return PostionType.KUAFUDALUANDOU_COPY_POSITION;
	}
	
}
