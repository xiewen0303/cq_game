package com.junyou.bus.share.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.junyou.constants.GameConstants;
import com.kernel.sync.annotation.Sync;

@Service
public class RoleStateService{

	private ConcurrentMap<String, Long> roleNameStates = new ConcurrentHashMap<>();
	
	@Sync(component=GameConstants.COMPONENT_BUS_SHARE,indexes={0})
	public boolean isOnline(Long roleid) {
		return roleNameStates.containsValue(roleid);
	}

	public Collection<Long> getAllOnlineRoleids() {
		return roleNameStates.values();
	}

	public void change2online(Long roleid, String rolename) {
		roleNameStates.put(rolename, roleid);
	}

	public void change2offline(Long roleid, String rolename) {
		roleNameStates.remove(rolename, roleid);
	}

	public Long getAllOnlineRoleid(String roleName) {
		if(roleNameStates.containsKey(roleName)){
			return roleNameStates.get(roleName);
		}
		return null;
	}

	public List<Long> getAllOnlineRoleidList(String roleName,int limit) {
		List<Long> list = new ArrayList<>();
		for (String key : roleNameStates.keySet()) {
			if(key.indexOf(roleName) > -1){
				list.add(roleNameStates.get(key));
				if(list.size() >= limit){
					break;
				}
			}
		}
		return list;
	}
}