package com.junyou.event;

import com.junyou.log.LogPrintHandle;

/**
 * 坐骑升阶
 * 
 * @author wind
 * 
 */
public class ZhuanShengDuiHuanLogEvent extends AbsGameLogEvent {
 

	private static final long serialVersionUID = 1L;
	
	private Long userRoleId;
	private String name;
	private Long exp;//消耗经验值
	private Long xiuwei;//获得修为值
	
	
	
	public ZhuanShengDuiHuanLogEvent(Long userRoleId, String name,Long exp,Long xiuwei) {
		super(LogPrintHandle.ZHUANSHENG_DUIHUAN);
		this.userRoleId = userRoleId;
		this.name = name;
		this.exp = exp;
		this.xiuwei = xiuwei;
	}
	
	public Long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getName() {
		return name;
	}

	public Long getExp() {
		return exp;
	}

	public Long getXiuwei() {
		return xiuwei;
	}
	
}