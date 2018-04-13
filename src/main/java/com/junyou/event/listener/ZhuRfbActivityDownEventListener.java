package com.junyou.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.constants.GameConstants;
import com.junyou.event.acitvity.HandleRfbActivityEvent;
import com.junyou.event.acitvity.ZhuRfbActivityEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.DownloadRemoteUtil;
import com.junyou.utils.json.JsonUtils;

/**
 * 主活动热发布活动监听器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:01:07
 */
@Service
public class ZhuRfbActivityDownEventListener implements SmartApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		ZhuRfbActivityEvent zhuConfigEvent = (ZhuRfbActivityEvent)event;
		Map<Integer, String> map = zhuConfigEvent.getZuIds();
		boolean isInit = zhuConfigEvent.isInit();
		
		if(map != null){
			Map<Integer,Integer> subMap = new HashMap<>();
			
			//1.下载主活动数据
			for (Map.Entry<Integer, String> entery : map.entrySet()) {
				byte[] data = DownloadRemoteUtil.download(ChuanQiConfigUtil.getLoadDirectoryUrl(), GameConstants.REFABU_DIR_NAME,entery.getValue());
				
				if(isInit){
					//初始化
					ActivityAnalysisManager.getInstance().analysisConfigureDataResolve(data,subMap);
				}else{
					//更改
					ActivityAnalysisManager.getInstance().changeConfigureDataResolve(data, entery.getKey(),subMap);
				}
			}
			
			//2.抛出一个事件，去下载实例的业务数据
			if(subMap.size() > 0){
				GamePublishEvent.publishEvent(new HandleRfbActivityEvent(subMap));
			}
		}
	}
	
	@Override
	public int getOrder() {
		return 8;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是下载主活动事件的子类
		return ZhuRfbActivityEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
