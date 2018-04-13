package com.junyou.event.acitvity;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 热发布关系变化
 * @author DaoZheng Yuan
 * 2015年5月26日 下午5:56:43
 */
public class GxRfbChangeEvent  extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	private int activity;
	
	public GxRfbChangeEvent(int activity) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		
		this.activity = activity;
	}

	
	/**
	 * 获取主活动id,-1表示全部
	 * @return
	 */
	public int getActivity() {
		return activity;
	}
}
