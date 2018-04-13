package com.junyou.bus.share.ruleinfo;

import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.bus.share.service.RoleStateService;
import com.kernel.pool.executor.IRuleInfoCheck;

public class BusRuleInfoCheck implements IRuleInfoCheck {

	@Autowired
	private RoleStateService roleStateService;
	
	@Override
	public Boolean valid(Object ruleinfo) {
		return !roleStateService.isOnline((Long) ruleinfo);
	}

}
