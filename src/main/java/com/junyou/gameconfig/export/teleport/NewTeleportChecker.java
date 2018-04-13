package com.junyou.gameconfig.export.teleport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewTeleportChecker extends TeleportChecker{
	
	//必要条件
	private List<ICheckRule> checkRules = new ArrayList<ICheckRule>();
	//分组条件
	private Map<Integer,List<ICheckRule>> checkGroupRules = new HashMap<Integer, List<ICheckRule>>();
	

	public boolean check(ICheckCallback callback) {
		
		if(checkRules.size() > 0){
			//检测必要条件
			for(ICheckRule checkRule : checkRules){
				if(!checkRule.check(callback)) return false;
			}
		}
		List<ICheckRule> handles = new ArrayList<ICheckRule>();//用于处理回调方法
		if(checkGroupRules.size() > 0){
			//检测分组条件
			boolean pass = false;
			for (List<ICheckRule> list : checkGroupRules.values()) {
				for (ICheckRule iCheckRule : list) {
					pass = iCheckRule.check(callback);
					if(pass){
						handles.add(iCheckRule);
						break;
					}
				}
				if(!pass)return false;
			}
		}
		callback.success();
		
		//处理回调
		for(ICheckRule checkRule : checkRules){
			checkRule.handle(callback);
		}
		for(ICheckRule checkRule : handles){
			checkRule.handle(callback);
		}
		return true;
		
	}

	/**
	 * 增加必要规则
	 * @param checkRule
	 */
	public void addRule(ICheckRule checkRule) {
		
		checkRules.add(checkRule);
	
	}
	/**
	 * 增加分组规则
	 * @param checkRule
	 */
	public void addRule(ICheckRule checkRule,Integer group) {
		
		List<ICheckRule> checkRules = checkGroupRules.get(group);
		if(checkRules == null){
			checkRules = new ArrayList<ICheckRule>();
			checkGroupRules.put(group, checkRules);
		}
		checkRules.add(checkRule);
	
	}
	
	public Integer ruleSize(){
		return checkRules.size() + checkGroupRules.size();
	}

}
