package com.junyou.gameconfig.map.configure.export;


public class MapLineConfig{
	
	private Integer mapId;
	
	private Integer maxLoad;
	
	private Integer maxLine;
	
	/**
	 * 默认开启线路数 
	 */
	private Integer defaultLine;
	
	/**
	 * 最大承载量增量，用于循环计算最优繁忙线路所需
	 */
	private Integer maxLoadIncrease;
	
	/**
	 * 默认开启线路数 
	 * @return
	 */
	public Integer getDefaultLine() {
		return defaultLine;
	}

	public void setDefaultLine(Integer defaultLine) {
		this.defaultLine = defaultLine;
	}

	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public Integer getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(Integer maxLoad) {
		this.maxLoad = maxLoad;
	}

	public Integer getMaxLine() {
		return maxLine;
	}

	public void setMaxLine(Integer maxLine) {
		this.maxLine = maxLine;
	}

	public Integer getMaxLoadIncrease() {
		return maxLoadIncrease;
	}

	public void setMaxLoadIncrease(Integer maxLoadIncrease) {
		this.maxLoadIncrease = maxLoadIncrease;
	}
	

}
