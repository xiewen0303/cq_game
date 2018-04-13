package com.junyou.bus.vip.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.junyou.bus.vip.entity.VipConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class VipConfigExportService  extends AbsClasspathConfigureParser{
	private String configureName = "Vip.jat";
	private Map<Integer, VipConfig> idConfigs;
	private Map<Integer, VipConfig> levelConfigs;
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, VipConfig> idConfigs = new HashMap<>();
		Map<Integer, VipConfig> levelConfigs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			VipConfig config = createVipConfig(tmp);
			idConfigs.put(config.getOrder(), config);
			if(config.getTime() < 1){
				levelConfigs.put(config.getLevel(), config);
			}
		}
		this.idConfigs = idConfigs;
		this.levelConfigs = levelConfigs;
	}
	
	private VipConfig createVipConfig(Map<String, Object> tmp){
		VipConfig config = new VipConfig();
		config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setNeedExp(CovertObjectUtil.obj2long(tmp.get("needgold")));
		config.setZplus(CovertObjectUtil.object2int(tmp.get("zplus")));
		config.setTitleId(CovertObjectUtil.obj2StrOrNull(tmp.get("titleid")));
		String levelStr = CovertObjectUtil.obj2StrOrNull(tmp.get("levelitem"));
		if(levelStr != null){
			Map<String, GoodsConfigureVo> levelGoodsMap = ConfigAnalysisUtils.getConfigVoMap(levelStr);
			config.setLevelItem(levelGoodsMap);
		}
		String weekStr = CovertObjectUtil.obj2StrOrNull(tmp.get("weekitem"));
		if(weekStr != null){
			Map<String,Integer> weekItem = ConfigAnalysisUtils.getConfigMap(weekStr);
			config.setWeekItem(weekItem);
		}
		config.setTequanMap(tequanMap(tmp));
		Map<String,Long> attribute = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttribute(attribute);
		
		config.setCuilianMoneyTimes(CovertObjectUtil.object2int(tmp.get("cishu1")));
		config.setCuilianBgoldTimes(CovertObjectUtil.object2int(tmp.get("cishu2")));
		config.setCuilianGoldTimes(CovertObjectUtil.object2int(tmp.get("cishu3")));
		return config;
	}
	private String TEQUAN_SPLIT_HEAD = "tq_";
	
	private Map<String,Integer> tequanMap(Map<String, Object> tmp){
		Map<String,Integer> map = new HashMap<>();
		for (Entry<String, Object> entry : tmp.entrySet()) {
			if(entry.getKey().startsWith(TEQUAN_SPLIT_HEAD)){
				String key = entry.getKey().substring(TEQUAN_SPLIT_HEAD.length());
				map.put(key, CovertObjectUtil.object2int(entry.getValue()));
			}
		}
		return map;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	/**
	 * 根据id获取配置
	 * @param id
	 * @return
	 */
	public VipConfig getVipConfigById(Integer id){
		return idConfigs.get(id);
	}
	/**
	 * 根据VIP等级获取配置
	 * @param level
	 * @return
	 */
	public VipConfig getVipConfigByLevel(Integer level){
		return levelConfigs.get(level);
	}
}
