package com.junyou.bus.rfbactivity.configure.export;

import com.junyou.log.ChuanQiLog;

public abstract class AbstractRfbConfigService implements IRfbConfigTemplateService {

	@Override
	public Object getChildData(int subId) {
		
		ChuanQiLog.debug("活动没有实现 getChildData,请检查，guid:"+this.getClass());
		return null;
	}
	
}
