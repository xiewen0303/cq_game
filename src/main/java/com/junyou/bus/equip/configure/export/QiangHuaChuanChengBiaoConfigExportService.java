package com.junyou.bus.equip.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
 

@Component
public class QiangHuaChuanChengBiaoConfigExportService extends AbsClasspathConfigureParser{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "ZhuangBeiChuanCheng.jat";
	
	private Map<Integer,QiangHuaChuanChengBiaoConfig>	 qhConfigs = null;
	
	protected String getConfigureName() {
		return configureName;
	} 

//	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.error("ZhuangBeiChuanCheng.jat is null");
			return;
		}
		qhConfigs = new HashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				QiangHuaChuanChengBiaoConfig  config=createQiangHuaChuanChengBiaoConfig(tmp);
				qhConfigs.put(config.getId(), config);
			}
		}
	}
	
	private QiangHuaChuanChengBiaoConfig createQiangHuaChuanChengBiaoConfig(Map<String, Object> tmp){
		QiangHuaChuanChengBiaoConfig config=new QiangHuaChuanChengBiaoConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("money")));
		config.setSuccessrate(CovertObjectUtil.object2int(tmp.get("successrate")));
		return config;
	}
	
	public QiangHuaChuanChengBiaoConfig getConfig(){
		if(qhConfigs == null || qhConfigs.size() <= 0){
			return null;
		}
		for (QiangHuaChuanChengBiaoConfig config : qhConfigs.values()) {
			return config;
		}
		return null;
	}
}