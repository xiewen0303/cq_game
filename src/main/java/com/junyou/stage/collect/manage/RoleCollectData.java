package com.junyou.stage.collect.manage;

import java.util.HashMap;
import java.util.Map;

public class RoleCollectData {

	private Long guid;
	
	private Map<Long,Long> collectRoles;
	
	
	
	/**
	 * 获取采集物场景内的guid
	 * @return
	 */
	public Long getGuid() {
		return guid;
	}




	public void setGuid(Long guid) {
		this.guid = guid;
	}

	
	/**
	 * 获取玩家开始采集时间
	 * @param roleId
	 * @return
	 */
	public Long getStartCollectTime(Long roleId){
		if(collectRoles == null){
			return 0L;
		}
		
		return collectRoles.get(roleId);
	}
	
	/**
	 * 玩家是否已采集中
	 * @param roleId
	 * @return true:是
	 */
	public boolean isContainRole(Long roleId){
		if(collectRoles == null) return false;
		
		return collectRoles.containsKey(roleId);
	}

	/**
	 * 开始采集记录角色
	 * @param roleId
	 */
	public void addStartCollectRole(Long roleId){
		if(collectRoles == null){
			collectRoles = new HashMap<Long, Long>();
		}
		
		collectRoles.put(roleId, System.currentTimeMillis());
	}
	
	
}
