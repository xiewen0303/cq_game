package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class SkillXueXiExportService extends AbsClasspathConfigureParser {
	
	@Autowired
	private SkillConfigExportService skillConfigExportService;
	
	/**
	 * 配置名
	 */
	private String configureName = "JiNengXueXiBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] skillXueXiData = GameConfigUtil.getResource(data);
		for (Object obj:skillXueXiData) {
			
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				String skillId = CovertObjectUtil.object2String(tmp.get("skill"));
				
				if(!CovertObjectUtil.isEmpty(skillId)){
					SkillConfig skillConfig = skillConfigExportService.loadById(skillId);
					createSkillXueXiConfig(tmp,skillConfig);
				}
				
			}
		}
		
	}
	
	
	private void createSkillXueXiConfig(Map<Object, Object> tmp,SkillConfig skillConfig){
		SkillXueXiConfig skillXueXiConfig = new SkillXueXiConfig();
		
		skillXueXiConfig.setMinLevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		skillXueXiConfig.setSkillLevel(CovertObjectUtil.object2int(tmp.get("skilllevel")));
		skillXueXiConfig.setNeedItem(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem")));
		skillXueXiConfig.setZplus(CovertObjectUtil.object2int(tmp.get("zplus")));
		skillXueXiConfig.setFormulea(CovertObjectUtil.obj2float(tmp.get("formulea")));
		skillXueXiConfig.setFormuleb(CovertObjectUtil.obj2float(tmp.get("formuleb")));
		skillXueXiConfig.setFormulec(CovertObjectUtil.obj2float(tmp.get("formulec")));
		skillXueXiConfig.setFormuled(CovertObjectUtil.obj2float(tmp.get("formuled")));
		
		skillXueXiConfig.setZhenqi(CovertObjectUtil.object2int(tmp.get("zhenqineed")));
		skillXueXiConfig.setGold(CovertObjectUtil.object2int(tmp.get("moneyneed")));
		skillXueXiConfig.setShulian(CovertObjectUtil.object2int(tmp.get("shulian")));
		
		List<SkillEffectConfig> effects = new ArrayList<>();
		for (int i = 1; i <= skillConfig.getEffs().size(); i++) {
			String effect = skillConfig.getEffs().get(i-1);
			SkillEffectConfig effectConfig = new SkillEffectConfig();
			effectConfig.setAddeffect(effect);
			effectConfig.setRate(CovertObjectUtil.obj2float(tmp.get("addeffectodds"+i)));
			effects.add(effectConfig);
		}
		skillXueXiConfig.setEffectConfigs(effects);
		
		skillConfig.addXuexiConfigs(skillXueXiConfig);
	}

	protected String getConfigureName() {
		return configureName;
	}
}
