package com.junyou.bus.stagecontroll;

public class StageLine {
	
	private Integer mapId;
	private Integer lineNo;
	private boolean isNew;

	public StageLine(Integer mapId, int lineNo) {
		this.mapId = mapId;
		this.lineNo = lineNo;
	}
	
	public StageLine(Integer mapId, int lineNo,boolean isNew){
		this.mapId = mapId;
		this.lineNo = lineNo;
		this.isNew = isNew;
	}

	/**
	 * 是否新创建
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * 获取线号
	 */
	public Integer getLineNo() {
		return lineNo;
	}

	/**
	 * 获取地图ID
	 * @return
	 */
	public Integer getMapId(){
		return mapId;
	}
}
