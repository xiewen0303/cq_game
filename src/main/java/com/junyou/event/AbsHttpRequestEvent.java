package com.junyou.event;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;

/**
 * HTTP请求基础类
 * @author LiuYu
 * @date 2015-5-9 下午1:19:14
 */
public abstract class AbsHttpRequestEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	
	public AbsHttpRequestEvent() {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
	}

	/**
	 * 获取http请求地址
	 * @return
	 */
	public abstract String getHttpUrl();
	
	/**
	 * 执行回调
	 * @param data
	 */
	public abstract void callBack(String data);

	
}
