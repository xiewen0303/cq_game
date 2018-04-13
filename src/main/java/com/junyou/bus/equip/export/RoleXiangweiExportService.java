package com.junyou.bus.equip.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.equip.entity.RoleXiangwei;
import com.junyou.bus.equip.service.RoleXiangweiService;

@Service
public class RoleXiangweiExportService {
	@Autowired
	private RoleXiangweiService roleXiangweiService;
	
	public List<RoleXiangwei> initRoleXiangwei(Long userRoleId) {
		return roleXiangweiService.initRoleXiangwei(userRoleId);
	}
	public Map<String, Long> getSuitXiangweiAttr(Long userRoleId) {
		return roleXiangweiService.getRoleAttrs(userRoleId);
	}
}
