package com.junyou.bus.platform.qq.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqShuJiaPublicConfig;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.qq.json.JSONException;
import com.junyou.bus.platform.qq.json.JSONObject;
import com.junyou.bus.platform.qq.utils.QqUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.spring.container.DataContainer;

/**
 * @author zhongdian
 * 2015-8-4 上午11:54:13
 */
@Service
public class QQShuJiaHuoDongService {

	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService platformGongGongShuJuBiaoConfigExportService;
	/**
	 * 执行腾讯暑假活动调用
	 */
	public void sendPlayzoneTask(Long userRoleId,String task){
		RoleWrapper role = null;
		if("up".equals(task)){
			role = roleExportService.getUserRoleFromDb(userRoleId);
		}else{
			role = roleExportService.getLoginRole(userRoleId);
		}
		if(role == null){
			return;
		}
		Map<String, String> keyMap = dataContainer.getData(QqConstants.COMPONENET_NAME, role.getUserId());	
		if(keyMap == null){
			return;
		}
		//只有平台是这个的才发
		String appCustom = keyMap.get("appCustom");
		if(appCustom == null || !QqConstants.SHUJIA_APPCOSTOM.equals(appCustom)){
			return;	
		}
		QqShuJiaPublicConfig publicConfig = platformGongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.QQ_SHUJIA);
		if(publicConfig == null){
			return;
		}
		String taskId = publicConfig.getRenwuMap().get(task);
		//发送请求 通知腾讯暑假任务完成
		HashMap<String, String> hParams = new HashMap<String, String>();
		hParams.put("openid", role.getUserId());
		hParams.put("openkey",keyMap.get("openkey"));
		hParams.put("appid", QqConstants.APP_ID+"");
		hParams.put("pf", keyMap.get("pf"));
		hParams.put("format", QqConstants.FORMAT);
		hParams.put("userip", role.getLastLoginIp());
		hParams.put("task_id", taskId);
		hParams.put("source_id", "10000454");
		hParams.put("cmd", "1");
		
		String hScriptName = "/v3/user/set_playzone_task";
		String hProtocol = "http";
		
		String resp = QqUtil.api(hScriptName, hParams, hProtocol, QqConstants.APP_ID, QqConstants.APP_KEY, ChuanQiConfigUtil.getTencentUrl());
		JSONObject jo = null;
		try {
			jo = new JSONObject(resp);
		} catch (JSONException e) {
			ChuanQiLog.error("api return json error",e);
		}
	    // 检测ret值
	    int rc = jo.optInt("code", 0);
	    //失败的情况下 重新请求一次
	    if(rc != 0 && rc != 2006){
	    	QqUtil.api(hScriptName, hParams, hProtocol, QqConstants.APP_ID, QqConstants.APP_KEY, ChuanQiConfigUtil.getTencentUrl());
	    }
	}
	
}
