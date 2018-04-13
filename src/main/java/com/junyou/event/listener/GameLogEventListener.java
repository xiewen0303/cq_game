package com.junyou.event.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.junyou.event.AbsGameLogEvent;
import com.junyou.log.LogPrintHandle;

/**
 * 游戏日志事件监听器
 * @author DaoZheng Yuan
 * 2014年11月28日 下午4:32:38
 */
@Service
public class GameLogEventListener  implements SmartApplicationListener{

	/**
	 * 事件执行
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		AbsGameLogEvent logEvent = (AbsGameLogEvent)event;
		String logMsg = JSON.toJSONString(logEvent);
		LogPrintHandle.printLog(logEvent.getType(), logMsg);
	}

	/**
	 * 顺序，即监听器执行的顺序，值越小优先级越高
	 */
	public int getOrder() {
		return 10;
	}

	/**
	 * 如果实现支持该事件类型 那么返回true 
	 */
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是游戏日志事件的子类
		return AbsGameLogEvent.class.isAssignableFrom(event);
	}

	/**
	 * 如果实现支持“目标”类型，那么返回true
	 */
	public boolean supportsSourceType(Class<?> source) {
		return true;
	}
	
}
