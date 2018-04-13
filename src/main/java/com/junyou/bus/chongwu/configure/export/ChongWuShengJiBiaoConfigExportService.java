package com.junyou.bus.chongwu.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ChongWuShengJiBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, ChongWuShengJiBiaoConfig> configs = null;

	/**
	 * configFileName
	 */
	private String configureName = "ChongWuShengJiBiao.jat";
	private int MAX_LEVEL = 0;

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, ChongWuShengJiBiaoConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ChongWuShengJiBiaoConfig config = createChongWuShengJiBiaoConfig(tmp);
				configs.put(config.getCwlevel(), config);
				if(config.getCwlevel()>MAX_LEVEL){
					MAX_LEVEL = config.getCwlevel();
				}
			}
		}
	}

	public ChongWuShengJiBiaoConfig createChongWuShengJiBiaoConfig(
			Map<String, Object> tmp) {
		ChongWuShengJiBiaoConfig config = new ChongWuShengJiBiaoConfig();
		config.setCwlevel(CovertObjectUtil.object2int(tmp.get("cwlevel")));
		Long cwexp = CovertObjectUtil.object2Long(tmp.get("cwexp"));
		config.setCwexp(cwexp==null?0L:cwexp);
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 根据level获得配置
	 * 
	 * @param level
	 * @return
	 */
	public ChongWuShengJiBiaoConfig loadByLevel(int level) {
		return configs.get(level);
	}

	public int getMAX_LEVEL() {
		return MAX_LEVEL;
	}
	
	public List<ChongWuShengJiBiaoConfig> getAfterConfig(Integer level){
		List<ChongWuShengJiBiaoConfig> ret = new ArrayList<ChongWuShengJiBiaoConfig>();
		for(Integer i=level;i<=MAX_LEVEL;i++){
			ChongWuShengJiBiaoConfig config= configs.get(i);
			if(config != null){
				ret.add(config);
			}
		}
		return ret;
	}
}