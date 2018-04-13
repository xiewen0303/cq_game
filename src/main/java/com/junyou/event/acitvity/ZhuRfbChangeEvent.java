package com.junyou.event.acitvity;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 主活动变化事件
 * @author DaoZheng Yuan
 * 2015年5月20日 下午4:37:34
 */
public class ZhuRfbChangeEvent  extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	private int zhuId;
	
	public ZhuRfbChangeEvent(int zhuId) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		
		this.zhuId = zhuId;
	}

	
	public int getZhuId() {
		return zhuId;
	}
}
