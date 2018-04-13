package com.junyou.event.acitvity;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 主活动删除事件
 * @author DaoZheng Yuan
 * 2015年5月20日 下午4:37:34
 */
public class ZhuRfbDelEvent  extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	private int zhuId;
	
	public ZhuRfbDelEvent(int zhuId) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		
		this.zhuId = zhuId;
	}

	
	public int getZhuId() {
		return zhuId;
	}
}
