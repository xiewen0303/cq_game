package com.junyou.bus.cuxiao.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 促销表
 */
@Component
public class CuxiaoConfigExportService extends AbsClasspathConfigureParser {

	/**
	 * configFileName
	 */
	private String configureName = "CuXiaoPeiZhiBiao.jat";

	private Map<Integer, CuxiaoConfig> map  = new HashMap<>();
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null) {
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		
		CuxiaoConfig config  = null;
		Map<String, Integer> rewardMap  =null;
		String rewards  = null;
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				int id =  CovertObjectUtil.object2int(tmp.get("id"));
				int taskId =  CovertObjectUtil.object2int(tmp.get("renwuid"));
				int id1 =  CovertObjectUtil.object2int(tmp.get("id1"));
				rewards =  CovertObjectUtil.object2String(tmp.get("jiangitem"));
				int needgold =  CovertObjectUtil.object2int(tmp.get("needgold"));
				int type =  CovertObjectUtil.object2int(tmp.get("type"));
				String data1 =  CovertObjectUtil.object2String(tmp.get("level"));
				rewardMap  = ConfigAnalysisUtils.getConfigMap(rewards);
				config  = new CuxiaoConfig();
				config.setId(id);
				config.setTaskId(taskId);
				config.setId1(id1);
				config.setData1(data1);
				config.setType(type);
				config.setRewards(rewardMap);
				config.setConsumeGold(needgold);
				map.put(id, config);
			}
		}
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public CuxiaoConfig getConfig(int id) {
		return map.get(id);
	}

	 
}