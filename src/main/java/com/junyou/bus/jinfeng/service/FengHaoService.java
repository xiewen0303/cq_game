package com.junyou.bus.jinfeng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.jinfeng.dao.FengHaoDao;
import com.junyou.bus.jinfeng.entity.FengHao;
import com.junyou.bus.jinfeng.utils.FengHaoUtil;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.constants.GameConstants;
import com.junyou.err.HttpErrorCode;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;


@Service
public class FengHaoService{
	
	@Autowired
	private FengHaoDao fengHaoDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	
	public String freeze(String userId, String serverId,int sort,String reasons, int keepTime) {
		
		if( ObjectUtil.strIsEmpty(userId) || ObjectUtil.strIsEmpty(serverId) ){
			return HttpErrorCode.USER_NOT_EXIST;
		}
		
		UserRole role = roleExportService.getRoleFromDb(userId, serverId);
		
		if(role == null){
			return HttpErrorCode.ROLE_NOT_EXIST;
		}
		
		reasons = FengHaoUtil.getReasonBySort(sort, reasons);
		Long roleId = role.getId();
		return freeze(roleId, sort, reasons, keepTime);
	}

	public String freeze(Long roleId,int sort,String reasons, int keepTime) {
		if(roleId == null){
			return HttpErrorCode.USER_NOT_EXIST;
		}
		
		FengHao fenghao = fengHaoDao.selectFromDB(roleId);
		long expTime = FengHaoUtil.getExpireTime(keepTime);
		if(fenghao != null){
			fenghao.setReasons(reasons);
			fenghao.setExpireTime(expTime);
			fengHaoDao.updateFromDB(fenghao);
		}else{
			fenghao = new FengHao();
			fenghao.setUserRoleId(roleId);
			fenghao.setReasons(reasons);
			fenghao.setExpireTime(expTime);
			fengHaoDao.insert2DB(fenghao);
		}
		
		if( publicRoleStateExportService.isPublicOnline(roleId) ){
			//封号强踢下线
			Object result = new Object[]{GameConstants.GM_OFFLINE,FengHaoUtil.getReasonBySort(sort, reasons)};
			sessionManagerExportService.tiChuOfflineSingle(roleId,result);
		}
		return HttpErrorCode.SUCCESS;
	}

	public String removeFreeze(Long roleId) {

		if( roleId == null ){
			return HttpErrorCode.USER_NOT_EXIST;
		}
		
		fengHaoDao.deleteFromDB(roleId);
		return HttpErrorCode.SUCCESS;
	}



	public boolean isFengHao(Long userRoleId) {
		FengHao fenghao = fengHaoDao.selectFromDB(userRoleId);
		if(fenghao == null)return false;
		long expTime = fenghao.getExpireTime();
		if(expTime > 0 && expTime < GameSystemTime.getSystemMillTime()){
			fengHaoDao.deleteFromDB(userRoleId);
			return false;
		}
		return true;
	}

}