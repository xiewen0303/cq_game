package com.junyou.gameconfig.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class DropExpShuaiJianConfigExportService extends AbsClasspathConfigureParser{


	private static int MAX_LEVEL = 0;
	private static int MIN_LEVEL = 0;
	
	private Map<Integer, DropExpShuaiJianConfig> configs;
	/**
	 * 配置名
	 */
	private String configureName = "JingYanShuaiJianBiao.jat";	

	@Override
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, DropExpShuaiJianConfig> configs = new HashMap<>();
		MAX_LEVEL = 0;
		MIN_LEVEL = 0;
		for (Object obj:dataList) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				DropExpShuaiJianConfig config = new DropExpShuaiJianConfig();
				config.setLevel(CovertObjectUtil.object2int(tmp.get("lvdifference")));
				config.setLevelXishu(CovertObjectUtil.object2Float(tmp.get("subtractfactor")));
				configs.put(config.getLevel(), config);
				
				//最大值
				if(config.getLevel() > MAX_LEVEL){
					MAX_LEVEL = config.getLevel();
				}
				
				//最小值
				if(config.getLevel() < MIN_LEVEL){
					MIN_LEVEL = config.getLevel();
				}
			}
		}
		this.configs = configs;
	}

	@Override
	protected String getConfigureName() {
		
		return configureName;
	}

	public DropExpShuaiJianConfig loadByLevel(Integer level) {
		if(level > MAX_LEVEL){
			level = MAX_LEVEL;
		}else if(level < MIN_LEVEL){
			level = MIN_LEVEL;
		}
		
		return configs.get(level);
	}
}
