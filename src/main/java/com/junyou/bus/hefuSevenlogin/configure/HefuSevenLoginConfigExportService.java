package com.junyou.bus.hefuSevenlogin.configure;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 合服七日登陆
 * @author 
 *
 */
@Component
public class HefuSevenLoginConfigExportService extends AbsClasspathConfigureParser {
	/**
	  * configFileName
	 */
	private String configureName = "HeFuQiDeng.jat";
	
	private Map<Integer,HefuSevenLoginConfig> hefuSevenLoginConfigs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer,HefuSevenLoginConfig> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				HefuSevenLoginConfig config = createSevenLoginConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.hefuSevenLoginConfigs = configs;
	}
	/**
	 * @param tmp
	 * @return
	 */
	public HefuSevenLoginConfig createSevenLoginConfig(Map<String, Object> tmp) {
		HefuSevenLoginConfig config = new HefuSevenLoginConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		String item1 = CovertObjectUtil.object2String(tmp.get("item1"));
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item1);
		Map<String, Integer> item  = new HashMap<>();
		for (Entry<String, Integer> element : itemMap.entrySet()) {
			String key = element.getKey();
			if(key.equals(ModulePropIdConstant.EXP_GOODS_ID)){
				config.setExp(CovertObjectUtil.obj2long(itemMap.get(key)));
			}else if(key.equals(ModulePropIdConstant.MONEY_GOODS_ID)) {
				config.setMoney(CovertObjectUtil.obj2long(itemMap.get(key)));
			}else{
				item.put(key, element.getValue());
			}
		}
		config.setItems(item);
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public HefuSevenLoginConfig loadById(Integer id){
		return hefuSevenLoginConfigs.get(id);
	}
	
}