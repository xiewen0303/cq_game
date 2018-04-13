package com.junyou.event.publish;

import org.springframework.context.ApplicationEvent;

import com.kernel.spring.SpringApplicationContext;

/**
 * 触发事件类
 * @author DaoZheng Yuan
 * 2014年11月28日 下午6:05:19
 */
public class GamePublishEvent {

	/**
	 * 触发事件  
	 * @param event
	 */
	public static void publishEvent(ApplicationEvent event){
		SpringApplicationContext.getApplicationContext().publishEvent(event);
	}
}
