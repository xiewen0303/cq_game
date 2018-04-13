package com.junyou.bus.zhuansheng.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-11-2 下午2:29:27
 */
@Service
public class ZhuanShengConfigExportService extends AbsClasspathConfigureParser {
	private String configureName = "ZhuanSheng.jat";
	private Map<Integer,ZhuanShengConfig> configs;
	
	private int minLevel=Integer.MAX_VALUE;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,ZhuanShengConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhuanShengConfig config = createZhuanShengConfig(tmp);
				configs.put(config.getLevel(), config);
				if(minLevel > config.getLevel()){
					minLevel = config.getLevel();
				}
			}
		}
		this.configs = configs;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private ZhuanShengConfig createZhuanShengConfig(Map<String, Object> tmp){
		ZhuanShengConfig config = new ZhuanShengConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("lv")));
		config.setNeedLv(CovertObjectUtil.object2int(tmp.get("needlv")));
		config.setXiuwei(CovertObjectUtil.object2int(tmp.get("uplv")));
		config.setId1(CovertObjectUtil.object2String(tmp.get("needitem")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("itemnum")));
		config.setAttribute(ConfigAnalysisUtils.setAttributeVal(tmp));
		return config;
	}
	
	public ZhuanShengConfig loadByLevel(int level){
		return configs.get(level);
	}

	public int getMinLevel() {
		return minLevel;
	}
	
}
