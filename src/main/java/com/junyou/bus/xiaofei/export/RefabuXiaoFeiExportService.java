package com.junyou.bus.xiaofei.export;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.xiaofei.entity.RefabuXiaofei;
import com.junyou.bus.xiaofei.server.RefabuXiaoFeiService;



@Service
public class RefabuXiaoFeiExportService {

	@Autowired
	private RefabuXiaoFeiService xiaofeiService;
	
	public void quartXiaoFei(){
		xiaofeiService.quartXiaoFei();
	}
	
	public List<RefabuXiaofei> initRefabuXiaofei(Long userRoleId){
		return xiaofeiService.initRefabuXiaofei(userRoleId);
	}
	
	public Object[] getRefbXiaoFeiInfo(Long userRoleId, Integer subId){
		return xiaofeiService.getRefbInfo(userRoleId, subId);
	} 
	
	public Object[] getRefbXiaoFeiLingQuStatus(Long userRoleId,Integer subId){
		return xiaofeiService.getRefbLingQuStatus(userRoleId, subId);
	}
	
	
	public void xiaofeiYb(Long userRoleId,Long yb){
		xiaofeiService.xiaofeiYb(userRoleId, yb);
	}
	
	/**
	 * 结算邮件
	 */
	public void xfJiangLiEmail() {
		xiaofeiService.xfJiangLiEmail();
	}
}
