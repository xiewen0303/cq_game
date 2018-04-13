package com.junyou.bus.offlineexp.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.offlineexp.service.OfflineExpService;

/**
 * 离线经验对外接口
 * @author jy
 *
 */
@Service
public class OfflineExpExportService {
	
	 @Autowired
	 private OfflineExpService offlineExpService;
	 
	 public void loginHandler(Long userRoleId){
		 offlineExpService.login(userRoleId);
	 }
 
	 /**
	  * 获取离线是否有奖励
	  * @param userRoleId
	  * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的2次方
	  */
	 public int getOfflineStateValue(Long userRoleId){
		 return offlineExpService.getOfflineStateValue(userRoleId);
	 }
}