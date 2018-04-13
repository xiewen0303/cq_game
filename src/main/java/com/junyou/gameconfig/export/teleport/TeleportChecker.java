package com.junyou.gameconfig.export.teleport;

import java.util.ArrayList;
import java.util.List;


public class TeleportChecker {
	
	private List<ICheckRule> checkRules = null;

	public boolean check(ICheckCallback callback) {
		
		if(null != checkRules){
			for(ICheckRule checkRule : checkRules){
				boolean pass = checkRule.check(callback);
				if(!pass) return pass;
			}
			
			for(ICheckRule checkRule : checkRules){
				checkRule.handle(callback);
			}
		}
		
		return true;
		
	}

	/**
	 * 增加规则
	 * @param checkRule
	 */
	public void addRule(ICheckRule checkRule) {
		
		if(null == checkRules)
			checkRules = new ArrayList<ICheckRule>();
		
		checkRules.add(checkRule);
	
	}
	
	public Integer ruleSize(){
		return (null != checkRules) ? checkRules.size() : 0;
	}

}
