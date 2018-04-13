package com.junyou.bus.branchtask.configure;

import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

public class ZhiXianOpen implements IOpenCondition {
	
	private int needId;
	
	
	public ZhiXianOpen(int needId) {
		super();
		this.needId = needId;
	}


	@Override
	public boolean isOpen(Object params) {
		if(params == null){
			ChuanQiLog.error("ZhiXianOpen params is null");
			return false;
		}
		
		Object[] ids = (Object[])params;
		for (Object id : ids) {
			if(CovertObjectUtil.object2int(id) == needId){
				return true;
			}
		}
		
		return false;
	}


	@Override
	public boolean isType(int conditionType) {
		return conditionType == ConditionType.ZHIXIAN_RENWU;
	}
}
