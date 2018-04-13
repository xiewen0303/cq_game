package com.junyou.stage.configure.export.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.service.RoleBehaviourService;

@Component
public class RoleBehaviourExportService{

	@Autowired
	private RoleBehaviourService roleBehaviourService;
	
	
	public void offOnline(Long userRoleId) {
		roleBehaviourService.saveRoleInfo(userRoleId);
	
	}
	
	//处理打坐下线的场景业务
	public void offOnlineDaZuo(Role role) {
		roleBehaviourService.offOnlineDaZuo(role);
	}
	

	//打断打坐
	public void daZuoCancelHandle(Role role) {
		roleBehaviourService.daZuoCancelHandle(role);
	}
	
	public boolean jumpMissHandle(Role target) {
		return roleBehaviourService.jumpMissHandle(target); 
	}
}
