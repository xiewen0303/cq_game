package com.junyou.event.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.bus.rfbactivity.configure.export.RfbConfigTemplateFacotry;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.constants.GameConstants;
import com.junyou.event.acitvity.HandleRfbChangeEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.DownloadRemoteUtil;

/**
 * 热发布活动的实际业务修改监听器
 * @author DaoZheng Yuan
 * 2015年5月18日 下午10:01:07
 */
@Service
public class HandleRfbChangeEventListener implements SmartApplicationListener {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		HandleRfbChangeEvent handleRfbEvent = (HandleRfbChangeEvent)event;
		Integer subId = handleRfbEvent.getSubId();
		
		if(subId != null){
			ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			if(configSon == null){
				ChuanQiLog.error("修改热发布子活动错误："+subId);
				return;
			}
			
			String preFixName = ReFaBuUtil.getRfbSubPrefixName(configSon.getSubActivityType());
			if(preFixName == null){
				ChuanQiLog.errorConfig("rfb handle change config fail subId:{} subType{}",subId,configSon.getSubActivityType());
				//根据子活动类型无法获取到前缀名称，就跳过
				return;
			}
			
			StringBuffer reFileName = new StringBuffer();
			reFileName.append(preFixName).append("_").append(subId).append(GameConstants.RFB_SUFFIX);
			ChuanQiLog.error(reFileName.toString());
			
			//1.下载实际业务活动数据
			byte[] data = DownloadRemoteUtil.download(ChuanQiConfigUtil.getLoadDirectoryUrl(), GameConstants.REFABU_DIR_HANLE_NAME,reFileName.toString());
			//2.完成业务配置解析
			annalysisDataModule(subId, configSon.getSubActivityType(), data);
			
			/*
			 * 处理子活动配置里的非excel数据（后台配置的子活动名称时间等信息）
			 */
			//1.下载
			int zhuId = handleRfbEvent.getMainId();
			StringBuffer zhuFileName = new StringBuffer();
			zhuFileName.append(GameConstants.ZHU_RFB_PREFIX).append(zhuId).append(GameConstants.RFB_SUFFIX);
			byte[] activityData = DownloadRemoteUtil.download(ChuanQiConfigUtil.getLoadDirectoryUrl(), GameConstants.REFABU_DIR_NAME,zhuFileName.toString());
			
			//2.解析
			ActivityAnalysisManager.getInstance().chanageConfigureDataSon(activityData, handleRfbEvent.getMainId(), handleRfbEvent.getSubId());
		}
	}
	
	
	/**
	 * 解析模板
	 * @param subId
	 * @param subName
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
		return HandleRfbChangeEvent.class.isAssignableFrom(event);
	}

	@Override
	public boolean supportsSourceType(Class<?> event) {
		return true;
	}
}
