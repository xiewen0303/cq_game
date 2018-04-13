package com.junyou.bus.shenqi.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class ShenQiJinJieConfigExportService extends AbsClasspathConfigureParser{
	private Map<Integer, Map<Integer, ShenQiJinJieConfig>> configs = null;
	private String configureName = "ShenQiJinJie.jat";
	private Map<Integer, Integer> minLevelMap;
	private Map<Integer, Integer> maxLevelMap;

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.info(configureName +" 文件数据为空, 请检查文件是否存在!");
			return ;
		}
		
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		configs = new HashMap<>();
		minLevelMap = new HashMap<>();
		maxLevelMap = new HashMap<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShenQiJinJieConfig config = createConfig(tmp);
				Map<Integer, ShenQiJinJieConfig> map = configs.get(config.getId());
				
				if (map == null) {
					map = new HashMap<>();
					configs.put(config.getId(), map);
				}
				map.put(config.getLevel(), config);

				caculateMinLevel(config);

				caculateMaxLevel(config);
			}
		}
	}

	private void caculateMaxLevel(ShenQiJinJieConfig config) {
		Integer maxLevel = maxLevelMap.get(config.getId());
		if (maxLevel == null) {
			maxLevelMap.put(config.getId(),
					config.getLevel());
		} else {
			if (maxLevel < config.getLevel()) {
				maxLevelMap.put(config.getId(),
						config.getLevel());
			}
		}
	}

	private void caculateMinLevel(ShenQiJinJieConfig config) {
		Integer minLevel = minLevelMap.get(config.getId());
		if (minLevel == null) {
			minLevelMap.put(config.getId(),
					config.getLevel());
		} else {
			if (minLevel > config.getLevel()) {
				minLevelMap.put(config.getId(),
						config.getLevel());
			}
		}
	}

	private ShenQiJinJieConfig createConfig(Map<String, Object> tmp) {
		ShenQiJinJieConfig config = new ShenQiJinJieConfig();
		config.setId(CovertObjectUtil.object2Integer(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setConsumeId(CovertObjectUtil.object2String(tmp.get("id1")));
		config.setMallId(CovertObjectUtil.object2String(tmp.get("mallid")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setZfzMin(CovertObjectUtil.object2int(tmp.get("zfzmin")));
		config.setZfzMax(CovertObjectUtil.object2int(tmp.get("zfzmax")));
		config.setSuccessRate(CovertObjectUtil.object2int(tmp.get("pro")));
		config.setZfzMinAdd(CovertObjectUtil.object2int(tmp.get("zfzmin2")));
		config.setZfzMaxAdd(CovertObjectUtil.object2int(tmp.get("zfzmin3")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}
	
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public List<String> getConsumeIds(String id1s) {
		List<String> result = new ArrayList<>();
		if(id1s != null){
				List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1s);
				if(ids != null){
					result.addAll(ids);
				}else{
					ChuanQiLog.error("神器进阶配置错误 :没有找到大类{}对应的物品配置信息",id1s);
				}
		}
		return result; 
	}
	
	
	public ShenQiJinJieConfig getConfigByIdAndLevel(int id ,int level){
		if(configs == null || !configs.containsKey(id)){
			return null;
		}
		
		return configs.get(id).get(level);
	}
	
	public Map<Integer, Map<Integer, ShenQiJinJieConfig>> getAllConfigs(){
		return configs;
	}
	
	
	public Integer getMaxLevel(int id) {
		return maxLevelMap.get(id);
	}
	
	public Integer getMinLevel(int id) {
		return minLevelMap.get(id);
	}
}
