package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 被动技能
 * @author LiuYu
 * @date 2015-3-11 上午10:38:23
 */
@Service
public class TangBaoSkillConfigExportService extends AbsClasspathConfigureParser {

	
	/**
	 * 配置名
	 */
	private String configureName = "TangBaoJiNengBiao.jat";
	private Map<String,TangBaoSkillConfig> configs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<String,TangBaoSkillConfig> configs = new HashMap<>();
		Object[] skillListData = GameConfigUtil.getResource(data);
		for (Object obj:skillListData) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				TangBaoSkillConfig skillConfig = createTangBaoSkillConfig(tmp);
				if(skillConfig == null){
					continue;
				}
				configs.put(skillConfig.getId(), skillConfig);
			}
		}
		this.configs = configs;
	}
	
	
	/**
	 * 技能信息
	 * @param tmp
	 * @return
	 */
	private TangBaoSkillConfig createTangBaoSkillConfig(Map<Object, Object> tmp) {
		String skillId = CovertObjectUtil.object2String(tmp.get("id"));
		if(CovertObjectUtil.isEmpty(skillId)){
			return null;
		}
		TangBaoSkillConfig config = new TangBaoSkillConfig();
		config.setId(skillId);
		Map<Integer,String> pros = new HashMap<>();
		for (int i = 1; i <= GameConstants.BEIDONG_PRO_COUNT; i++) {
			String pro = CovertObjectUtil.obj2StrOrNull(tmp.get("pro"+i));
			if(!CovertObjectUtil.isEmpty(pro)){
				pros.put(i, pro);
			}
		}
		config.setPros(pros);
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}


	public TangBaoSkillConfig loadById(String skillId) {
		return configs.get(skillId);
	}

}
