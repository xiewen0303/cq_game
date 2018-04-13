package com.junyou.bus.platform.taiwan.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.qq.entity.TencentUserInfo;
import com.junyou.bus.platform.taiwan.service.TaiWanService;
/**
 * @author zhongdian
 * 2015-7-29 下午2:08:06
 */
@Service
public class TaiWanExportService {

	
	@Autowired
	private TaiWanService taiWanService;
	
	/**
	  * 腾讯via用户绑定
	  * @param userRoleId
	  */
	 public void tencentViaUser(Long userRoleId){
		 taiWanService.tencentViaUser(userRoleId);
	 }
	 public List<TencentUserInfo> initTencentUserInfos(Long userRoleId){
		 return taiWanService.initTencentUserInfos(userRoleId);
	 }
	 public String getUserZhuCePf(Long userRoleId){
		 return taiWanService.getUserZhuCePf(userRoleId);
	 }
	 
	 
}
