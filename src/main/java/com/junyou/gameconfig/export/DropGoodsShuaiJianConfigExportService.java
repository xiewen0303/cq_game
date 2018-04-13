package com.junyou.gameconfig.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

public class DropGoodsShuaiJianConfigExportService extends AbsClasspathConfigureParser{

	
	/**
	 * 配置名
	 */
	private String configureName = "DiaoLuoShuaiJianBiao.jat";
	
	private Map<Integer, DropGoodsShuaiJianConfig> configs;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, DropGoodsShuaiJianConfig> configs = new HashMap<>();
		for (Object obj:dataList) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				DropGoodsShuaiJianConfig config = new DropGoodsShuaiJianConfig();
				config.setLevelDifference(CovertObjectUtil.object2int(tmp.get("lvdifference")));
				config.setMonsterType(CovertObjectUtil.object2int(tmp.get("difficulty")));
				configs.put(config.getMonsterType(), config);
			}
		}
		this.configs = configs;
	}
	
	@Override
	protected String getConfigureName() {
		
		return configureName;
	}

	public DropGoodsShuaiJianConfig loadByType(Integer monsterType) {
		
		return configs.get(monsterType);
	}
}
