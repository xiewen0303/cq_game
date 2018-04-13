package com.junyou.stage.model.skill.comsume;

import java.util.HashMap;
import java.util.Map;

import com.junyou.stage.model.core.skill.ISkillConsume;

/**
 * 
 * @description 消耗计算规则工厂
 *
 * @author LiuJuan
 * @created 2011-12-13 下午04:30:38
 */
public class ConsumeCalRuleFactory {
	
	private static Map<Integer,ISkillConsume> consumeCalRules = new HashMap<Integer, ISkillConsume>();
	
	static {
		consumeCalRules.put(SkillConsumes.CONSUME_TYPE_EMPTY, new ConsumeCalRuleEmpty());
	}
	
	/**
	 * 获取消耗计算规则
	 * @param SkillConsumes {@link SkillConsumes} 
	 */
	public static ISkillConsume getConsumeCalRule(Integer consumeType){
		
		ISkillConsume rule = consumeCalRules.get(consumeType);
		if(null == rule){
			rule = consumeCalRules.get(SkillConsumes.CONSUME_TYPE_EMPTY);
		}
		
		return rule;
	}

}
