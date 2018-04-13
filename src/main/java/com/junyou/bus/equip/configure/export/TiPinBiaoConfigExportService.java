package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 装备提品配置
 * @author LiuYu
 * @date 2015-7-15 上午10:09:10
 */
@Component
public class TiPinBiaoConfigExportService extends AbsClasspathConfigureParser{
	
	private Map<String, TiPinBiaoConfig> configs;
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhuangBeiTiPin.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<String, TiPinBiaoConfig> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TiPinBiaoConfig config = createTiPinBiaoConfig(tmp);
								
				configs.put(getId(config.getType(), config.getRareLevel()), config);
				
			}
		}
		this.configs = configs;
	}
	
	public TiPinBiaoConfig createTiPinBiaoConfig(Map<String, Object> tmp) {
		TiPinBiaoConfig config = new TiPinBiaoConfig();	
							
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setRareLevel(CovertObjectUtil.object2int(tmp.get("rarelevel")));
		config.setMax(CovertObjectUtil.object2int(tmp.get("maxvalue")) + 1);
		config.setMin(CovertObjectUtil.object2int(tmp.get("minvalue")));
		config.setMaxXing(CovertObjectUtil.object2int(tmp.get("xingshu"))*CovertObjectUtil.object2int(tmp.get("maxxing")));
		config.setStarMax(CovertObjectUtil.object2int(tmp.get("maxxing")));
		config.setXingshu(CovertObjectUtil.object2int(tmp.get("xingshu")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("needyinliang")));
		config.setItemId(CovertObjectUtil.object2String(tmp.get("needitem")));
		config.setItemCount(CovertObjectUtil.object2int(tmp.get("neednum")));
		
		return config;
	}

	private String getId(int type,int rarelevel){
		return type + "-" + rarelevel;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public TiPinBiaoConfig loadById(Integer type,int rarelevel){
		return configs.get(getId(type, rarelevel));
	}
}