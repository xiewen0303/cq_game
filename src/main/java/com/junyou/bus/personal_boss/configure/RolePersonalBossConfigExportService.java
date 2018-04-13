package com.junyou.bus.personal_boss.configure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class RolePersonalBossConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "GeRenBoss.jat";
	Map<Integer, RolePersonalBossConfig> configs = new HashMap<>();
	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, RolePersonalBossConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				RolePersonalBossConfig config = createConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
	}

	public RolePersonalBossConfig createConfig(Map<String, Object> tmp) {
		RolePersonalBossConfig config = new RolePersonalBossConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setMonsterid(CovertObjectUtil.object2String(tmp.get("monsterid")));
		config.setCishu(CovertObjectUtil.object2int(tmp.get("cishu")));
		config.setItem(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("item"))));
		config.setJlitem(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jlitem"))));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setMap(CovertObjectUtil.object2int(tmp.get("map")));
		config.setX(CovertObjectUtil.object2int(tmp.get("x")));
		config.setY(CovertObjectUtil.object2int(tmp.get("y")));
		config.setTztime(CovertObjectUtil.object2int(tmp.get("tztime")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("tztime")));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public RolePersonalBossConfig loadById(int id) {
		return configs.get(id);
	}
	public Collection<Integer> loadAllConfigId() {
		return configs.keySet();
	}
}
