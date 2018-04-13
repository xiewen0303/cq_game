package com.junyou.bus.tafang.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.tafang.configure.TaFangLevelExpConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-12 下午4:28:26
 */
@Service
public class TaFangLevelExpConfigServiceExport extends AbsClasspathConfigureParser{

	private String configureName = "TaFangDengJiJiaCheng.jat";
	private Map<Integer,TaFangLevelExpConfig> configs;
	private int minLv;
	private int maxLv;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		int minLv=0;
		int maxLv=0;
		Map<Integer, TaFangLevelExpConfig> tempConfigs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TaFangLevelExpConfig config = createTaFangLevelExpConfig(tmp);
				tempConfigs.put(config.getLevel(), config);
				if(minLv == 0 || minLv > config.getLevel()){
					minLv = config.getLevel();
				}else if(maxLv < config.getLevel()){
					maxLv = config.getLevel();
				}
			}
		}
		this.minLv = minLv;
		this.maxLv = maxLv;
		this.configs = tempConfigs;
	}
	
	private TaFangLevelExpConfig createTaFangLevelExpConfig(Map<String, Object> tmp){
		TaFangLevelExpConfig config = new TaFangLevelExpConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setRate(CovertObjectUtil.obj2float(tmp.get("xishu")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public TaFangLevelExpConfig getConfigByLevel(int level){
		if(configs == null){
			return null;
		}
		if(level < minLv){
			level = minLv;
		}else if(level > maxLv){
			level = maxLv;
		}
		return configs.get(level);
	}
	
}
