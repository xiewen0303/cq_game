package com.junyou.bus.platform.qq.confiure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 腾讯折扣商店配置表 
 *
 * @author ZHONGDIAN
 * @date 2015-12-16 14:57:22
 */
@Component
public class LanZuanZheKouShopConfigService extends AbsClasspathConfigureParser {
	
	private Map<Integer,LanZuanZheKouShopConfig> configs =  new HashMap<Integer,LanZuanZheKouShopConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "LanZuanZheKouShop.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				LanZuanZheKouShopConfig config = createLanZuanZheKouShopConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public LanZuanZheKouShopConfig createLanZuanZheKouShopConfig(Map<String, Object> tmp) {
		LanZuanZheKouShopConfig config = new LanZuanZheKouShopConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setNeedgold(CovertObjectUtil.object2int(tmp.get("needgold")));
											
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
											
		config.setShowgold(CovertObjectUtil.object2int(tmp.get("showgold")));
											
		config.setItemid(CovertObjectUtil.object2String(tmp.get("itemid")));
											
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public LanZuanZheKouShopConfig loadById(Integer id){
		return configs.get(id);
	}
}