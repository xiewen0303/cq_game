package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 地图非战斗资源配置表 
 *
 * @author DaoZheng Yuan
 * @date 2013-10-26 16:52:02
 */
@Component
public class ZiYuanConfigExportService extends AbsClasspathConfigureParser{
	
	
	/**
	  * configFileName
	 */
	private String configureName = "ZiYuanBiao.jat";
	
	private Map<Integer,ZiYuanConfig> configs = new HashMap<>();
	private Map<String,ZiYuanMapConfig> mapConfigs = new HashMap<>();
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,ZiYuanConfig> configs = new HashMap<>();
		Map<String,ZiYuanMapConfig> mapConfigs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZiYuanConfig config = createZiYuanConfig(tmp);
								
				configs.put(config.getId(), config);
				if(config.getMapId() != null){
					ZiYuanMapConfig ziYuanMapConfig = mapConfigs.get(config.getMapId());
					if(ziYuanMapConfig == null){
						ziYuanMapConfig = new ZiYuanMapConfig();
						ziYuanMapConfig.setMapId(config.getMapId());
						
						mapConfigs.put(ziYuanMapConfig.getMapId(),ziYuanMapConfig);
					}
					ziYuanMapConfig.addZiYuan(config);
				}
				
			}
		}
		
		this.configs = configs;
		this.mapConfigs = mapConfigs;
	}
	
	private static final String SPLIT_CHAR = "\\|";
	private static final String SUB_SPLIT_CHAR = ":";
	
	public ZiYuanConfig createZiYuanConfig(Map<String, Object> tmp) {
		ZiYuanConfig config = new ZiYuanConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
		config.setMapId(CovertObjectUtil.obj2StrOrNull(tmp.get("map")));
		config.setCollectTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setRefreshTime(CovertObjectUtil.object2int(tmp.get("time1")));
		config.setDelaTime(CovertObjectUtil.object2int(tmp.get("dela")));
		config.setDelaNumber(CovertObjectUtil.object2int(tmp.get("number")));
		config.setNotice(CovertObjectUtil.object2boolean(tmp.get("gonggao")));
		
		
		//采集的物品ID
		String collectItem = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		if(!CovertObjectUtil.isEmpty(collectItem)){
			String[] collectItemStr = collectItem.split(":");
			
			config.setCollectItem(collectItemStr[0]);
			config.setCollectItemCount(Integer.parseInt(collectItemStr[1]));
		}
		
		String zuobiaos = CovertObjectUtil.object2String(tmp.get("zuobiao"));
		if(!CovertObjectUtil.isEmpty(zuobiaos)){
			List<Integer[]> zbs = new ArrayList<Integer[]>();
			
			String[] points = zuobiaos.split(SPLIT_CHAR);
			for (String point : points) {
				String[] xy = point.split(SUB_SPLIT_CHAR);
				zbs.add(new Integer[]{Integer.parseInt(xy[0]),Integer.parseInt(xy[1])});
			}
			config.setZuobiao(zbs);
		}
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZiYuanConfig loadById(Integer id){
		return configs.get(id);
	}
	
	public List<ZiYuanConfig> getZiYuanConfigByType(Integer type){
		List<ZiYuanConfig> list = new ArrayList<>();
		for (Integer id : configs.keySet()) {
			ZiYuanConfig config = configs.get(id);
			if(config != null && config.getType() == type.intValue()){
				list.add(config);
			}
		}
		return list;
	}
	
	public ZiYuanMapConfig loadByMapId(String mapId){
		return mapConfigs.get(mapId);
	}
	
	
}