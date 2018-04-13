package com.junyou.bus.bagua.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bagua.entity.RoleBagua;
import com.junyou.bus.bagua.service.BaguaFubenTeamService;
import com.junyou.utils.KuafuConfigPropUtil;

@Service
public class BaguaExportService {
	@Autowired
	private BaguaFubenTeamService baguaService;

	public List<RoleBagua> initRoleBagua(Long userRoleId) {
		return baguaService.initRoleBagua(userRoleId);
//		return null;
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		baguaService.onlineHandle(userRoleId);
	}
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		baguaService.offlineHandle(userRoleId);
	}
	public void initHandleOutTimeTeamSchedule(){
		if(KuafuConfigPropUtil.isKuafuServer()){
			baguaService.startHandleOutTimeTeamSchedule();
		}
	}
}
