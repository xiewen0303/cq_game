package com.junyou.bus.online.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.online.service.RoleOnlineService;

/**
 * 签到对外接口
 * @author jy
 *
 */
@Service
public class RoleOnlineExportService {
	
	 @Autowired
	 private RoleOnlineService roleOnlineService;
	 
	 public void loginHandler(Long userRoleId){
		 roleOnlineService.login(userRoleId);
	 }
	 
	 public void offilineHandler(Long userRoleId){
		 roleOnlineService.logout(userRoleId);
	 }
	 
	 /**
	  * 凌晨定时打印昨日在线
	  */
	 public void quartzLog(){
		 roleOnlineService.quartzLog();
	 }
	 
	 /**
	  * 获取在线是否有奖励
	  * @param userRoleId
	  * @return
	  */
	 public int getOnlineStateValue(Long userRoleId){
		 return roleOnlineService.getOnlineStateValue(userRoleId);
	 }
	 /**
	  *  获取总计在线时间（单位毫秒）
	  * @param userRoleId
	  * @return
	  */
	 public long getTotalOnlineTime(Long userRoleId){
		return roleOnlineService.getTotalOnlineTime(userRoleId);
	 }
}