package com.junyou.bus.kuafuluandou.configure;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 跨服大乱斗奖励表 
 *
 * @author ZHONGDIAN
 * @date 2016-02-17 15:16:10
 */
@Component
public class DaLuanDouJiangLiBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer, DaLuanDouJiangLiBiaoConfig> configs = new HashMap<>();;
	
	/**
	  * configFileName
	 */
	private String configureName = "DaLuanDouJiangLiBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DaLuanDouJiangLiBiaoConfig config = createDaLuanDouJiangLiBiaoConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public DaLuanDouJiangLiBiaoConfig createDaLuanDouJiangLiBiaoConfig(Map<String, Object> tmp) {
		DaLuanDouJiangLiBiaoConfig config = new DaLuanDouJiangLiBiaoConfig();	
							
		config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
											
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setMax(CovertObjectUtil.object2int(tmp.get("max")));
											
		config.setJiangitem(CovertObjectUtil.object2int(tmp.get("jiangitem")));
											
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public DaLuanDouJiangLiBiaoConfig loadById(Integer id){
		return configs.get(id);
	}
	
	/**
	 * 根据名次获取配置的奖励
	 * @return
	 */
	public DaLuanDouJiangLiBiaoConfig getJiangItemByMingCi(Integer mingci){
		if(configs == null || configs.size() <= 0){
			return null;
		}
		for (Entry<Integer, DaLuanDouJiangLiBiaoConfig> entry: configs.entrySet()) {
			DaLuanDouJiangLiBiaoConfig config = entry.getValue();
			if(config.getType() == 1){
				continue;
			}
			if(mingci>= config.getMin() && mingci <= config.getMax()){
				return config;
			}
		
		}
		return null;
	}
}