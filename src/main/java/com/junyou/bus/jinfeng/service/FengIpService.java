package com.junyou.bus.jinfeng.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.jinfeng.dao.FengIpDao;
import com.junyou.bus.jinfeng.entity.FengIp;
import com.junyou.bus.jinfeng.utils.FengHaoUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.err.HttpErrorCode;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.datetime.GameSystemTime;

@Component
public class FengIpService{

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private FengIpDao fengIpDao;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	
	
	public boolean isFengIp(String ip){
		FengIp fengIp = fengIpDao.getFengIpByIp(ip);
		if(fengIp == null){
			return false;
		}
		if(fengIp.getExpireTime() > 0 && fengIp.getExpireTime() < GameSystemTime.getSystemMillTime()){
			fengIpDao.delete(ip);
			return false;
		}
		return true;
	}
	
	public String fengIp(Long userRoleId,int keepTime,String reasons) {
		if( userRoleId == null ){
			return HttpErrorCode.USER_NOT_EXIST;
		}
		
		RoleWrapper roleWrapper = null;
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			roleWrapper = roleExportService.getLoginRole(userRoleId);
		}else{
			roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
		}
		//角色不存在
		if(roleWrapper == null){
			return HttpErrorCode.ROLE_NOT_EXIST;
		}
		
		//把同IP内的在线玩家踢下线
		String lastLoginIp = roleWrapper.getLastLoginIp();
		Object[] roleIds = sessionManagerExportService.getRoleIdsByIp(lastLoginIp);
		if(roleIds != null){
			
			for( Object obj : roleIds ){
				Long roleId = (Long)obj;
				
				Object result = new Object[]{GameConstants.GM_OFFLINE,lastLoginIp};
				sessionManagerExportService.tiChuOfflineSingle(roleId,result);
			}
		}
		
		FengIp fengIp = fengIpDao.getFengIpByIp(lastLoginIp);
		long expireTime = FengHaoUtil.getExpireTime(keepTime);
		if(fengIp == null){
			fengIp = new FengIp();
			fengIp.setIp(lastLoginIp);
			fengIp.setReasons(reasons);
			fengIp.setServerId(roleWrapper.getServerId());
			fengIp.setExpireTime(expireTime);
			fengIp.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			fengIpDao.cacheInsert(fengIp, userRoleId);
		}else{
			fengIp.setReasons(reasons);
			fengIp.setExpireTime(expireTime);
			fengIpDao.cacheUpdate(fengIp, userRoleId);
		}
		
		return lastLoginIp;
	}
	
	public void removeFengIpByIp(String ip) {
		fengIpDao.delete(ip);
	}
}
