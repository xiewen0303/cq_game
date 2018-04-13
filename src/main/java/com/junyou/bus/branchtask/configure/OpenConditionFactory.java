package com.junyou.bus.branchtask.configure;

import com.junyou.utils.common.CovertObjectUtil;

public class OpenConditionFactory {
	
	public static IOpenCondition createOpenCondition(int conditionType,Object conditionParams){
		
		switch (conditionType) {
		case ConditionType.HERO_LEVEL:
			return new LevelOpen(CovertObjectUtil.object2int(conditionParams));
		
		case ConditionType.ZHIXIAN_RENWU:
			return new ZhiXianOpen(CovertObjectUtil.object2int(conditionParams));

		default:
			break;
		}
		return null;
	}
}
