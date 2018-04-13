package com.junyou.event.acitvity;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 子活动标记删除事件
 * @author DaoZheng Yuan
 * 2015年5月20日 下午4:37:34
 */
public class ZhiRfbDelEvent  extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	private int zhiId;
	
	public ZhiRfbDelEvent(int zhiId) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		
		this.zhiId = zhiId;
	}

	/**
	 * 获取子活动ID
	 * @return
	 */
	public int getZhiId() {
		return zhiId;
	}
}
