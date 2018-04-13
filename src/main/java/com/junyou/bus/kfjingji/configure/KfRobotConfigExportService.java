package com.junyou.bus.kfjingji.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-26 下午4:57:58
 */
@Service
public class KfRobotConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "KuaFuGuaiWu.jat";
	private Map<Integer, KfRobotConfig> configs;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, KfRobotConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				KfRobotConfig config = createKfRobotConfig(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private KfRobotConfig createKfRobotConfig(Map<String, Object> tmp){
		KfRobotConfig config = new KfRobotConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.obj2StrOrNull(tmp.get("name")));
		config.setConfigId(CovertObjectUtil.object2int(tmp.get("job")));
		config.setZuoqi(CovertObjectUtil.object2int(tmp.get("yujian")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		Map<String,Long> att = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(att);
		config.setZplus(CovertObjectUtil.obj2long(tmp.get("zplus")));
		for (int i = 1; i < 5; i++) {
			String skillId = CovertObjectUtil.obj2StrOrNull(tmp.get("skill"+i));
			if(skillId != null){
				config.addSkill(SkillManager.getSkillId(skillId, 1));
			}
		}
		return config;
	}
	public KfRobotConfig loadById(Integer id){
		return configs.get(id);
	}
}
