package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.stage.entity.Team;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 组队效果
 * @author LiuYu
 * @date 2015-2-6 上午9:42:27
 */
@Service
public class TeamConfigExportService extends AbsClasspathConfigureParser {

	
	/**
	 * 配置名
	 */
	private String configureName = "ZuDui.jat";
	private Map<Integer,TeamConfig> configs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] skillListData = GameConfigUtil.getResource(data);
		Map<Integer,TeamConfig> configs = new HashMap<>();
		int maxCount = 1;
		for (Object obj:skillListData) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				TeamConfig config = createTeamConfig(tmp);
				int count = config.getCount();
				if(count > maxCount){
					maxCount = count;
				}
				configs.put(count, config);
			}
		}
		Team.setMaxCount(maxCount);
		this.configs = configs;
	}
	
	
	/**
	 * 队伍配置信息
	 * @param tmp
	 * @return
	 */
	private TeamConfig createTeamConfig(Map<String, Object> tmp) {
		TeamConfig config = new TeamConfig();
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		Map<String,Long> att = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(att);
		return config;
	}

	protected String getConfigureName() {
		return configureName;
	}


	public TeamConfig loadByCount(int count) {
		return configs.get(count);
	}

}
