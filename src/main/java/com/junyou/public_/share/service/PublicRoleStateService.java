package com.junyou.public_.share.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.constants.PublicNodeConstants;
import com.junyou.stage.export.StageExternalExportService;
import com.kernel.sync.annotation.Sync;

@Service
public class PublicRoleStateService{
	//在线用户场景数据
	private ConcurrentMap<Long, String> publicRoleStages = new ConcurrentHashMap<>();
	
	@Autowired
	private StageExternalExportService stageExportService;
	
	
	@Sync(component=PublicNodeConstants.PUBLIC_COMPONENT_NAME,indexes={0})
	public void change2PublicOnline(Long roleid) {
		publicRoleStages.put(roleid, "");
	}

	@Sync(component=PublicNodeConstants.PUBLIC_COMPONENT_NAME,indexes={0})
	public void change2PublicOffline(Long roleid) {
		
		publicRoleStages.remove(roleid);
		
		stageExportService.offlineHandle(roleid);
		
	}

	public boolean isPublicOnline(Long roleid) {
		boolean isOnline = false;
		
		try {
			isOnline = publicRoleStages.containsKey(roleid);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		
		return isOnline;
	}

	
	public Collection<Long> getAllOnlineRoleids() {
		return publicRoleStages.keySet();
	}
	
	
	public void roleEnter2PublicStage(Long roleid,String stageId) {
		publicRoleStages.put(roleid, stageId);
	}
	
	
	public String getRolePublicStageId(Long roleid) {
		return publicRoleStages.get(roleid);
	}
	
}
