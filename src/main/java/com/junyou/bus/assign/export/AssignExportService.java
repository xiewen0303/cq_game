package com.junyou.bus.assign.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.assign.service.AssignService;

@Service
public class AssignExportService {
	@Autowired
	private AssignService assignService;
	/**
	 * 获得总得签到次数
	 * @param userRoleId
	 * @return
	 */
	public int getAssignAll(Long userRoleId) {
		return assignService.getAssignAll(userRoleId);
	}
}
