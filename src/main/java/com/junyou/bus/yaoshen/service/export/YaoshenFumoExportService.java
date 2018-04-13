package com.junyou.bus.yaoshen.service.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.yaoshen.entity.RoleYaoshenFumo;
import com.junyou.bus.yaoshen.service.YaoshenFumoService;

@Service
public class YaoshenFumoExportService  {
	@Autowired
	private YaoshenFumoService yaoshenFumoService;
	
	
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenFumo initRoleYaoshenMoYin(Long userRoleId) {
		
		return yaoshenFumoService.initRoleYaoshenFumo(userRoleId);
	}
	/** 
	 * 玩家登陆后统计属性
	 */
	public Map<String, Long> getYaoshenFumoAttributeAfterLogin(Long userRoleId) {
		 
		return yaoshenFumoService.getYaoshenFumoAttributeAfterLogin(userRoleId);
	}
	/**
	 * 妖神附魔属性总和
	 */
	public Map<String, Long> getYaoshenFumoAttribute(Long userRoleId){
		
		return yaoshenFumoService.countAllAttr(userRoleId);
	}
	 
}
