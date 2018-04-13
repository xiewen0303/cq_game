package com.junyou.bus.email.export;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.entity.EmailRelation;
import com.junyou.bus.email.service.EmailRelationService;
import com.junyou.log.ChuanQiLog;

@Service
public class EmailRelationExportService {

	@Autowired
	private EmailRelationService emailRelationService;
	
	public List<EmailRelation> initEmailRelation(Long userRoleId){
		return emailRelationService.initEmailRelation(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		try{
			emailRelationService.onlineHandle(userRoleId);
		}catch (Exception e) {
			ChuanQiLog.error("上线邮件异常");
		}
	}
}
