package com.junyou.bus.lingjing.export;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.lingjing.entity.RoleLingjing;
import com.junyou.bus.lingjing.service.LingJingService;

/**
 * @author LiuYu
 * 2015-6-29 下午3:41:31
 */
@Service
public class LingJingExportService {

	@Autowired
	private LingJingService lingJingService;
	
	public List<RoleLingjing> initRoleLingjing(Long userRoleId) {
		return lingJingService.initRoleLingjing(userRoleId);
	}
	
	public Map<String,Long> getAttribute(Long userRoleId){
		return lingJingService.getAttribute(userRoleId);
	}
}
