package com.junyou.bus.platform.baidu.confiure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 任务集市配置表 
 *
 * @author ZHONGDIAN
 * @date 2015-07-20 15:57:49
 */
@Component
public class BaiduRenWuJiShiConfigExportService extends AbsClasspathConfigureParser{
	

	private Map<Integer,BaiduRenWuJiShiConfig> configs =  new HashMap<Integer,BaiduRenWuJiShiConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "RenWuJiShi-BaiDu.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				BaiduRenWuJiShiConfig config = createRenWuJiShiConfig(tmp);
								
				configs.put(config.getConfigid(), config);
			}
		}
	}
	
	public BaiduRenWuJiShiConfig createRenWuJiShiConfig(Map<String, Object> tmp) {
		BaiduRenWuJiShiConfig config = new BaiduRenWuJiShiConfig();	
							
		config.setId(CovertObjectUtil.obj2StrOrNull(tmp.get("id")));
											
		config.setJiangli(CovertObjectUtil.obj2StrOrNull(tmp.get("jiangli")));
											
		config.setRenwu(CovertObjectUtil.obj2StrOrNull(tmp.get("renwu")));
											
		config.setJiangshow(CovertObjectUtil.obj2StrOrNull(tmp.get("jiangshow")));
											
		config.setConfigid(CovertObjectUtil.object2int(tmp.get("configid")));
											
		config.setCishu(CovertObjectUtil.object2int(tmp.get("cishu")));
											
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
											
		config.setBuzhou(CovertObjectUtil.object2int(tmp.get("buzhou")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public BaiduRenWuJiShiConfig loadById(String id){
		return configs.get(id);
	}
	
	public BaiduRenWuJiShiConfig getRenWuByIdAndBu(final String id,final int buzhou) {

		for (Integer key : configs.keySet()) {
			BaiduRenWuJiShiConfig config = configs.get(key);
			if(config.getId().equals(id) && config.getBuzhou().intValue() == buzhou){
				return config;
			}
		}
		return null;
	}
	
}