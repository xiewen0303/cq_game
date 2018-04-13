package com.junyou.bus.stagecontroll.position;

public abstract class AbsRolePosition {

	protected Integer mapId;
	protected Integer mapType;
	protected Integer lineNo;  
	protected Integer x;
	protected Integer y;
	
	protected Long roleId;
	
	protected AbsRolePosition hisRolePosition;
	
	public AbsRolePosition(Long roleId,Integer mapId,Integer mapType,Integer lineNo,Integer x,Integer y) {
		
		this.mapId = mapId;
		this.mapType = mapType;
		this.lineNo = lineNo;
		this.x = x;
		this.y = y;
		
		this.roleId = roleId;
	}

	public Integer getMapId(){
		return mapId;
	}

	public Integer getLineNo(){
		return lineNo;
	}

	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}

	public abstract boolean isCopyMap();
	
	/**
	 * 是否统计分线人数
	 * @return
	 */
	public boolean isStatisticalLineNo(){
		return !isCopyMap();
	}
	
	public abstract int getPostionType();

	public Integer getY(){
		return y;
	}

	public Integer getX(){
		return x;
	}

	public Long getRoleId(){
		return roleId;
	}
	
	public Integer getMapType(){
		return mapType;
	}
	
	public abstract Object[] enterTransformat();
	
	public abstract Object[] remoteEntertransformat();
	
	public abstract String getStageId();

	public AbsRolePosition getHisRolePosition() {
		return hisRolePosition;
	}

	public void setHisRolePosition(AbsRolePosition hisRolePosition) {
		this.hisRolePosition = hisRolePosition;
	}

	public void setPosition(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 切换场景位置信息格式
	 * @return
	 */
	public Object[] changeTransformat(){
		AbsRolePosition hisPosition = getHisRolePosition();
		String leaveStageId = null;
		if(hisPosition != null){
			leaveStageId = hisPosition.getStageId();
		}
		return new Object[]{leaveStageId,getStageId(),getX(),getY()};
	}
}
