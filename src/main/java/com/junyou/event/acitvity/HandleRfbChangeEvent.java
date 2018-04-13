package com.junyou.event.acitvity;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 热发布活动的实际业务修改事件（例如：首充等）
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:02:20
 */
public class HandleRfbChangeEvent extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	//主活动Id
	private int mainId;
	//子活动ID
	private int subId;
	
	public HandleRfbChangeEvent(int subId, int mainId) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		
		this.subId = subId;
		this.mainId = mainId;
	}

	
	public int getSubId() {
		return subId;
	}


	public int getMainId() {
		return mainId;
	}
}
