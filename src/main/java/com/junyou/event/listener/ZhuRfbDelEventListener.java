package com.junyou.event.listener;

import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfig;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.event.acitvity.ZhuRfbDelEvent;

/**
 * 主活动删除监听器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:01:07
 */
@Service
public class ZhuRfbDelEventListener implements SmartApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		ZhuRfbDelEvent zhuConfigEvent = (ZhuRfbDelEvent)event;
		
		int zhuId = zhuConfigEvent.getZhuId();
		ActivityConfig config = ActivityAnalysisManager.getInstance().loadById(zhuId);
		if(config != null){
			//全服通知客户删除主活动
			BusMsgSender.send2All(ClientCmdType.RFB_ACTIVITY_DELETE, zhuId);
			//子活动配置标识删除
			Map<Integer, ActivityConfigSon> map = config.getZihuodongMap();
			if(map != null && map.size() > 0){
				for (ActivityConfigSon configSon : map.values()) {
					configSon.setDel(true);
				}
			}
			//删除主活动
			ActivityAnalysisManager.getInstance().deleteActivity(zhuId);
			
		}
	}

	@Override
	public int getOrder() {
		return 12;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是活动删除事件的子类
		return ZhuRfbDelEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
