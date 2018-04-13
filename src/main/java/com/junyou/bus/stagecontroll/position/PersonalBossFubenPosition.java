package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;
import com.junyou.utils.id.IdUtil;

public class PersonalBossFubenPosition extends AbsRolePosition {

	private String stageId;
	Object attach;
	public PersonalBossFubenPosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y, Object attach) {
		super(roleId, mapId, mapType, 0, x, y);
		stageId = IdUtil.nextString(roleId+"_"+mapType);
		this.attach = attach;
	}

	@Override
	public String getStageId() {
		return stageId;
	}

	public Object getAttach() {
		return attach;
	}

	@Override
	public boolean isCopyMap() {
		return false;
	}

	@Override
	public Object[] enterTransformat() {
		return new Object[]{getStageId(),getX(),getY(),getMapId(), false};
	}

	@Override
	public Object[] remoteEntertransformat() {
		return null;
	}

	@Override
	public int getPostionType() {
		return PostionType.STAGE_COPY_POSITION;
	}
}
