package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.bus.stagecontroll.StageUtil;

/**
 *@Description  跨服巅峰之战路径信息
 *@Author Yang Gao
 *@Since 2016-5-25
 *@Version 1.1.0
 */
public class KuaFuDianFengStagePosition extends AbsRolePosition {

	// 场景ID
	private String stageId;
	
	private Integer loop;
	
	private Integer room;
	
	
	public KuaFuDianFengStagePosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y,  Integer loop, Integer room) {
	    super(roleId, mapId, mapType, 0, x, y);
	    this.loop = loop;
	    this.room = room;
	    this.stageId = StageUtil.getDianFengStageId(mapId, loop, room);
	}
	
	public Integer getLoop() {
        return loop;
    }

    public Integer getRoom() {
        return room;
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
		return PostionType.KUAFU_DIANFENG_STAGE_POSITION;
	}
	
}
