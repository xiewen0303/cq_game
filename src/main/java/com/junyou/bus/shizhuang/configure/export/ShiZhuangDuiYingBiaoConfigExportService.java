package com.junyou.bus.shizhuang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 转职时装转换表 
 *
 * @author ZHONGDIAN
 * @date 2016-03-14 17:23:39
 */
@Component
public class ShiZhuangDuiYingBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer,ShiZhuangDuiYingBiaoConfig> configMap = new HashMap<Integer, ShiZhuangDuiYingBiaoConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "ShiZhuangDuiYingBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShiZhuangDuiYingBiaoConfig config = createShiZhuangDuiYingBiaoConfig(tmp);
								
				configMap.put(config.getId(), config);
			}
		}
	}
	
	public ShiZhuangDuiYingBiaoConfig createShiZhuangDuiYingBiaoConfig(Map<String, Object> tmp) {
		ShiZhuangDuiYingBiaoConfig config = new ShiZhuangDuiYingBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setId1(CovertObjectUtil.object2int(tmp.get("id1")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ShiZhuangDuiYingBiaoConfig loadById(Integer id){
		return configMap.get(id);
	}
}