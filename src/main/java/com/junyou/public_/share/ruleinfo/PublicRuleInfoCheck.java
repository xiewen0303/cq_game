package com.junyou.public_.share.ruleinfo;

import org.springframework.beans.factory.annotation.Autowired;

import com.hehj.easyexecutor.cmd.CmdGroupInfo;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.kernel.pool.executor.IRuleInfoCheck;

public class PublicRuleInfoCheck implements IRuleInfoCheck {

	private String public_group = "public";
	
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	
	@Override
	public Boolean valid(Object ruleinfo) {
	
		if(CmdGroupInfo.isGroup((String)ruleinfo, public_group)){
			return false;
		}
		
		if(publicRoleStateService.isPublicOnline((Long)ruleinfo)){
			return false;
		}
		
		return true;
	}

}
