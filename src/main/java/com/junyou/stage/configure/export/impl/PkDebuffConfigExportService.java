package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description
 *
 * @author LiuJuan
 * @created 2011-12-12 下午03:48:45
 */
@Service
public class PkDebuffConfigExportService extends AbsClasspathConfigureParser {

	
	/**
	 * 配置名
	 */
	private String configureName = "Pkzhijichubiao.jat";
	private Map<Integer,PkDebuffConfig> configs;
	private List<Integer> values;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<Integer,PkDebuffConfig> configs = new HashMap<>();
		List<Integer> values = new ArrayList<>();
		Object[] listData = GameConfigUtil.getResource(data);
		for (Object obj:listData) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				PkDebuffConfig config = createPkDebuffConfig(tmp);
				values.add(config.getMin());
				configs.put(config.getMin(), config);
			}
		}
		Collections.sort(values);
		this.configs = configs;
		this.values = values;
	}
	
	private PkDebuffConfig createPkDebuffConfig(Map<String, Object> tmp) {
		PkDebuffConfig config = new PkDebuffConfig();
		config.setMin(CovertObjectUtil.object2int(tmp.get("min")));
		Map<String,Long> att = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(att);
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}


	public PkDebuffConfig loadByPk(Integer value) {
		Integer id = null;
		for (Integer pk : values) {
			if(pk <= value){
				id = pk;
			}else{
				break;
			}
		}
		if(id != null){
			return configs.get(id);
		}
		return null;
	}

}
