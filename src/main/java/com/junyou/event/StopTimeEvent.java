package com.junyou.event;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 停机次数事件
 * @author DaoZheng Yuan
 * 2014年12月1日 下午7:50:04
 */
public class StopTimeEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private byte type;
	
	public StopTimeEvent(byte type) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		this.type = type;
	}

	/**
	 * 主键类型
	 * @return
	 */
	public byte getType() {
		return type;
	}
}
