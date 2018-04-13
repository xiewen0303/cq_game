/**
 * 
 */
package com.junyou.stage.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.rolestage.export.RoleStageExportService;
import com.junyou.bus.stagecontroll.position.AbsRolePosition;
import com.junyou.bus.stagecontroll.position.BaguaFubenStagePosition;
import com.junyou.bus.stagecontroll.position.KuaFuDaLuanDouStagePosition;
import com.junyou.bus.stagecontroll.position.KuaFuDianFengStagePosition;
import com.junyou.bus.stagecontroll.position.KuaFuYunGongStagePosition;
import com.junyou.bus.stagecontroll.position.KuafuArenaFubenStagePosition;
import com.junyou.bus.stagecontroll.position.MaiguFubenStagePosition;
import com.junyou.bus.stagecontroll.position.MoreFubenStagePosition;
import com.junyou.bus.stagecontroll.position.PublicFubenPosition;
import com.junyou.bus.stagecontroll.position.SafePosition;
import com.junyou.bus.stagecontroll.position.StageCopyPosition;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.service.StageService;
import com.kernel.spring.container.DataContainer;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-2上午11:05:20
 */
@Component
public class StageExternalExportService{

	@Autowired
	private StageService stageService;
	@Autowired
	private RoleStageExportService roleStageExportService;
	@Autowired
	private DataContainer dataContainer;
	
	public Object getRoleStageData(Long userRoleId, String curStageId) {
		return stageService.getStageData(userRoleId,curStageId);
	}
	

	public void offlineHandle(Long userRoleId){

		//宠物下线处理	Liuyu：暂时不清除数据
//		PetVo petVo = dataContainer.getData(GameConstants.COMPONENT_NAME_PET, userRoleId.toString());
//		if(petVo != null){
//			dataContainer.removeData(GameConstants.COMPONENT_NAME_PET, userRoleId.toString());
//		}
		
		//镖车下线处理
		Biaoche biaoche = dataContainer.getData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		if(biaoche != null){
			
			if(biaoche.getStage() != null){
				biaoche.getStage().leave(biaoche);
			}
			
			if(biaoche.getOwner() != null){
				biaoche.getOwner().setBiaoche(null);
			}
			dataContainer.removeData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		}
		
	}
	
	

	public Integer[] getPosition(Long roleId, String stageId) {
		return stageService.getPosition(roleId,stageId);
	}

	public Object teleportCheck(Long roleId, String stageId) {
		return stageService.teleportCheck(roleId,stageId);
	}

	public boolean inFighting(Long roleId, String stageId) {
		return stageService.inFighting(roleId,stageId);
	}
	
	public boolean isWarTime(){
		return false;
	}

	public RoleStageWrapper getRoleStageFromDb(Long userRoleId) {
		return new RoleStageWrapper(roleStageExportService.getRoleStageFromDb(userRoleId));
	}

	public RoleStageWrapper getRoleStage(Long userRoleId) {
		return new RoleStageWrapper(roleStageExportService.getRoleStage(userRoleId));
	}
	
	public void syncRoleStageData(Long userRoleId, AbsRolePosition curPosition,Integer targetId,boolean exitGameOrNot){
		stageService.syncRoleStageData(userRoleId, curPosition,targetId, exitGameOrNot);
	}
	
	public void saveExceptionStageData(Long userRoleId, Integer mapId){
		stageService.saveExceptionStageData(userRoleId, mapId);
	}
	
	public boolean checkAndCreateStage(String stageId,Integer mapId,Integer lineNo) throws Exception {
		return stageService.checkAndCreateStage(stageId,mapId,lineNo);
	}
	
	public boolean checkPublicFubenStageIsExist(PublicFubenPosition publicFubenPosition) {
		return stageService.checkPublicFubenStageIsExist(publicFubenPosition.getStageId(),publicFubenPosition.getMapType(),publicFubenPosition.getActiveType());
	}
	
	//	public void checkAndCreateKuafuFuben(KuafuStagePosition kuafuStagePosition) {
	//	stageService.checkAndCreateKuafuFuben(kuafuStagePosition.getRoleId(), kuafuStagePosition.getStageId(), kuafuStagePosition.getMapId(), kuafuStagePosition.getMapType());
	//}
	
	public void createFuben(StageCopyPosition stageCopyPosition) {
		stageService.createFuben(stageCopyPosition.getRoleId(), stageCopyPosition.getStageId(), stageCopyPosition.getMapId(), stageCopyPosition.getFubenId(), stageCopyPosition.getMapType(), stageCopyPosition.getData(),stageCopyPosition.getDeadDisplay());
	}
	
	public void checkAndCreateSafeStage(SafePosition safePosition) {
		stageService.checkAndCreateSafeStage(safePosition.getRoleId(), safePosition.getStageId(), safePosition.getMapId(),safePosition.getMapType());
	}
	
	public void checkAndCreateMoreFuben(MoreFubenStagePosition morePosition){
		stageService.checkAndCreateMoreFuben(morePosition.getRoleId(), morePosition.getStageId(), morePosition.getMapId(),morePosition.getMapType(),morePosition.getFubenId());
	}
	
	public void checkAndCreateBaguaFuben(BaguaFubenStagePosition baguaPosition){
		stageService.checkAndCreateBaguaFuben(baguaPosition.getRoleId(), baguaPosition.getStageId(), baguaPosition.getMapId(),baguaPosition.getMapType(),baguaPosition.getFubenId(),baguaPosition.getCeng());
	}
	public void checkAndCreateMaiguFuben(MaiguFubenStagePosition maiguPosition){
		stageService.checkAndCreateMaiguFuben(maiguPosition.getRoleId(), maiguPosition.getStageId(), maiguPosition.getMapId(),maiguPosition.getMapType(),maiguPosition.getFubenId());
	}
	public void checkAndCreateDaLuanDouFuben(KuaFuDaLuanDouStagePosition maiguPosition){
		stageService.checkAndCreateDaLuanDou(maiguPosition.getRoleId(), maiguPosition.getStageId(), maiguPosition.getMapId(),maiguPosition.getMapType());
	}
	public void checkAndCreateKuafuArenaSingle(KuafuArenaFubenStagePosition morePosition){
		stageService.checkAndCreateKuafuArenaSingle(morePosition.getRoleId(), morePosition.getStageId(), morePosition.getMapId(),morePosition.getMapType());
	}
	
    public void checkAndCreateDianFengStage(KuaFuDianFengStagePosition dfPosition) {
        stageService.checkAndCreateDianFengStage(dfPosition.getStageId(), dfPosition.getMapId(), dfPosition.getLoop(), dfPosition.getRoom(), dfPosition.getMapType());
    }

    public void checkAndCreateYunGongStage(KuaFuYunGongStagePosition ygPosition) {
        stageService.checkAndCreateYunGongStage(ygPosition.getStageId(), ygPosition.getMapId(), ygPosition.getMapType());
    }

}