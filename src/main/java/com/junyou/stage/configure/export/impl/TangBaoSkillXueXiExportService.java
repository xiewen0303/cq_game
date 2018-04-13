package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class TangBaoSkillXueXiExportService extends AbsClasspathConfigureParser {
	
	@Autowired
	private TangBaoSkillConfigExportService tangBaoSkillConfigExportService;
	
	/**
	 * 配置名
	 */
	private String configureName = "TangBaoJiNengXueXiBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] skillXueXiData = GameConfigUtil.getResource(data);
		for (Object obj:skillXueXiData) {
			
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				String skillId = CovertObjectUtil.object2String(tmp.get("skill"));
				
				if(!CovertObjectUtil.isEmpty(skillId)){
					TangBaoSkillConfig skillConfig = tangBaoSkillConfigExportService.loadById(skillId);
					if(skillConfig == null){
						ChuanQiLog.error("tangBaoSkillConfigExportService--- skillConfig is null.skillId:"+skillId);
						continue;
					}
					createSkillXueXiConfig(tmp,skillConfig);
				}
				
			}
		}
		
	}
	
	
	private void createSkillXueXiConfig(Map<Object, Object> tmp,TangBaoSkillConfig skillConfig){
		TangBaoSkillXueXiConfig skillXueXiConfig = new TangBaoSkillXueXiConfig();
		
		skillXueXiConfig.setMinLevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		skillXueXiConfig.setLevel(CovertObjectUtil.object2int(tmp.get("skilllevel")));
		skillXueXiConfig.setNeedItem(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem1")));
		skillXueXiConfig.setCount(CovertObjectUtil.object2int(tmp.get("num")));
		skillXueXiConfig.setGold(CovertObjectUtil.object2int(tmp.get("needmoney")));
		
		Map<String,Long> attribute = new HashMap<>();
		attribute.put(EffectType.zplus.name(), CovertObjectUtil.object2Long(tmp.get("zplus")));
		if(skillConfig.getPros() != null){
			for (Entry<Integer, String> entry : skillConfig.getPros().entrySet()) {
				Long value = CovertObjectUtil.object2Long(tmp.get("provalue"+entry.getKey()));
				attribute.put(entry.getValue(), value);
			}
		}
		skillXueXiConfig.setAttributes(attribute);
		
		skillConfig.addConfig(skillXueXiConfig);
	}

	protected String getConfigureName() {
		return configureName;
	}

}
