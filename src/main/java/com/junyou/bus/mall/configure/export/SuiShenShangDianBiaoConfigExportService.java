package com.junyou.bus.mall.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 随身商店 
 *
 * @author HanChun
 * @date 2015-03-17 14:35:44
 */
@Component
public class SuiShenShangDianBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer, ReFaBuShangChengBiaoConfig> suishenConfig ;
	/**
	  * configFileName
	 */
	private String configureName = "SuiShenShangDianBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer, ReFaBuShangChengBiaoConfig> tmpSuishenconfig = new HashMap<>();
		if(data == null){
			return;
		}
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ReFaBuShangChengBiaoConfig config = createReFaBuShangChengBiaoConfig(tmp);
								
				tmpSuishenconfig.put(config.getId(), config);;
			}
		}
		this.suishenConfig = tmpSuishenconfig;
	}
	
	public ReFaBuShangChengBiaoConfig createReFaBuShangChengBiaoConfig(Map<String, Object> tmp) {
		ReFaBuShangChengBiaoConfig config = new ReFaBuShangChengBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setMaxlevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
											
		config.setPrice(CovertObjectUtil.object2int(tmp.get("price")));
											
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
											
		config.setSellid(CovertObjectUtil.obj2StrOrNull(tmp.get("sellid")));
											
		config.setModelid(CovertObjectUtil.object2int(tmp.get("modelid")));
											
		config.setDay(CovertObjectUtil.object2int(tmp.get("day")));
											
		config.setMoneytype(CovertObjectUtil.object2int(tmp.get("moneytype")));
											
		config.setMinlevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		
		int count = CovertObjectUtil.object2int(tmp.get("count")) == 0 ? 1 : CovertObjectUtil.object2int(tmp.get("count"));//配置的物品数量，如果为null就默认为1
		
		config.setCount(count);
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ReFaBuShangChengBiaoConfig loadById(int id){
		return suishenConfig.get(id);
	}
}