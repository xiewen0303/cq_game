package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.utils.id.IdUtil;

public class StageCopyPosition extends AbsRolePosition {
	
	private Integer fubenId;
	
	private String stageId;
	
	private Object data;
	
	private DeadDisplay deadDisplay;
	
	/**
	 * 场景是否存在
	 */
	private boolean stageExist;

	public StageCopyPosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y, Integer fubenId, Object data) {
	    this(roleId, mapId, mapType, x, y, fubenId);
	    this.data = data;
	}
	
	public StageCopyPosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y, Integer fubenId) {
		
		this(roleId, IdUtil.nextString(new StringBuffer().append(roleId).append("_").append(mapType).toString()), mapId, mapType, x, y, fubenId);
	}
	

	public StageCopyPosition(Long roleId,String stageId, Integer mapId, Integer mapType, Integer x, Integer y, Integer fubenId){
		super(roleId, mapId, mapType, 0, x, y);
		
		this.fubenId = fubenId;
		this.stageId = stageId;
		
	}

	@Override
	public boolean isCopyMap() {
		return true;
	}

	public Integer getFubenId() {
		return fubenId;
	}

	
	public Object getData() {
        return data;
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

	public boolean isStageExist() {
		return stageExist;
	}

	public void setStageExist(boolean stageExist) {
		this.stageExist = stageExist;
	}

	@Override
	public int getPostionType() {
		return PostionType.STAGE_COPY_POSITION;
	}

	public DeadDisplay getDeadDisplay() {
		return deadDisplay;
	}

	public void setDeadDisplay(DeadDisplay deadDisplay) {
		this.deadDisplay = deadDisplay;
	}
}
