package com.junyou.bus.yabiao.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 押镖表配置 
 *
 * @author LiNing
 * @date 2015-03-13 13:51:43
 */
@Component
public class YaBiaoConfigExportService extends AbsClasspathConfigureParser{
	
	/**
	  * configFileName
	 */
	private String configureName = "YaBiao.jat";
	private Map<Integer, YaBiaoConfig> yabiaoMap;
	private int maxYabiaoId = 0;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, YaBiaoConfig> tmpYabiaoMap = new HashMap<>();
		
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				YaBiaoConfig config = createYaBiaoConfig(tmp);
				tmpYabiaoMap.put(config.getId(), config);
				if(config.getId() > maxYabiaoId){
					maxYabiaoId = config.getId();
				}
			}
		}
		this.yabiaoMap = tmpYabiaoMap;
	}
	
	public YaBiaoConfig createYaBiaoConfig(Map<String, Object> tmp) {
		YaBiaoConfig config = new YaBiaoConfig();	
							
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setNeedmoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		config.setSpeed(CovertObjectUtil.object2int(tmp.get("speed")));
		config.setMap(CovertObjectUtil.object2int(tmp.get("map")));
		config.setUseitem(CovertObjectUtil.object2String(tmp.get("useitem")));
		config.setGongg(CovertObjectUtil.object2boolean(tmp.get("guangbo")));
		config.setXintiao(CovertObjectUtil.object2int(tmp.get("xintiao")));
		//镖车属性
		Map<String, Long> attMap = ConfigAnalysisUtils.setAttributeVal(tmp);
		if(attMap != null && attMap.size() > 0 ){
			config.setAttributeMap(new ReadOnlyMap<>(attMap));
		}
		
		//每次被攻击伤害值（伤害下限|伤害上限）
		String harm = CovertObjectUtil.object2String(tmp.get("harm"));
		if(!CovertObjectUtil.isEmpty(harm)){
			String[] harms = harm.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
			int minHarm = Integer.parseInt(harms[0]);
			int maxHarm = Integer.parseInt(harms[1]);
			config.setMinHarm(minHarm);
			config.setRollHarm(maxHarm + 1 - minHarm);
		}
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public YaBiaoConfig getYabiaoConfigById(Integer id){
		return yabiaoMap.get(id);
	}
	
	public int getMaxYabiaoId(){
		return maxYabiaoId;
	}
}