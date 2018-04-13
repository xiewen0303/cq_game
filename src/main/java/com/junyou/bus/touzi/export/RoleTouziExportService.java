package com.junyou.bus.touzi.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.touzi.entity.RoleTouzi;
import com.junyou.bus.touzi.service.RoleTouziService;

/**
 * 投资计划
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-6-8 下午9:42:28 
 */
@Component
public class RoleTouziExportService {
	@Autowired
	private RoleTouziService roleTouziService;
	
	public List<RoleTouzi> initRoleTouzi(Long userRoleId){
		return roleTouziService.initRoleTouzi(userRoleId);
	}
	
	/**
	 * 上线处理
	 * @param userRoleId
	 */
	public void onlineTouzi(Long userRoleId){
		roleTouziService.onlineTouzi(userRoleId);
	}
}
