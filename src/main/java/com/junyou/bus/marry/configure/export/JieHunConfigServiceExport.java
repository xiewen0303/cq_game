package com.junyou.bus.marry.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.marry.entity.JieHunConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-8-10 下午10:42:26
 */
@Service
public class JieHunConfigServiceExport extends AbsClasspathConfigureParser {
	private Map<Integer,JieHunConfig> configs;
	/**
	 * configFileName
	 */
	private String configureName = "JieHunXinWu.jat";
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConfigMd5SignManange.addConfigSign(configureName, data);

		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,JieHunConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				JieHunConfig config = createJieHunConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	private JieHunConfig createJieHunConfig(Map<String, Object> tmp){
		JieHunConfig config = new JieHunConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("rarelevel")));
		config.setItemId1(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		Map<String,Long> atts = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAtts(atts);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public JieHunConfig getConfig(Integer id){
		return configs.get(id);
	}

}
