package com.junyou.bus.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.event.PlatformLogEvent;
import com.junyou.event.publish.GamePublishEvent;

/**
 * 平台日志
 * @author DaoZheng Yuan
 * 2015年6月15日 下午12:16:05
 */
@Service
public class PlatformLogService {

	@Autowired
	private RoleExportService roleExportService;
	
	/**
	 * 打印平台日志
	 * @param userRoleId
	 * @param url
	 */
	public void printPlatformLog(Long userRoleId,String url){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role != null){
			//抛出点击打印平台url日志事件
			GamePublishEvent.publishEvent(new PlatformLogEvent(url, role.getUserId(), userRoleId, role.getName(), role.getLastLoginIp()));
		}
	}
}
