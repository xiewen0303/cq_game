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
public class ChongWuShengJieBiaoConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, Map<Integer, Map<Integer, ChongWuShengJieBiaoConfig>>> configs = null;
	private Map<Integer, ChongWuShengJieBiaoConfig> configs2 = new HashMap<Integer,ChongWuShengJieBiaoConfig>();

	/**
	 * configFileName
	 */
	private String configureName = "ChongWuShengJieBiao.jat";
	
	private int MAX_JIE = 0;
	private int MAX_CENG = 0;

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, Map<Integer, Map<Integer, ChongWuShengJieBiaoConfig>>>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ChongWuShengJieBiaoConfig config = createChongWuShengJieBiaoConfig(tmp);
				configs2.put(config.getId1(), config);
				Map<Integer, Map<Integer, ChongWuShengJieBiaoConfig>> map1 = configs
						.get(config.getId());
				if (map1 == null) {
					map1 = new HashMap<Integer, Map<Integer, ChongWuShengJieBiaoConfig>>();
					configs.put(config.getId(), map1);
				}
				Map<Integer, ChongWuShengJieBiaoConfig> map2 = map1.get(config
						.getId2());
				if (map2 == null) {
					map2 = new HashMap<Integer, ChongWuShengJieBiaoConfig>();
					map1.put(config.getId2(), map2);
				}
				map2.put(config.getId3(), config);
				if (config.getId2() > MAX_JIE) {
					MAX_JIE = config.getId2();
				}
				if (config.getId3() > MAX_CENG) {
					MAX_CENG = config.getId3();
				}
			}
		}
	}

	public ChongWuShengJieBiaoConfig createChongWuShengJieBiaoConfig(
			Map<String, Object> tmp) {
		ChongWuShengJieBiaoConfig config = new ChongWuShengJieBiaoConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setId1(CovertObjectUtil.object2int(tmp.get("id1")));
		config.setId2(CovertObjectUtil.object2int(tmp.get("id2")));
		config.setId3(CovertObjectUtil.object2int(tmp.get("id3")));
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMallid(CovertObjectUtil.object2String(tmp.get("mallid")));
		config.setExpone(CovertObjectUtil.object2int(tmp.get("expone")));
		Long expmax = CovertObjectUtil.object2Long(tmp.get("expmax"));
		config.setExpmax(expmax==null?0L:expmax);
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}
	public int getMAX_JIE() {
		return MAX_JIE;
	}

	public int getMAX_CENG() {
		return MAX_CENG;
	}
	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public ChongWuShengJieBiaoConfig loadByIdJieCeng(int id, int jie, int ceng) {
		Map<Integer, Map<Integer, ChongWuShengJieBiaoConfig>> map1 = configs
				.get(id);
		if (map1 == null) {
			return null;
		}
		Map<Integer, ChongWuShengJieBiaoConfig> map2 = map1.get(jie);
		if (map2 == null) {
			return null;
		}
		return map2.get(ceng);
	}
	public ChongWuShengJieBiaoConfig loadById(Integer id){
		return configs2.get(id);
	}
	
	public List<ChongWuShengJieBiaoConfig> getAfterConfig(Integer configId,Integer jie,Integer ceng){
		ChongWuShengJieBiaoConfig currentconfig = loadByIdJieCeng(configId, jie, ceng);
		ChongWuShengJieBiaoConfig maxConfig = loadByIdJieCeng(configId, MAX_JIE, MAX_CENG);
		List<ChongWuShengJieBiaoConfig> ret = new ArrayList<ChongWuShengJieBiaoConfig>();
		for(Integer i=currentconfig.getId1();i<=maxConfig.getId1();i++){
			ChongWuShengJieBiaoConfig config= loadById(i);
			if(config != null){
				ret.add(config);
			}
		}
		return ret;
	}

}