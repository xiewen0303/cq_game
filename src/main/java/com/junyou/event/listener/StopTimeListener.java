package com.junyou.event.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.event.StopTimeEvent;

/**
 * 停机次数监听器
 * @author DaoZheng Yuan
 * 2014年12月1日 下午3:05:01
 */
@Service
public class StopTimeListener implements SmartApplicationListener {

	/**
	 * 事件执行
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		//更新服务器的停机次数数据
		ServerInfoServiceManager.getInstance().updateStopTimes();
	}


	/**
	 * 顺序，即监听器执行的顺序，值越小优先级越高
	 */
	public int getOrder() {
		return 1;
	}

	/**
	 * 如果实现支持该事件类型 那么返回true 
	 */
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是停机次数事件的子类
		return StopTimeEvent.class.isAssignableFrom(event);
	}

	/**
	 * 如果实现支持“目标”类型，那么返回true
	 */
	public boolean supportsSourceType(Class<?> source) {
		return true;
	}

}
