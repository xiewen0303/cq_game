package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 腾讯用户来源日志
 * @author zhongdian
 * @date 2015-3-30 下午5:17:30
 */
public class TencentAppCustomLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public TencentAppCustomLogEvent(String userId, String appCustom) {
		super(LogPrintHandle.TENCENT_APP_CUSTOM);
		this.userId = userId;
		this.appCustom = appCustom;
	}

	private String userId;
	private String appCustom;

	public String getUserId() {
		return userId;
	}
	public String getAppCustom() {
		return appCustom;
	}

}