package com.junyou.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.huoyuedu.service.HuoYueDuService;
import com.junyou.event.huoyuedu.HuoyueduEvent;

/**
* 阵营排行
 */
@Service
public class HuoyueduEventListener implements SmartApplicationListener {

	@Autowired
	private  HuoYueDuService huoYueDuService;
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		HuoyueduEvent huoyueduEvent = (HuoyueduEvent)event;
		huoYueDuService.completeActivity(huoyueduEvent.getUserRoleId(),huoyueduEvent.getacEnum());
		 
		
	}

	@Override
	public int getOrder() {
		return 12;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是活动的子类
		return HuoyueduEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
