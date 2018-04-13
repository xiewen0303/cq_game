package com.junyou.gameconfig.export;

import java.util.ArrayList;
import java.util.List;

public class ResourceRefreshConfig {
	
	private String teamId;
	
	private int maxCount;
	
	private String resourceId;
	
	private String resourceName;
	
	private int refreshType;
	
	private int refreshInterval;  //怪物刷新时间
	
	private int resourceType;
	
	private List<Integer[]> point;

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getRefreshType() {
		return refreshType;
	}

	public void setRefreshType(int refreshType) {
		this.refreshType = refreshType;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public int getResourceType() {
		return resourceType;
	}

	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}

	public List<Integer[]> getPoint() {
		
		if(null != point){
			return new ArrayList<Integer[]>(point);
		}
		
		return point;
	}

	public void setPoint(List<Integer[]> point) {
		this.point = point;
	}

}
