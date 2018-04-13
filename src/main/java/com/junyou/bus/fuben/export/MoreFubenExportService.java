package com.junyou.bus.fuben.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.MoreFuben;
import com.junyou.bus.fuben.service.MoreFubenTeamService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.utils.KuafuConfigPropUtil;

@Service
public class MoreFubenExportService {
	@Autowired
	private MoreFubenTeamService moreFubenTeamService;

	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public List<MoreFuben> initFuben(Long userRoleId){
		return moreFubenTeamService.initMoreFuben(userRoleId);
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		moreFubenTeamService.onlineHandle(userRoleId);
	}
	
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		moreFubenTeamService.offlineHandle(userRoleId);
	}
	
	public void initHandleOutTimeTeamSchedule(){
		if(KuafuConfigPropUtil.isKuafuServer()){
			moreFubenTeamService.startHandleOutTimeTeamSchedule();
		}
	}
}
