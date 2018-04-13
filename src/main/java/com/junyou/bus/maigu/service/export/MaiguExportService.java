package com.junyou.bus.maigu.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.maigu.entity.RoleMaigu;
import com.junyou.bus.maigu.service.MaiguFubenTeamService;
import com.junyou.utils.KuafuConfigPropUtil;

@Service
public class MaiguExportService {
	@Autowired
	private MaiguFubenTeamService maiguService;
	
	
	public List<RoleMaigu> getRoleMaiguList(Long userRoleId){
		
		return maiguService.getRoleMaiguList(userRoleId);
	}

	public List<RoleMaigu> initRoleMaigu(Long userRoleId) {
		return maiguService.initRoleMaigu(userRoleId);
//		return null;
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		maiguService.onlineHandle(userRoleId);
	}
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		maiguService.offlineHandle(userRoleId);
	}
	public void initHandleOutTimeTeamSchedule(){
		if(KuafuConfigPropUtil.isKuafuServer()){
			maiguService.startHandleOutTimeTeamSchedule();
		}
	}
}
