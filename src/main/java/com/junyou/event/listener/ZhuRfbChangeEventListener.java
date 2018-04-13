package com.junyou.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.constants.GameConstants;
import com.junyou.event.acitvity.HandleRfbActivityEvent;
import com.junyou.event.acitvity.ZhuRfbChangeEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.DownloadRemoteUtil;

/**
 * 主活动变化监听器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:01:07
 */
@Service
public class ZhuRfbChangeEventListener implements SmartApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		ZhuRfbChangeEvent zhuConfigEvent = (ZhuRfbChangeEvent)event;
		
		int zhuId = zhuConfigEvent.getZhuId();
		StringBuffer zhuFileName = new StringBuffer();
		zhuFileName.append(GameConstants.ZHU_RFB_PREFIX).append(zhuId).append(GameConstants.RFB_SUFFIX);
		
		byte[] data = DownloadRemoteUtil.download(ChuanQiConfigUtil.getLoadDirectoryUrl(), GameConstants.REFABU_DIR_NAME,zhuFileName.toString());
		
		Map<Integer,Integer> subMap = new HashMap<>();
		ActivityAnalysisManager.getInstance().changeConfigureDataResolve(data, zhuId,subMap);
		//2.抛出一个事件，去下载实例的业务数据
		if(subMap.size() > 0){
			GamePublishEvent.publishEvent(new HandleRfbActivityEvent(subMap));
		}
	}

	@Override
	public int getOrder() {
		return 12;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是活动变化事件的子类
		return ZhuRfbChangeEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
