package com.junyou.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.tuangou.export.TuanGouExportService;
import com.junyou.bus.xiaofei.export.RefabuXiaoFeiExportService;
import com.junyou.event.acitvity.RfbOverInitEvent;

/**
 * 启动服务器后热发布配置解析完毕后监听器
 * @description 
 * @author Hanchun
 * @email 279444454@qq.com
 * @date 2015-6-15 下午9:36:28
 */
@Service
public class RfbOverInitListener implements SmartApplicationListener {

	@Autowired
	private RefabuXiaoFeiExportService refabuXiaoFeiExportService;
	@Autowired
	private TuanGouExportService tuanGouExportService; 
	
	public void onApplicationEvent(ApplicationEvent event) { 
		//消费排名
		refabuXiaoFeiExportService.quartXiaoFei();
		//团购活动初始化
		tuanGouExportService.quartTuanGou();
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
		return RfbOverInitEvent.class.isAssignableFrom(event);
	}

	/**
	 * 如果实现支持“目标”类型，那么返回true
	 */
	public boolean supportsSourceType(Class<?> source) {
		return true;
	}
}
