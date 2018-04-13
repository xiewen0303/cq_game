package com.junyou.event.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.rfbactivity.configure.export.ReFaBuGxConfigExportService;
import com.junyou.event.acitvity.GxRfbChangeEvent;

/**
 * 热发布关系变化监听器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:01:07
 */
@Service
public class GxRfbChangeEventListener implements SmartApplicationListener {

	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		GxRfbChangeEvent zhuConfigEvent = (GxRfbChangeEvent)event;
		
		int activityId = zhuConfigEvent.getActivity();
		ReFaBuGxConfigExportService.getInstance().changeConfigureDataResolve(activityId);
	}

	@Override
	public int getOrder() {
		return 14;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是关系变化事件的子类
		return GxRfbChangeEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
