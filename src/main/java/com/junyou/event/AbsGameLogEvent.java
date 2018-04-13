package com.junyou.event;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 日志事件基础类
 * @author DaoZheng Yuan
 * 2014年12月1日 下午2:55:13
 */
public class AbsGameLogEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	private int type;
	
	public AbsGameLogEvent(int type) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		this.type = type;
	}

	/**
	 * 日志大类类型
	 * @return
	 */
	public int getType() {
		return type;
	}

	
}
