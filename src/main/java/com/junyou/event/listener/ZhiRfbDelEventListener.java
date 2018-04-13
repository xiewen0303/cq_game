package com.junyou.event.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.event.acitvity.ZhiRfbDelEvent;

/**
 * 子活动标记删除监听器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:01:07
 */
@Service
public class ZhiRfbDelEventListener implements SmartApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		ZhiRfbDelEvent zhuConfigEvent = (ZhiRfbDelEvent)event;
		
		int zhuId = zhuConfigEvent.getZhiId();
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(zhuId);
		if(configSon != null){
			//标记子活动删除
			configSon.setDel(true);
		}
	}

	@Override
	public int getOrder() {
		return 12;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是活动标记删除事件的子类
		return ZhiRfbDelEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
