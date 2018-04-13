package com.junyou.bus.rolestage.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.bus.rolestage.service.RoleStageService;
/**
 * 场景
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-10 下午7:25:01 
 */
@Service
public class RoleStageExportService {

	@Autowired
	private RoleStageService roleStageService;
	
	public void roleStageInit(Long userRoleId,Integer configId, Map<Short, Object> otherMsg) {
		roleStageService.roleStageInit(userRoleId,configId,otherMsg);
	}
	
	public RoleStage getRoleStage(Long userRoleId) {
		return roleStageService.getRoleStage(userRoleId);
	}
	
	public RoleStage getRoleStageFromDb(Long userRoleId){
		return roleStageService.getRoleStageFromDb(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		roleStageService.onlineHandle(userRoleId);
	}
	
	public void offlineHandle(Long userRoleId){
		roleStageService.offlineHandle(userRoleId);
	}
	
	public Integer getRoleWearingShenqi(Long userRoleId){
		return getRoleStage(userRoleId).getShenqi();
	}
	
	public void updateShenqi(Long userRoleId,Integer shenqiId){
		roleStageService.updateShenqi(userRoleId, shenqiId);
	}
}