package com.junyou.public_.share.export;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.configure.export.impl.RoleBehaviourExportService;

@Component
public class PublicRoleStateExportService{

	@Autowired
	private PublicRoleStateService publicRoleStateService;
	
	@Autowired
	private RoleBehaviourExportService roleBExportService;
	
	public void change2PublicOnline(Long roleid) {
		publicRoleStateService.change2PublicOnline(roleid);
		
	}

	public void change2PublicOffline(Long roleid) {
		
		publicRoleStateService.change2PublicOffline(roleid);
	}

	/**
	 * 判断一个角色是否在线
	 * @param roleid
	 * @return true在线
	 */
	public boolean isPublicOnline(Long roleid) {
		return publicRoleStateService.isPublicOnline(roleid);
	}

	/**
	 * 角色进入场景记录
	 * @param roleid
	 * @param stageId
	 */
	public void roleEnter2PublicStage(Long roleid,String stageId){
		publicRoleStateService.roleEnter2PublicStage(roleid, stageId); 
	}
	
	/**
	 * 获取角色场景ID
	 * @param roleid
	 * @return
	 */
	public String getRolePublicStageId(Long roleid){
		return publicRoleStateService.getRolePublicStageId(roleid);
	}
	/**
	 * 获取所有在线角色id
	 * 	注：一遍历时一定要加try-catch!
	 * @return
	 */
	public List<Long> getAllOnlineRoleids() {
		List<Long> list = new ArrayList<>(publicRoleStateService.getAllOnlineRoleids().size());
		for (Long roleId : publicRoleStateService.getAllOnlineRoleids()) {
			if(roleId > 0){
				list.add(roleId);
			}
		}
		return list;
	}
}
