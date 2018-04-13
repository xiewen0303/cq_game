package com.junyou.bus.equip.configure.export;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
 

/**
 * 道具类型排序表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:43:38
  *@Description:
 */
@Component
public class ZhuangBeiQiangHuaJiaChengExportService extends AbsClasspathConfigureParser{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "ZhuangBeiQiangHuaJiaChengBiao.jat"; 
 
	
	private Map<Integer,Map<String,Long>> colorJCConfigs = null;
	
	private Map<Integer,Map<String,Long>> qhJCConfigs = null;
	
	private List<ZhuangBeiQiangHuaJiaChengConfig> tempConfigs = new ArrayList<>();
	
	protected String getConfigureName() {
		return configureName;
	} 

	@Override
	protected void configureDataResolve(byte[] data) {
		
		colorJCConfigs = new HashMap<>();
		qhJCConfigs = new HashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhuangBeiQiangHuaJiaChengConfig  config = createQiangHuaConfig(tmp);
				tempConfigs.add(config); 
			}
		}
		 
		tempConfigs=null;
	}
	
	public Map<String,Long> getAttrsByColor(int color){
		return colorJCConfigs.get(color);
	}
	
	public Map<String,Long> getqhJCAttrs(int qhLevel){
		return qhJCConfigs.get(qhLevel);
	}
	
	private ZhuangBeiQiangHuaJiaChengConfig createQiangHuaConfig(Map<String, Object> tmp){
		ZhuangBeiQiangHuaJiaChengConfig config = new ZhuangBeiQiangHuaJiaChengConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("ID")));
		config.setLv(CovertObjectUtil.object2int(tmp.get("lv")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		
		Map<String,Long> resultAttrs = new HashMap<>();
		ObjectUtil.longMapAdd(resultAttrs,attrs);
		
		for (ZhuangBeiQiangHuaJiaChengConfig tConfig : tempConfigs) {
			if(tConfig.getType() == config.getType()) {
				ObjectUtil.longMapAdd(resultAttrs, tConfig.getAttrs());
			}
		}
		
		if(config.getType() == 0) { 
			colorJCConfigs.put(config.getLv(),resultAttrs);
		} else if(config.getType() == 1){
			qhJCConfigs.put(config.getLv(),resultAttrs);
		}
		
		return config; 
	}
}