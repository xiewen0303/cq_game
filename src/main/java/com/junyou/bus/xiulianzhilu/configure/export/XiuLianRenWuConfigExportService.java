package com.junyou.bus.xiulianzhilu.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class XiuLianRenWuConfigExportService extends AbsClasspathConfigureParser {
	/**
	 * configFileName
	 */
	private String configureName = "XiuLianRenWu.jat";

	private Map<Integer, XiuLianRenWuConfig> configMap = new HashMap<>();
	
	private Map<Integer, Integer> typeMap = new HashMap<>();//map<类型ID，时间类型>
	
	private XiuLianRenWuConfig createChengHaoPeiZhiConfig(Map<String, Object> tmp) {
		XiuLianRenWuConfig config = new XiuLianRenWuConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setDay(CovertObjectUtil.object2int(tmp.get("day")));
		config.setMissionid(CovertObjectUtil.object2int(tmp.get("missionid")));
		config.setMissiontype(CovertObjectUtil.object2int(tmp.get("missiontype")));
		config.setTimeType(CovertObjectUtil.object2int(tmp.get("timetype")));
		config.setData1(CovertObjectUtil.object2int(tmp.get("para1")));
		config.setData2(CovertObjectUtil.object2String(tmp.get("para2")));
		config.setJifen(CovertObjectUtil.object2int(tmp.get("jifen")));
		
		return config;
	}

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);

		Map<Integer, XiuLianRenWuConfig> configMap = new HashMap<Integer, XiuLianRenWuConfig>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				XiuLianRenWuConfig config = createChengHaoPeiZhiConfig(tmp);
				configMap.put(config.getId(), config);
				typeMap.put(config.getMissiontype(), config.getTimeType());
			}
		}
		this.configMap = configMap;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	/**
	 * 根据类型获取配置
	 * @param day
	 * @param type
	 * @return
	 */
	public List<XiuLianRenWuConfig> getConfigByType(Integer type){
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		List<XiuLianRenWuConfig> list = new ArrayList<>();
		for(XiuLianRenWuConfig c : configMap.values()) {
			if(c.getMissiontype().intValue() == type){
				list.add(c);
			}
		}
		return list;
	}
	/**
	 * 获取某天的所有任务
	 * @param day
	 * @param type
	 * @return
	 */
	public List<XiuLianRenWuConfig> getConfigByDay(Integer day){
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		List<XiuLianRenWuConfig> list = new ArrayList<>();
		for(XiuLianRenWuConfig c : configMap.values()) {
			if(c.getDay().intValue() == day){
				list.add(c);
			}
		}
		return list;
	}
	/**
	 * 根据时间和类型获取配置
	 * @param day
	 * @param type
	 * @return
	 */
	public List<XiuLianRenWuConfig> getConfigByDayAndType(Integer day,Integer type){
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		List<XiuLianRenWuConfig> list = new ArrayList<>();
		for(XiuLianRenWuConfig c : configMap.values()) {
			if(c.getDay().intValue() <= day && c.getMissiontype().intValue() == type){
				list.add(c);
			}
		}
		return list;
	}
	

	public XiuLianRenWuConfig loadById(Integer id) {
		return configMap.get(id);
	}
	
	public Integer getTimeType(Integer type){
		return typeMap.get(type);
	}

}
