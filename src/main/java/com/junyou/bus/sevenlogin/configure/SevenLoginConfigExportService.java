package com.junyou.bus.sevenlogin.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 七日登陆
 * @author jy
 *
 */
@Component
public class SevenLoginConfigExportService extends AbsClasspathConfigureParser{
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "QiRiDengLu.jat";
	
	private Map<Integer,SevenLoginConfig> sevenLoginConfigs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer,SevenLoginConfig> configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				SevenLoginConfig config = createSevenLoginConfig(tmp);
				configs.put(config.getId(), config);
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.sevenLoginConfigs = configs;
	}
	
	/**
	 * @param tmp
	 * @return
	 */
	public SevenLoginConfig createSevenLoginConfig(Map<String, Object> tmp) {
		SevenLoginConfig config = new SevenLoginConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		String itemsIdStr = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		config.setAwards(ConfigAnalysisUtils.getConfigMap(itemsIdStr));
		for(int i = 0 ; i < 4 ; i++){
			String itemsIdString = CovertObjectUtil.object2String(tmp.get("jiangitem" + (i + 1)));
			config.addJobAwards((byte)i, ConfigAnalysisUtils.getConfigMap(itemsIdString));
		}
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public SevenLoginConfig loadById(Integer id){
		return sevenLoginConfigs.get(id);
	}
	
}