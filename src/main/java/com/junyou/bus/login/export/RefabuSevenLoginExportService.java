package com.junyou.bus.login.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.login.entity.RefabuSevenLogin;
import com.junyou.bus.login.service.RefabuSevenLoginService;

/**
 * @author zhongdian
 * 2016-1-13 下午2:50:08
 */
@Service
public class RefabuSevenLoginExportService {

	@Autowired
	private RefabuSevenLoginService refabuSevenLoginService;
	
	
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		return refabuSevenLoginService.getRefbInfo(userRoleId, subId);
	}
	
	
	public List<RefabuSevenLogin> initRefabuSevenLogin(Long userRoleId){
		return refabuSevenLoginService.initRefabuSevenLogin(userRoleId);
	} 

	/**
	 * 上线后调用逻辑
	 * @param userRoleId
	 * @throws InterruptedException
	 */
	public void onlineHandle(Long userRoleId) {
		refabuSevenLoginService.onlineHandle(userRoleId);

	}
}
