package com.junyou.bus.kuafuarena1v1.service.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafuarena1v1.entity.RoleGongxunDuihuanInfo;
import com.junyou.bus.kuafuarena1v1.entity.RoleKuafuArena1v1;
import com.junyou.bus.kuafuarena1v1.service.KuafuArena1v1SourceService;
import com.junyou.utils.KuafuConfigPropUtil;

@Service
public class KuafuArena1v1SourceExportService {
	@Autowired
	private KuafuArena1v1SourceService kuafuArena1v1SourceService;

	public List<RoleKuafuArena1v1> initRoleKuafuArena1v1(Long userRoleId) {
		return kuafuArena1v1SourceService.initRoleKuafuArena1v1(userRoleId);
	}

	public List<RoleGongxunDuihuanInfo> initRoleGongxunDuihuanInfo(
			Long userRoleId) {
		return kuafuArena1v1SourceService
				.initRoleGongxunDuihuanInfo(userRoleId);
	}

	// public void offlineHandle(Long userRoleId) {
	// kuafuArenaSingleService.offlineHandle(userRoleId);
	// }
	
	 public void offlineHandle(Long userRoleId) {
		 kuafuArena1v1SourceService.offlineHandle(userRoleId);
	 }

	public void startKuafuArenaCleanJob() {
		if (!KuafuConfigPropUtil.isKuafuServer()) {
			kuafuArena1v1SourceService.startKuafuArenaCleanJob();
		}
	}

	public void clean() {
//		kuafuArena1v1SourceService.clean();
		kuafuArena1v1SourceService.cleanJob();
	}

	public void clean(Long userRoleId) {
		kuafuArena1v1SourceService.cleanUserJifen(userRoleId);
	}
	
	public void addGongxun(Long userRoleId,int gongxun){
		kuafuArena1v1SourceService.addGongxun(userRoleId, gongxun);
	}
	
	public int getGongxun(Long userRoleId){
		return kuafuArena1v1SourceService.getGongxun(userRoleId);
	}
}
