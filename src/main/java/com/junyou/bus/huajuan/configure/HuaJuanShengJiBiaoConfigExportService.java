package com.junyou.bus.huajuan.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 画卷升级表 
 *
 * @author ZHONGDIAN
 * @date 2016-03-08 14:55:20
 */
@Component
public class HuaJuanShengJiBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private Map<Integer, HuaJuanShengJiBiaoConfig> configs =  new HashMap<Integer, HuaJuanShengJiBiaoConfig>();
	
	/**
	  * configFileName
	 */
	private String configureName = "HuaJuanShengJiBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				HuaJuanShengJiBiaoConfig config = createHuaJuanShengJiBiaoConfig(tmp);
								
				configs.put(config.getId(), config);
			}
		}
	}
	
	public HuaJuanShengJiBiaoConfig createHuaJuanShengJiBiaoConfig(Map<String, Object> tmp) {
		HuaJuanShengJiBiaoConfig config = new HuaJuanShengJiBiaoConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("level")));
											
		config.setNeedexp(CovertObjectUtil.object2int(tmp.get("needexp")));
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
	
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public HuaJuanShengJiBiaoConfig loadById(Integer id){
		return configs.get(id);
	}
}