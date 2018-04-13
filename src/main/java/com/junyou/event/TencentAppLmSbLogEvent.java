package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 腾讯联盟上报失败日志
 * @author zhongdian
 * @date 2015-3-30 下午5:17:30
 */
public class TencentAppLmSbLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;  

	public TencentAppLmSbLogEvent(String openId, String pfkey,String pf) {
		super(LogPrintHandle.TENCENT_APP_SBSB);
		this.openId = openId;
		this.pfkey = pfkey;
		this.pf = pf;
	}

	private String openId;
	private String pfkey;
	private String pf;

	public String getOpenId() {
		return openId;
	}
	public String getPfkey() {
		return pfkey;
	}
	public String getPf() {
		return pf;
	}

	
}