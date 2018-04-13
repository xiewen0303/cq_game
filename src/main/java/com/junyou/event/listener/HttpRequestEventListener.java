package com.junyou.event.listener;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.event.AbsHttpRequestEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.DownloadPathUtil;

/**
 * HTTP请求事件监听器
 * @author LiuYu
 * @date 2015-5-9 下午1:18:09
 */
@Service
public class HttpRequestEventListener  implements SmartApplicationListener{

	/**
	 * 事件执行
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		
		AbsHttpRequestEvent httpEvent = (AbsHttpRequestEvent)event;
		URL url;
		try {
			url = new URL(httpEvent.getHttpUrl());
			byte[] data = DownloadPathUtil.download(url,10000);
			if(data == null){
				httpEvent.callBack(null);
				return;
			}
			InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(data));
			BufferedReader reader_ = new BufferedReader(reader);
			String sign = reader_.readLine();
			httpEvent.callBack(sign);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
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
		return AbsHttpRequestEvent.class.isAssignableFrom(event);
	}

	/**
	 * 如果实现支持“目标”类型，那么返回true
	 */
	public boolean supportsSourceType(Class<?> source) {
		return true;
	}
	
}
