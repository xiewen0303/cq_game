package com.junyou.bus.marry.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.marry.entity.LoveLevelConfig;
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
public class LoveLevelConfigServiceExport extends AbsClasspathConfigureParser {
	private Map<Integer,LoveLevelConfig> configs;
	private long maxValue = 0;
	/**
	 * configFileName
	 */
	private String configureName = "JieHunQinMiDu.jat";
	
	@Override
	protected void configureDataResolve(byte[] data) {
		ConfigMd5SignManange.addConfigSign(configureName, data);

		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,LoveLevelConfig> configs = new HashMap<>();
		long maxValue = 0;
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				LoveLevelConfig config = createLoveLevelConfig(tmp);
				configs.put(config.getLevel(), config);
				if(maxValue < config.getMaxLove()){
					maxValue = config.getMaxLove();
				}
			}
		}
		this.configs = configs;
		this.maxValue = maxValue;
	}
	
	private LoveLevelConfig createLoveLevelConfig(Map<String, Object> tmp){
		LoveLevelConfig config = new LoveLevelConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("qmdlevel")));
		config.setItemId1(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setItemGold(CovertObjectUtil.object2int(tmp.get("needgold")));
		config.setItemBGold(CovertObjectUtil.object2int(tmp.get("needbgold")));
		config.setAddLove(CovertObjectUtil.object2int(tmp.get("qmdone")));
		config.setMaxLove(CovertObjectUtil.object2int(tmp.get("yfzmax")));
		Map<String,Long> atts = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAtts(atts);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public LoveLevelConfig getConfig(Integer level){
		return configs.get(level);
	}
	
	public long getMaxLove(){
		return maxValue;
	}

}
