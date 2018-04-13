package com.junyou.bus.stagecontroll.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.stagecontroll.service.StageControllService;

@Component
public class StageControllExportService {

	@Autowired
	private StageControllService stageControllService;
	
	public void exit(Long roleId) {
		stageControllService.exit(roleId);
	}

	
	public void login(Long userRoleId) {
		stageControllService.login(userRoleId);
	}

	
	public Integer getCurMapId(Long roleId) {
		return stageControllService.getCurMapId(roleId);
	}


	
	public String getCurStageId(Long roleId) {
		return stageControllService.getCurStageId(roleId);
	}
	
	public String[] getStageIdsByMapId(String mapId){
		return stageControllService.getStageIdsByMapId(mapId);
	}
	
	
	/**
	 * 是否在副本中
	 * @param roleId
	 * @return true:在副本中
	 */
	public boolean inFuben(Long roleId){
		return stageControllService.inFuben(roleId);
	}

	/**
	 * 修改副本状态
	 * @param roleId
	 * @param fuben	是否是在副本中
	 */
	public void changeFuben(Long roleId,boolean fuben){
		stageControllService.changeFuben(roleId, fuben);
	}
	
	public boolean isChanging(Long roleId){
		return stageControllService.isChanging(roleId);
	}
	
	public void setChanging(Long roleId,boolean changing){
		stageControllService.setChanging(roleId, changing);
	}
}
