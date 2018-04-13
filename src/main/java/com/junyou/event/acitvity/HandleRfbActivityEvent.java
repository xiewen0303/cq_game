package com.junyou.event.acitvity;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * 下载热发布活动的实际业务事件（例如：首充等）
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:02:20
 */
public class HandleRfbActivityEvent extends ApplicationEvent{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 子活动id = 子活动类型
	 */
	private Map<Integer,Integer> handleMap;
	
	public HandleRfbActivityEvent(Map<Integer,Integer> handleMap) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		
		this.handleMap = handleMap;
	}
	
	/**
	 * 子活动id = 子活动类型
	 */
	public Map<Integer, Integer> getHandleMap() {
		return handleMap;
	}
}
