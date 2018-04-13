package com.junyou.event.huoyuedu;

/**
 * 活跃度系统事件
 */
import org.springframework.context.ApplicationEvent;

import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.constants.GameConstants;

public class HuoyueduEvent extends ApplicationEvent{
	private static final long serialVersionUID = 1L;
	
	private ActivityEnum aEnum;
	private Long userRoleId;
	public HuoyueduEvent(Long userRoleId,ActivityEnum aEnum) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		this.aEnum  = aEnum; 
		this.userRoleId = userRoleId;
	}
	/**
	 * 用来区分具体是哪个活动
	 * @return
	 */
	public ActivityEnum getacEnum() {
		return aEnum;
	}
	public Long getUserRoleId() {
		return userRoleId;
	}
}
