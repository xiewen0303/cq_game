package com.junyou.bus.stagecontroll.position;

import com.junyou.bus.stagecontroll.PostionType;


public class BaguaFubenStagePosition extends AbsRolePosition {
	
	// 多人副本ID
	private Integer fubenId;
	// 场景ID
	private String stageId;
	
	private Integer ceng;
	
	public BaguaFubenStagePosition(Long roleId, Integer mapId, Integer mapType, Integer x, Integer y, Integer fubenId, Integer teamId,Integer ceng) {
		
		this(roleId, teamId.toString()+"_"+ceng.toString(), mapId, mapType, x, y, fubenId,ceng);
	}
	
	public BaguaFubenStagePosition(Long roleId,String stageId, Integer mapId, Integer mapType, Integer x, Integer y, Integer fubenId,Integer ceng){
		super(roleId, mapId, mapType, 0, x, y);
		
		this.fubenId = fubenId;
		this.stageId = stageId;
		this.ceng = ceng;
		
	}

	@Override
	public boolean isCopyMap() {
		return true;
	}

	public Integer getFubenId() {
		return fubenId;
	}

	@Override
	public String getStageId() {
		return stageId;
	}

	public Integer getCeng() {
		return ceng;
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
		return PostionType.BAGUA_COPY_POSITION;
	}
	
}
