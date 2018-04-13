package com.junyou.stage.configure.export.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 战斗力系数配置表 
 *
 * @author DaoZheng Yuan
 * @date 2013-10-28 10:54:27
 */
@Component
public class ZhuanDouLiCanShuConfigExportService{
	
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhanDouLiCanShu.jat";
	
	private static final String ZHAN_DOU_LI_ID = "ZHAN_DOU_LI_ID";
	
	private ZhuanDouLiCanShuConfig config = null;
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		
		if(dataList != null){
			ZhuanDouLiCanShuConfig config = new ZhuanDouLiCanShuConfig();	
			config.setId(ZHAN_DOU_LI_ID);
			
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>)obj;
				if (null != tmp) {
					String proName = CovertObjectUtil.object2String(tmp.get("proname"));
					EffectType.checkContains(proName);
					
					Float value = CovertObjectUtil.object2Float(tmp.get("value"));
					
					config.addAttrXs(proName, value);
				}
			}
			
			this.config = config;
		}
	}
	
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZhuanDouLiCanShuConfig load(){
		return config;
	}
}