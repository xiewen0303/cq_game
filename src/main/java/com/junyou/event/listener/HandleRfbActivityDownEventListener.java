package com.junyou.event.listener;

import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.bus.rfbactivity.configure.export.RfbConfigTemplateFacotry;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.constants.GameConstants;
import com.junyou.event.acitvity.HandleRfbActivityEvent;
import com.junyou.event.acitvity.RfbOverInitEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.DownloadRemoteUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * 下载热发布活动的实际业务监听器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:01:07
 */
@Service
public class HandleRfbActivityDownEventListener implements SmartApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		HandleRfbActivityEvent handleRfbEvent = (HandleRfbActivityEvent)event;
		Map<Integer, Integer> map = handleRfbEvent.getHandleMap();
		
		if(map != null){
			
			for (Map.Entry<Integer, Integer> entery : map.entrySet()) {
				String preFixName = ReFaBuUtil.getRfbSubPrefixName(entery.getValue());
				if(preFixName == null){
					ChuanQiLog.errorConfig("rfb handle config fail subId:{} subType{}",entery.getKey(),entery.getValue());
					//根据子活动类型无法获取到前缀名称，就跳过
					continue;
				}
				
				StringBuffer reFileName = new StringBuffer();
				reFileName.append(preFixName).append("_").append(entery.getKey()).append(GameConstants.RFB_SUFFIX);

				//1.下载实际业务活动数据
				byte[] data = DownloadRemoteUtil.download(ChuanQiConfigUtil.getLoadDirectoryUrl(), GameConstants.REFABU_DIR_HANLE_NAME,reFileName.toString());
				//2.完成业务配置解析
				annalysisDataModule(entery.getKey(), entery.getValue(), data);
			}
			
			ChuanQiLog.debug("================热发布流程已完成！");
			//热发布配置解析完毕后抛出事件
			GamePublishEvent.publishEvent(new RfbOverInitEvent());
		}
	}
	
	/**
	 * 解析模板
	 * @param subId  子活动Id(gm后台自增)
	 * @param type   子活动类型
	 * @param data
	 */
	private void annalysisDataModule(int subId,int type,byte[] data){
		//获取解析模板
		IRfbConfigTemplateService rfbConfigTemplateService = RfbConfigTemplateFacotry.getRfbConfigTemplateService(type);
		if(rfbConfigTemplateService != null){
			//分配给指定模板去解析
			rfbConfigTemplateService.analysisConfigureDataResolve(subId, data);
		}
	}
	

	@Override
	public int getOrder() {
		return 9;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> event) {
		//判断事件的类型是否是下载热发布活动的实际业务事件的子类
		return HandleRfbActivityEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
