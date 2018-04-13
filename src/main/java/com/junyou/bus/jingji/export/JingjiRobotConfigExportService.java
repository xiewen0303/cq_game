package com.junyou.bus.jingji.export;

import java.util.LinkedHashMap;
import java.util.Map;

import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import org.springframework.stereotype.Service;

import com.junyou.bus.jingji.entity.JingjiRobotConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class JingjiRobotConfigExportService  extends AbsClasspathConfigureParser {

	private String configureName = "JingjiRobot.jat";
	private Map<Integer, JingjiRobotConfig> configs = new LinkedHashMap<>();
	//@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				JingjiRobotConfig config = createMingCiZhanShiConfig(tmp);
				configs.put(config.getRankId(), config);
			}
		}
	}

	//@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private JingjiRobotConfig createMingCiZhanShiConfig(Map<String, Object> tmp){
		JingjiRobotConfig config = new JingjiRobotConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setRankId(CovertObjectUtil.object2int(tmp.get("rankId")));
		config.setRoleConfigId(CovertObjectUtil.object2int(tmp.get("roleConfigId")));
		config.setSwingLevel(CovertObjectUtil.object2int(tmp.get("swingLevel")));
		config.setWuqiLevel(CovertObjectUtil.object2int(tmp.get("wuqiLevel")));
		config.setRobotName(CovertObjectUtil.object2String(tmp.get("robotName")));
		String skillD = CovertObjectUtil.obj2StrOrNull((tmp.get("skillDatas")));
		if(skillD != null) {
			config.setSkillDatas(skillD.split(";"));
		}

		Map<String, Long> robotAttrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setRobotAttrs(robotAttrs);
		return config;
	}
	
	/**
	 * 获取所有配置
	 * @return
	 */
	public Map<Integer, JingjiRobotConfig> getConfigs(){
		return configs;
	}

	public int getRobotLen(){
		return configs.size();
	}

	public JingjiRobotConfig getLastConfig(){
		return configs.get(configs.size());
	}
}
