package com.junyou.bus.shenmo.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.shenmo.configure.ShenMoScoreConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-9-24 下午5:03:32
 */
@Service
public class ShenMoScoreConfigExportService extends AbsClasspathConfigureParser{
	
	private String configureName = "ShenMoGuaiWuJiFen.jat";
	private Map<String,ShenMoScoreConfig> configs;
	private Map<Integer,ShenMoScoreConfig> teamJing;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Map<String,ShenMoScoreConfig> configs = new HashMap<>();
		Map<Integer,ShenMoScoreConfig> teamJing = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShenMoScoreConfig config = createShenMoScoreConfig(tmp);
				configs.put(config.getId(), config);
				if(config.getType() == GameConstants.SHENMO_TYPE_SHUIJING){
					teamJing.put(config.getTeamId(), config);
				}
			}
		}
		this.configs = configs;
		this.teamJing = teamJing;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private ShenMoScoreConfig createShenMoScoreConfig(Map<String, Object> tmp){
		ShenMoScoreConfig config = new ShenMoScoreConfig();
		config.setId(CovertObjectUtil.object2String(tmp.get("id")));
		config.setTeamId(CovertObjectUtil.object2int(tmp.get("zheny")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setKillScore(CovertObjectUtil.object2int(tmp.get("killjf")));
		config.setGsTime(CovertObjectUtil.object2int(tmp.get("gstame")));
		config.setGsScore(CovertObjectUtil.object2int(tmp.get("gsjf")));
		config.setBuffId(CovertObjectUtil.obj2StrOrNull(tmp.get("buffid")));
		
		return config;
	}
	
	public ShenMoScoreConfig getConfig(String id){
		if(configs == null){
			return null;
		}
		return configs.get(id);
	}
	
	/**
	 * 获取本阵营水晶配置
	 * @param teamId
	 * @return
	 */
	public ShenMoScoreConfig getTeamShuiJing(Integer teamId){
		return teamJing.get(teamId);
	}
	
	
}
