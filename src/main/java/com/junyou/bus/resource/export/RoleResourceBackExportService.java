package com.junyou.bus.resource.export;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.resource.service.RoleResourceBackService;
/**
 * @author LiuYu
 * 2015-7-7 下午3:25:33
 */
@Service
public class RoleResourceBackExportService {
	
	@Autowired
	private RoleResourceBackService roleResourceBackService;
	
	public void onlineHandle(Long userRoleId){
		roleResourceBackService.onlineHandle(userRoleId);
	}
	
	public void updateDaTiTime(Long userRoleId){
		roleResourceBackService.updateDaTiTime(userRoleId);
	}
	
	public void changeTypeMap(Long userRoleId,Map<String,Map<String,Integer>> map,String type){
		roleResourceBackService.changeTypeMap(userRoleId, map, type);
	}
	
	public int getResourceBackStateValue(Long userRoleId){
		return roleResourceBackService.getResourceBackStateValue(userRoleId);
	}
}
