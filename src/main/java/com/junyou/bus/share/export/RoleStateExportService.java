package com.junyou.bus.share.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.share.service.RoleStateService;

@Component
public class RoleStateExportService{

	@Autowired
	private RoleStateService roleStateService;
	
	public boolean isOnline(Long roleid) {
		return roleStateService.isOnline(roleid);
	}

	public void change2online(Long roleid, String rolename) {
		roleStateService.change2online(roleid, rolename);
	}

	public void change2offline(Long roleid, String rolename) {
		roleStateService.change2offline(roleid, rolename);
	}

	public Long getAllOnlineRoleid(String roleName) {
		return roleStateService.getAllOnlineRoleid(roleName);
	}

	public List<Long> getAllOnlineRoleidList(String roleName, int limit) {
		return roleStateService.getAllOnlineRoleidList(roleName, limit);
	}
}
