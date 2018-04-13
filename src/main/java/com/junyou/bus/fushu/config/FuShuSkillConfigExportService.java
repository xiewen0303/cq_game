package com.junyou.bus.fushu.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @author LiuYu
 * 2015-8-25 上午10:13:36
 */
@Service
public class FuShuSkillConfigExportService  extends AbsClasspathConfigureParser{
	/**
	 * 配置名
	 */
	private String configureName = "JiNengShuShuXing.jat";
	
	private Map<String, FuShuSkillConfig> configs;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<String, FuShuSkillConfig> configs = new HashMap<>();
		for (Object obj:dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				FuShuSkillConfig config = new FuShuSkillConfig();
				config.setId(CovertObjectUtil.obj2StrOrNull(tmp.get("id")));
				Map<String,Long> atts = ConfigAnalysisUtils.setAttributeValForKV(tmp);
				config.setAtts(atts);
				configs.put(config.getId(), config);
			}
		}
		this.configs = configs;
	}
	
	@Override
	protected String getConfigureName() {
		
		return configureName;
	}

	public FuShuSkillConfig loadById(String id) {
		return configs.get(id);
	}
}
