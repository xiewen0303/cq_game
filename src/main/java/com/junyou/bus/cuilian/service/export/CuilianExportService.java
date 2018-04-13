package com.junyou.bus.cuilian.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.cuilian.entity.RoleCuilian;
import com.junyou.bus.cuilian.service.CuilianService;

@Service
public class CuilianExportService {
	@Autowired
	private CuilianService cuilianService;

	public List<RoleCuilian> initRoleCuilian(Long userRoleId) {
		return cuilianService.initRoleCuilian(userRoleId);
	}
}
