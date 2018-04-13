package com.junyou.bus.role.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.role.entity.TangbaoConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-5-1 下午3:16:11
 */
@Service
public class TangbaoConfigExportService extends AbsClasspathConfigureParser {
	
	/**
	 * 配置名
	 */
	private String configureName = "TangBaoShuXingBiao.jat";
	private Map<Integer,TangbaoConfig> configs;

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,TangbaoConfig> configs = new HashMap<>();
		for (Object obj:dataList) {
			@SuppressWarnings("unchecked")
			Map<String,Object> tmp = (Map<String,Object>)obj;
			TangbaoConfig config = createTangbaoConfig(tmp);
			configs.put(config.getLevel(), config);
		}
		
		this.configs = configs;
	}
	
	private TangbaoConfig createTangbaoConfig(Map<String,Object> tmp){
		TangbaoConfig config = new TangbaoConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		Map<String,Long> attributeMap = ConfigAnalysisUtils.setAttributeVal(tmp);
		attributeMap.put(GameConstants.PET_HARM_KEY, CovertObjectUtil.obj2long(tmp.get("guding")));//设置固定伤害
		config.setAttribute(attributeMap);
		
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public TangbaoConfig getTangbaoConfig(Integer level){
		return configs.get(level);
	}
}
