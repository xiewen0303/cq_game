package com.junyou.bus.setting.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.setting.service.RoleSettingService;

/**
 * 角色设置（快捷键等）
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-16 下午10:56:05 
 */
@Component
public class RoleSettingExportService {
	@Autowired
	private RoleSettingService roleSettingService;
	/**
	 * 获取玩家快捷键信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getQuickbarInfo(Long userRoleId){
		return roleSettingService.getQuickbarInfo(userRoleId);
	}
	/**
	 * 获取玩家挂机设置信息
	 * @param userRoleId
	 * @return
	 */
	public Object getGuajiInfo(Long userRoleId){
		return roleSettingService.getGuajiInfo(userRoleId);
	}
	
	/**
	 * 是否设置拒绝
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public boolean isSetting(Long userRoleId,String type){
		return roleSettingService.isSetting(userRoleId, type);
	}
}
