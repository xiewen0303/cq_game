package com.junyou.bus.fushu.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-8-25 上午10:13:36
 */
@Service
public class JiNengSuoDingGoldConfigExportService  extends AbsClasspathConfigureParser{
	/**
	 * 配置名
	 */
	private String configureName = "JiNengSuoDingGold.jat";
	
	private Map<Integer, JiNengSuoDingGoldConfig> configs;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, JiNengSuoDingGoldConfig> configs = new HashMap<>();
		for (Object obj:dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				JiNengSuoDingGoldConfig config = new JiNengSuoDingGoldConfig();
				config.setNum(CovertObjectUtil.object2int(tmp.get("locknum")));
				config.setGold(CovertObjectUtil.object2int(tmp.get("needgold")));
				configs.put(config.getNum(), config);
			}
		}
		this.configs = configs;
	}
	
	@Override
	protected String getConfigureName() {
		
		return configureName;
	}

	public JiNengSuoDingGoldConfig loadByCount(Integer count) {
		return configs.get(count);
	}
}
