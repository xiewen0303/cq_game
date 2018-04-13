package com.junyou.stage.campwar.confiure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 阵营战经验配置 
 *
 * @author LiNing
 * @date 2015-04-07 12:26:52
 */
@Component
public class ZhenYinZhanJiNengBiaoConfigExportService extends AbsClasspathConfigureParser{
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhenYinZhanJiNengBiao.jat";
	private Map<String, ZhenYinZhanJiNengBiaoConfig> skillMap;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<String, ZhenYinZhanJiNengBiaoConfig> tmpSkillMap = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhenYinZhanJiNengBiaoConfig config = createZhenYinZhanJiNengBiaoConfig(tmp);
							
				tmpSkillMap.put(config.getSkillId(), config);
			}
		}
		this.skillMap = tmpSkillMap;
	}
	
	public ZhenYinZhanJiNengBiaoConfig createZhenYinZhanJiNengBiaoConfig(Map<String, Object> tmp) {
		ZhenYinZhanJiNengBiaoConfig config = new ZhenYinZhanJiNengBiaoConfig();	
							
		config.setSkillId(CovertObjectUtil.object2String(tmp.get("id")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 根据技能Id判断是否对雕像造成伤害
	 * @param skillId 技能Id
	 * @return true:造成伤害
	 */
	public boolean check(String skillId){
		return skillMap.containsKey(skillId);
	}
}